package com.investment.repository;

import com.investment.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限关联数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * 根据角色ID查找权限关联
     *
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId")
    List<RolePermission> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID查找角色关联
     *
     * @param permissionId 权限ID
     * @return 角色权限关联列表
     */
    @Query("SELECT rp FROM RolePermission rp WHERE rp.permission.id = :permissionId")
    List<RolePermission> findByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 删除角色的所有权限关联
     *
     * @param roleId 角色ID
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除权限的所有角色关联
     *
     * @param permissionId 权限ID
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permission.id = :permissionId")
    void deleteByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 检查角色是否拥有权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(rp) > 0 THEN true ELSE false END FROM RolePermission rp " +
            "WHERE rp.role.id = :roleId AND rp.permission.id = :permissionId")
    boolean existsByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}
