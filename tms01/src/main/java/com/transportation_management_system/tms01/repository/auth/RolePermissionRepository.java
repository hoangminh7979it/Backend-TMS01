package com.transportation_management_system.tms01.repository.auth;

import com.transportation_management_system.tms01.entity.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole_RoleId(Long roleId);

    @Query("SELECT rp FROM RolePermission rp JOIN FETCH rp.permission WHERE rp.role.roleId = :roleId")
    List<RolePermission> findByRole_RoleIdWithPermission(@Param("roleId") Long roleId);

    void deleteByRole_RoleId(Long roleId);

    boolean existsByRole_RoleIdAndPermission_PermissionId(Long roleId, Long permissionId);
}
