package com.investment.repository;

import com.investment.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置数据访问层
 *
 * @author Investment Team
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {

    /**
     * 根据配置键查找配置
     *
     * @param configKey 配置键
     * @return 系统配置
     */
    Optional<SystemConfig> findByConfigKey(String configKey);

    /**
     * 检查配置键是否存在
     *
     * @param configKey 配置键
     * @return 是否存在
     */
    boolean existsByConfigKey(String configKey);

    /**
     * 根据配置分类查找配置列表
     *
     * @param category 配置分类
     * @return 配置列表
     */
    List<SystemConfig> findByCategoryOrderBySortOrder(String category);

    /**
     * 查找所有公开的配置
     *
     * @return 配置列表
     */
    @Query("SELECT sc FROM SystemConfig sc WHERE sc.isPublic = true ORDER BY sc.sortOrder")
    List<SystemConfig> findAllPublic();

    /**
     * 查找所有可编辑的配置
     *
     * @return 配置列表
     */
    @Query("SELECT sc FROM SystemConfig sc WHERE sc.isEditable = true ORDER BY sc.category, sc.sortOrder")
    List<SystemConfig> findAllEditable();

    /**
     * 更新配置值
     *
     * @param configKey   配置键
     * @param configValue 配置值
     */
    @Modifying
    @Query("UPDATE SystemConfig sc SET sc.configValue = :configValue WHERE sc.configKey = :configKey")
    int updateConfigValue(@Param("configKey") String configKey, @Param("configValue") String configValue);

    /**
     * 根据配置键批量查找配置
     *
     * @param configKeys 配置键列表
     * @return 配置列表
     */
    List<SystemConfig> findByConfigKeyIn(List<String> configKeys);

    /**
     * 根据环境作用域查找配置
     *
     * @param envScope 环境作用域
     * @return 配置列表
     */
    List<SystemConfig> findByEnvScopeOrderBySortOrder(String envScope);
}
