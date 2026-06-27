package ru.diamondshield_central.service;

import org.springframework.stereotype.Service;
import ru.diamondshield_central.dto.cardbinding.CardBindingWaitRequest;
import ru.diamondshield_central.dto.websocket.CardBindingNotification;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.PersonRepository;
import ru.diamondshield_central.repository.ReaderRepository;

import java.time.LocalDateTime;

@Service
public class CardBindingService {

    private final ReaderRepository readerRepository;
    private final PersonRepository personRepository;
    private final WebSocketNotificationService webSocketNotificationService;

    public CardBindingService(ReaderRepository readerRepository,
                              PersonRepository personRepository,
                              WebSocketNotificationService webSocketNotificationService) {
        this.readerRepository = readerRepository;
        this.personRepository = personRepository;
        this.webSocketNotificationService = webSocketNotificationService;
    }

    public void startWait(CardBindingWaitRequest request) {
        readerRepository.findById(request.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        personRepository.findById(request.getPersonId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        // Уведомляем клиентов, что по считывателю нужно ждать карту
        webSocketNotificationService.sendCardBinding(
                new CardBindingNotification(
                        "wait_started",
                        request.getReaderId(),
                        request.getPersonId(),
                        LocalDateTime.now()
                )
        );
    }

    public void cancelWait(CardBindingWaitRequest request) {
        // Отмена ожидания карты
        webSocketNotificationService.sendCardBinding(
                new CardBindingNotification(
                        "wait_cancelled",
                        request.getReaderId(),
                        request.getPersonId(),
                        LocalDateTime.now()
                )
        );
    }
}