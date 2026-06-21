package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.Reader;

import java.util.UUID;

public interface ReaderRepository extends JpaRepository<Reader, UUID> {

    Page<Reader> findByControllerId(UUID controllerId, Pageable pageable);
}