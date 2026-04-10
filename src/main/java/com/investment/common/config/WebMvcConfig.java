package com.investment.common.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Web MVC配置
 *
 * @author Investment Team
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将上传路径转为绝对路径，兼容 Windows/Linux
        String absolutePath = Paths.get(uploadPath).toAbsolutePath().toString();
        String resourceLocation = "file:" + absolutePath + "/";
        log.info("Upload resource location: {}", resourceLocation);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
    }
}
