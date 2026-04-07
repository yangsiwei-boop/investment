package com.investment.repository;

import com.investment.entity.PrivacySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 隐私设置数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface PrivacySettingRepository extends JpaRepository<PrivacySetting, Long> {

    /**
     * 根据用户ID查找隐私设置
     *
     * @param userId 用户ID
     * @return 隐私设置
     */
    @Query("SELECT ps FROM PrivacySetting ps WHERE ps.user.id = :userId")
    Optional<PrivacySetting> findByUserId(@Param("userId") Long userId);

    /**
     * 检查用户是否已有隐私设置
     *
     * @param userId 用户ID
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(ps) > 0 THEN true ELSE false END FROM PrivacySetting ps WHERE ps.user.id = :userId")
    boolean existsByUserId(@Param("userId") Long userId);

    /**
     * 更新联系方式可见性
     *
     * @param userId    用户ID
     * @param isVisible 是否可见
     */
    @Modifying
    @Query("UPDATE PrivacySetting ps SET ps.allowShowContactInfo = :isVisible WHERE ps.user.id = :userId")
    void updateShowContact(@Param("userId") Long userId, @Param("isVisible") Boolean isVisible);

    /**
     * 更新BP可见性
     *
     * @param userId    用户ID
     * @param isVisible 是否可见
     */
    @Modifying
    @Query("UPDATE PrivacySetting ps SET ps.requireBpApproval = :isVisible WHERE ps.user.id = :userId")
    void updateShowBp(@Param("userId") Long userId, @Param("isVisible") Boolean isVisible);
}
