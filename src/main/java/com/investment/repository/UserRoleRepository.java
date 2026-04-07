package com.investment.repository;

import com.investment.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * 根据用户ID查找用户角色关联
     *
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
    List<UserRole> findByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查找用户角色关联
     *
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId")
    List<UserRole> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除用户的所有角色关联
     *
     * @param userId 用户ID
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    /**
     * 删除角色的所有用户关联
     *
     * @param roleId 角色ID
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.role.id = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 检查用户是否拥有角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END FROM UserRole ur " +
            "WHERE ur.user.id = :userId AND ur.role.id = :roleId")
    boolean existsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
