package com.a1.exoscaleathometask.service;

import com.a1.exoscaleathometask.config.ExoscaleApiProperties;
import com.a1.exoscaleathometask.util.CloudInitUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExoscaleService {
    private static final String INSTANCE_ENDPOINT = "/instance";
    private static final String TEMPLATE_ENDPOINT = "/template";
    private static final String INSTANCE_TYPE_ENDPOINT = "/instance-type";

    private final RestTemplate restTemplate;
    private final ExoscaleApiProperties apiProperties;
    private final ObjectMapper mapper;


    public Object createExoscaleInstance(String instanceRequest) {
        log.info("Creating Exoscale instance. Endpoint: {}, Request: {}", INSTANCE_ENDPOINT, instanceRequest);

        String userData = CloudInitUtil.getEncodedCloudInitScript();
        ObjectNode rootNode;
        try {
            rootNode = (ObjectNode) mapper.readTree(instanceRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for instanceRequest", e);
        }

        if (userData != null && !userData.isEmpty()) {
            rootNode.put("user-data", userData);
        }
        try {

            Object response = restTemplate.postForObject(
                    apiProperties.baseUrl() + INSTANCE_ENDPOINT,
                    rootNode,
                    Object.class
            );
            log.info("Received response for creating Exoscale instance: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error creating Exoscale instance at endpoint {}: {}", INSTANCE_ENDPOINT, e.getMessage(), e);
            throw e;
        }
    }

    public Object getAllTemplates() {
        log.info("Fetching all templates. Endpoint: {}", TEMPLATE_ENDPOINT);
        try {
            Object response = restTemplate.getForObject(
                    apiProperties.baseUrl() + TEMPLATE_ENDPOINT,
                    Object.class
            );
            log.info("Received response for fetching all templates: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error fetching templates at endpoint {}: {}", TEMPLATE_ENDPOINT, e.getMessage(), e);
            throw e;
        }
    }

    public Object getAllInstanceTypes() {
        log.info("Fetching all instance-types. Endpoint: {}", INSTANCE_TYPE_ENDPOINT);
        try {
            Object response = restTemplate.getForObject(
                    apiProperties.baseUrl() + INSTANCE_TYPE_ENDPOINT,
                    Object.class
            );
            log.info("Received response for fetching all instance-types: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error fetching instance-types at endpoint {}: {}", INSTANCE_TYPE_ENDPOINT, e.getMessage(), e);
            throw e;
        }
    }
}
