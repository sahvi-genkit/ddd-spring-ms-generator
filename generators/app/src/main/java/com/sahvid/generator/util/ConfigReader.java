package com.sahvid.generator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahvid.generator.model.EntityConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigReader {
    private static final Set<String> SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
        "String", "Integer", "Long", "Double", "LocalDate", 
        "LocalDateTime", "List", "Map", "Boolean"
    ));

    public static EntityConfig readConfig(String configPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EntityConfig config = mapper.readValue(new File(configPath), EntityConfig.class);
        validateConfig(config);
        return config;
    }

    private static void validateConfig(EntityConfig config) {
        if (config.getProjectName() == null || config.getProjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name is required");
        }
        if (config.getBasePackageName() == null || config.getBasePackageName().trim().isEmpty()) {
            throw new IllegalArgumentException("Base package name is required");
        }
        if (config.getEntityName() == null || config.getEntityName().trim().isEmpty()) {
            throw new IllegalArgumentException("Entity name is required");
        }
        if (config.getFields() == null || config.getFields().isEmpty()) {
            throw new IllegalArgumentException("At least one field is required");
        }

        for (EntityConfig.FieldConfig field : config.getFields()) {
            if (field.getName() == null || field.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Field name is required");
            }
            if (field.getType() == null || !SUPPORTED_TYPES.contains(field.getType())) {
                throw new IllegalArgumentException("Unsupported field type: " + field.getType());
            }
        }
    }
} 