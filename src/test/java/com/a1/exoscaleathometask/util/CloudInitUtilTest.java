package com.a1.exoscaleathometask.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CloudInitUtilTest {
    @Test
    public void testGetEncodedCloudInitScript() throws IOException {
        ClassPathResource resource = new ClassPathResource("cloud-init.yaml");
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());

        String encodedScript = CloudInitUtil.getEncodedCloudInitScript();

        String expectedEncodedString = Base64.getEncoder().encodeToString(fileContent);
        assertEquals(expectedEncodedString, encodedScript, "Encoded cloud-init script does not match expected value");
    }
}