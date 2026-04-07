package com.investment.repository;

import com.investment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 根据角色编码查找角色
     *
     * @param roleCode 角色编码
     * @return 角色
     */
    Optional<Role> findByRoleCode(String roleCode);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @return 是否存在
     */
    boolean existsByRoleCode(String roleCode);

    /**
     * 根据用户ID查找角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Query("SELECT r FROM Role r JOIN UserRole ur ON r.id = ur.role.id WHERE ur.user.id = :userId AND r.isEnabled = true")
    List<Role> findByUserId(@Param("userId") Long userId);

    /**
     * 查找所有启用的角色
     *
     * @return 角色列表
     */
    @Query("SELECT r FROM Role r WHERE r.isEnabled = true ORDER BY r.sortOrder")
    List<Role> findAllActive();

    /**
     * 根据名称模糊搜索
     *
     * @param keyword 关键词
     * @return 角色列表
     */
    List<Role> findByRoleNameContainingIgnoreCase(String keyword);
}
