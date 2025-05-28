package com.sahvid.generator.model;

import java.util.List;
import lombok.Data;

@Data
public class EntityConfig {
    private String projectName;
    private String basePackageName;
    private String entityName;
    private List<FieldConfig> fields;

    @Data
    public static class FieldConfig {
        private String type;
        private String name;
        private boolean required;
        private String description;
        private List<String> validations;
    }
} 