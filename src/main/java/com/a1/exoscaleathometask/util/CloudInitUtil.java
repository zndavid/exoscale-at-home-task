package com.a1.exoscaleathometask.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@UtilityClass
@Slf4j
public class CloudInitUtil {
    public static String getEncodedCloudInitScript() {
        try {
            Path filePath = ResourceUtils.getFile("classpath:cloud-init.yaml").toPath();
            byte[] fileContent = Files.readAllBytes(filePath);
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (FileNotFoundException e) {
            log.error("Cloud-init script file not found", e);
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load cloud-init script", e);
        }
    }
}
