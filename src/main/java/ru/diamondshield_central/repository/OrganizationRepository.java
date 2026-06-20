package ru.diamondshield_central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.Organization;

import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByNameIgnoreCase(String name);
}