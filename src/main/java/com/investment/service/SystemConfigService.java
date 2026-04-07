package com.investment.service;

import com.investment.dto.response.admin.SystemConfigResponse;
import com.investment.entity.SystemConfig;
import com.investment.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统配置服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigRepository configRepository;

    /**
     * 获取配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    @Cacheable(value = "systemConfig", key = "#configKey")
    public String getConfigValue(String configKey) {
        return configRepository.findByConfigKey(configKey)
                .map(SystemConfig::getConfigValue)
                .orElse(null);
    }

    /**
     * 获取配置值，带默认值
     *
     * @param configKey    配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String getConfigValue(String configKey, String defaultValue) {
        String value = getConfigValue(configKey);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取配置值并转换为整数
     *
     * @param configKey    配置键
     * @param defaultValue 默认值
     * @return 整数值
     */
    public Integer getConfigValueAsInt(String configKey, Integer defaultValue) {
        String value = getConfigValue(configKey);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse config value as integer: {}", configKey);
            return defaultValue;
        }
    }

    /**
     * 获取配置值并转换为布尔值
     *
     * @param configKey    配置键
     * @param defaultValue 默认值
     * @return 布尔值
     */
    public Boolean getConfigValueAsBoolean(String configKey, Boolean defaultValue) {
        String value = getConfigValue(configKey);
        if (value == null) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }

    /**
     * 获取所有配置
     *
     * @return 配置列表
     */
    public List<SystemConfigResponse> getAllConfigs() {
        List<SystemConfig> configs = configRepository.findAll();
        return configs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定分组的配置
     *
     * @param group 分组名称
     * @return 配置列表
     */
    public List<SystemConfigResponse> getConfigsByGroup(String group) {
        List<SystemConfig> configs = configRepository.findByCategoryOrderBySortOrder(group);
        return configs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有公开的配置（前端可访问）
     *
     * @return 公开配置Map
     */
    public Map<String, String> getPublicConfigs() {
        List<SystemConfig> configs = configRepository.findAllPublic();
        Map<String, String> result = new HashMap<>();
        for (SystemConfig config : configs) {
            result.put(config.getConfigKey(), config.getConfigValue());
        }
        return result;
    }

    /**
     * 更新配置
     *
     * @param configKey 配置键
     * @param value     配置值
     */
    @Transactional
    @CacheEvict(value = "systemConfig", key = "#configKey")
    public void updateConfig(String configKey, String value) {
        log.info("Updating system config: {} = {}", configKey, value);

        SystemConfig config = configRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new RuntimeException("Config not found: " + configKey));

        config.setConfigValue(value);
        configRepository.save(config);
    }

    /**
     * 批量更新配置
     *
     * @param configs 配置Map
     */
    @Transactional
    @CacheEvict(value = "systemConfig", allEntries = true)
    public void updateConfigs(Map<String, String> configs) {
        log.info("Updating system configs: {}", configs.keySet());

        for (Map.Entry<String, String> entry : configs.entrySet()) {
            configRepository.findByConfigKey(entry.getKey()).ifPresent(config -> {
                config.setConfigValue(entry.getValue());
                configRepository.save(config);
            });
        }
    }

    /**
     * 转换为响应DTO
     */
    private SystemConfigResponse convertToResponse(SystemConfig config) {
        return SystemConfigResponse.builder()
                .id(config.getId())
                .configKey(config.getConfigKey())
                .configGroup(config.getCategory())
                .configValue(config.getConfigValue())
                .defaultValue(config.getDefaultValue())
                .configType(config.getConfigType())
                .description(config.getDescription())
                .isPublic(config.getIsPublic())
                .isEditable(config.getIsEditable())
                .isSystem(config.getIsSystem())
                .sortOrder(config.getSortOrder())
                .build();
    }
}
