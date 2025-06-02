package com.sahvid.generator.model;

import java.util.List;
import lombok.Data;

@Data
public class ProjectConfig {
    private String projectName;
    private String projectDescription;
    private String projectArtifactoryId;
    private String basePackageName;
    private String apiContextPath;
    private String emailContact;
    private List<ValueObjectConfig> valueObjects;
    private List<EntityConfig> entities;
    private List<ClientConfig> clients;

    @Data
    public static class ValueObjectConfig {
        private String name;
        private String type;
        private String description;
        private ValidationConfig validation;

        @Data
        public static class ValidationConfig {
            private String type;
            private String message;
            private String value;
            private Integer min;
            private Integer max;
            private String regexp;
        }
    }

    @Data
    public static class EntityConfig {
        private String name;
        private String tableName;
        private List<FieldConfig> fields;

        @Data
        public static class FieldConfig {
            private String type;
            private String name;
            private String valueObject; // Reference to ValueObject name if type is "ValueObject"
            private boolean required;
            private boolean unique;
            private String description;
            private List<ValidationConfig> validations;
        }
    }

    @Data
    public static class ClientConfig {
        private String name;
        private String baseUrl;
        private int connectTimeout = 5000;
        private int readTimeout = 5000;
        private int maxTotalConnections = 100;
        private int maxPerRouteConnections = 20;
        private boolean enableRetry = true;
        private int maxRetries = 3;
        private int retryDelay = 1000;
        private List<EndpointConfig> endpoints;

        @Data
        public static class EndpointConfig {
            private String name;
            private String path;
            private String method; // GET, POST, PUT, DELETE
            private String requestType;
            private String responseType;
            private List<String> headers;
            private boolean retryable = true;
        }
    }
} 