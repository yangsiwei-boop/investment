package com.investment.repository;

import com.investment.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * 根据权限编码查找权限
     *
     * @param permissionCode 权限编码
     * @return 权限
     */
    Optional<Permission> findByPermissionCode(String permissionCode);

    /**
     * 检查权限编码是否存在
     *
     * @param permissionCode 权限编码
     * @return 是否存在
     */
    boolean existsByPermissionCode(String permissionCode);

    /**
     * 根据角色ID查找权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permission.id WHERE rp.role.id = :roleId")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查找权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Query("SELECT DISTINCT p FROM Permission p " +
            "JOIN RolePermission rp ON p.id = rp.permission.id " +
            "JOIN UserRole ur ON rp.role.id = ur.role.id " +
            "WHERE ur.user.id = :userId")
    List<Permission> findByUserId(@Param("userId") Long userId);

    /**
     * 根据权限类型查找权限
     *
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<Permission> findByPermissionType(String permissionType);

    /**
     * 根据资源路径查找权限
     *
     * @param resourcePath 资源路径
     * @return 权限列表
     */
    List<Permission> findByResourcePath(String resourcePath);
}
