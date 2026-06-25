package ru.diamondshield_central.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.diamondshield_central.dto.imports.PersonImportResponse;
import ru.diamondshield_central.entity.ImportHistory;
import ru.diamondshield_central.entity.ImportHistoryDetail;
import ru.diamondshield_central.entity.Organization;
import ru.diamondshield_central.entity.Person;
import ru.diamondshield_central.entity.SystemUser;
import ru.diamondshield_central.exception.BadRequestException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.ImportHistoryDetailRepository;
import ru.diamondshield_central.repository.ImportHistoryRepository;
import ru.diamondshield_central.repository.OrganizationRepository;
import ru.diamondshield_central.repository.PersonRepository;
import ru.diamondshield_central.security.CustomUserDetails;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class PersonImportService {

    private final PersonRepository personRepository;
    private final OrganizationRepository organizationRepository;
    private final ImportHistoryRepository importHistoryRepository;
    private final ImportHistoryDetailRepository importHistoryDetailRepository;
    private final PersonService personService;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public PersonImportService(PersonRepository personRepository,
                               OrganizationRepository organizationRepository,
                               ImportHistoryRepository importHistoryRepository,
                               ImportHistoryDetailRepository importHistoryDetailRepository,
                               PersonService personService,
                               AuditService auditService,
                               ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.organizationRepository = organizationRepository;
        this.importHistoryRepository = importHistoryRepository;
        this.importHistoryDetailRepository = importHistoryDetailRepository;
        this.personService = personService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public PersonImportResponse importPersons(UUID organizationId,
                                              MultipartFile file,
                                              HttpServletRequest httpRequest) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getOriginalFilename() == null || !file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            throw new BadRequestException("Only XLSX files are supported");
        }

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        ImportHistory history = new ImportHistory();
        history.setOrganization(organization);
        history.setSystemUser(getCurrentUserOrNull());
        history.setFileName(file.getOriginalFilename());
        history.setImportType("persons_xlsx");
        history.setStatus("processing");

        ImportHistory savedHistory = importHistoryRepository.save(history);

        int totalRows = 0;
        int successRows = 0;
        int skippedRows = 0;

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;

            if (sheet == null) {
                throw new BadRequestException("XLSX file does not contain a sheet");
            }

            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                throw new BadRequestException("XLSX file does not contain a header row");
            }

            Map<String, Integer> columns = readHeader(headerRow);

            validateRequiredColumns(columns);

            int lastRow = sheet.getLastRowNum();

            for (int rowIndex = 1; rowIndex <= lastRow; rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                totalRows++;

                Map<String, String> raw = readRow(row, columns);

                try {
                    String lastName = raw.get("last_name");
                    String firstName = raw.get("first_name");
                    String personType = raw.get("person_type");
                    String personnelNumber = emptyToNull(raw.get("personnel_number"));

                    if (isBlank(lastName) || isBlank(firstName) || isBlank(personType)) {
                        skippedRows++;
                        saveDetail(savedHistory, rowIndex + 1, "skipped", "Required fields are missing", raw);
                        continue;
                    }

                    personService.validatePersonType(personType);

                    if (personnelNumber != null &&
                            personRepository.existsByOrganizationIdAndPersonnelNumber(organizationId, personnelNumber)) {
                        skippedRows++;
                        saveDetail(savedHistory, rowIndex + 1, "skipped", "Personnel number already exists", raw);
                        continue;
                    }

                    Person person = new Person();
                    person.setOrganization(organization);
                    person.setLastName(lastName.trim());
                    person.setFirstName(firstName.trim());
                    person.setMiddleName(emptyToNull(raw.get("middle_name")));
                    person.setPersonType(personType.trim());
                    person.setPersonnelNumber(personnelNumber);
                    person.setBirthDate(parseBirthDate(raw.get("birth_date")));
                    person.setPhone(emptyToNull(raw.get("phone")));
                    person.setEmail(emptyToNull(raw.get("email")));
                    person.setDocumentType(emptyToNull(raw.get("document_type")));
                    person.setDocumentSeries(emptyToNull(raw.get("document_series")));
                    person.setDocumentNumber(emptyToNull(raw.get("document_number")));
                    person.setActive(true);

                    personRepository.save(person);

                    successRows++;
                    saveDetail(savedHistory, rowIndex + 1, "added", null, raw);
                } catch (Exception ex) {
                    skippedRows++;
                    saveDetail(savedHistory, rowIndex + 1, "error", ex.getMessage(), raw);
                }
            }

            savedHistory.setTotalRows(totalRows);
            savedHistory.setSuccessRows(successRows);
            savedHistory.setSkippedRows(skippedRows);

            if (skippedRows == 0) {
                savedHistory.setStatus("success");
            } else if (successRows > 0) {
                savedHistory.setStatus("partial");
            } else {
                savedHistory.setStatus("error");
            }

            importHistoryRepository.save(savedHistory);

            PersonImportResponse response = buildResponse(savedHistory);

            auditService.log("PERSONS_XLSX_IMPORTED", "import_history", savedHistory.getId(), null, response, httpRequest);

            return response;
        } catch (BadRequestException ex) {
            savedHistory.setStatus("error");
            savedHistory.setErrorMessage(ex.getMessage());
            importHistoryRepository.save(savedHistory);
            throw ex;
        } catch (Exception ex) {
            savedHistory.setStatus("error");
            savedHistory.setErrorMessage("Cannot process XLSX file");
            importHistoryRepository.save(savedHistory);
            throw new BadRequestException("Cannot process XLSX file");
        }
    }

    private Map<String, Integer> readHeader(Row headerRow) {
        Map<String, Integer> columns = new HashMap<>();

        for (Cell cell : headerRow) {
            String name = getCellValueAsString(cell);

            if (name != null && !name.isBlank()) {
                // Заголовки приводим к нижнему регистру
                columns.put(name.trim().toLowerCase(), cell.getColumnIndex());
            }
        }

        return columns;
    }

    private void validateRequiredColumns(Map<String, Integer> columns) {
        List<String> required = List.of("last_name", "first_name", "person_type");

        for (String column : required) {
            if (!columns.containsKey(column)) {
                throw new BadRequestException("Required column is missing: " + column);
            }
        }
    }

    private Map<String, String> readRow(Row row, Map<String, Integer> columns) {
        Map<String, String> result = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : columns.entrySet()) {
            Cell cell = row.getCell(entry.getValue());
            result.put(entry.getKey(), getCellValueAsString(cell));
        }

        return result;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        DataFormatter formatter = new DataFormatter();

        // DataFormatter позволяет читать строковое представление разных типов ячеек
        return formatter.formatCellValue(cell);
    }

    private boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            String value = getCellValueAsString(cell);
            if (value != null && !value.isBlank()) {
                return false;
            }
        }

        return true;
    }

    private LocalDate parseBirthDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            // Основной ожидаемый формат: yyyy-MM-dd
            return LocalDate.parse(value.trim());
        } catch (Exception ignored) {
            return null;
        }
    }

    private void saveDetail(ImportHistory history,
                            int rowNumber,
                            String status,
                            String reason,
                            Map<String, String> raw) {
        try {
            ImportHistoryDetail detail = new ImportHistoryDetail();
            detail.setImportHistory(history);
            detail.setRowNumber(rowNumber);
            detail.setStatus(status);
            detail.setReason(reason);
            detail.setRawData(objectMapper.writeValueAsString(raw));

            importHistoryDetailRepository.save(detail);
        } catch (Exception ignored) {
            // Ошибка сохранения детали не должна останавливать весь импорт
        }
    }

    private PersonImportResponse buildResponse(ImportHistory history) {
        PersonImportResponse response = new PersonImportResponse();
        response.setImportHistoryId(history.getId());
        response.setStatus(history.getStatus());
        response.setTotalRows(history.getTotalRows());
        response.setSuccessRows(history.getSuccessRows());
        response.setSkippedRows(history.getSkippedRows());

        if ("success".equals(history.getStatus())) {
            response.setMessage("Import completed successfully");
        } else if ("partial".equals(history.getStatus())) {
            response.setMessage("Import completed with skipped rows");
        } else {
            response.setMessage("Import completed with errors");
        }

        return response;
    }

    private SystemUser getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }

        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}