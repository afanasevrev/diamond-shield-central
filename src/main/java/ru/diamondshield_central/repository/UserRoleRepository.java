package ru.diamondshield_central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.SystemUser;
import ru.diamondshield_central.entity.UserRole;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    List<UserRole> findBySystemUser(SystemUser systemUser);

    boolean existsBySystemUserIdAndRoleIdAndObjectId(UUID systemUserId, UUID roleId, UUID objectId);
}