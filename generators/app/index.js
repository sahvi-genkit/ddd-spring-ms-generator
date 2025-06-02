"use strict";
const Generator = require("yeoman-generator");
const chalk = require("chalk");
const yosay = require("yosay");
var mkdirp = require('mkdirp');
const path = require("path");
const fs_ = require('fs');

module.exports = class extends Generator {
  async initializing() {
    // Check for --config argument
    const configArgIndex = process.argv.indexOf("--config");
    if (configArgIndex === -1 || !process.argv[configArgIndex + 1]) {
      this.log(chalk.red("Config file path is required. Use --config <path>"));
      process.exit(1);
    }

    const configPath = path.resolve(
      process.cwd(),
      process.argv[configArgIndex + 1]
    );
    if (!this.fs.exists(configPath)) {
      this.log(chalk.red(`Config file not found: ${configPath}`));
      process.exit(1);
    }

    try {
      const configRaw = this.fs.read(configPath, "utf-8");
      this.config = JSON.parse(configRaw);

      // Validate required fields
      const requiredFields = [
        "projectName",
        "projectDescription",
        "projectArtifactoryId",
        "basePackageName",
        "apiContextPath",
        "emailContact"
      ];

      for (const field of requiredFields) {
        if (!this.config[field]) {
          throw new Error(`Missing required field: ${field}`);
        }
      }

      // Set props from config
      this.props = {
        project_name: this.config.projectName,
        project_description: this.config.projectDescription,
        project_artifactory_id: this.config.projectArtifactoryId,
        base_package_name: this.config.basePackageName,
        package_dir: this.config.basePackageName.replace(/\./g, "/"),
        api_context_path: this.config.apiContextPath,
        email_contact: this.config.emailContact,
        entities: this.config.entities || [],
        clients: this.config.clients || [],
        year: new Date().getFullYear()
      };

    } catch (e) {
      this.log(chalk.red(`Error reading config: ${e.message}`));
      process.exit(1);
    }
  }

  writing() {
    // Create project structure
    const templateData = {
      project_name: this.props.project_name,
      project_description: this.props.project_description,
      project_artifactory_id: this.props.project_artifactory_id,
      base_package_name: this.props.base_package_name,
      package_dir: this.props.package_dir,
      api_context_path: this.props.api_context_path,
      email_contact: this.props.email_contact,
      year: new Date().getFullYear(),
      clients: this.props.clients || []
    };

    // Create main resources directory
    const resourcesDir = path.join(
      this.props.project_path || ".",
      "src/main/resources"
    );
    mkdirp.sync(resourcesDir);

    // Create test resources directory
    const testResourcesDir = path.join(
      this.props.project_path || ".",
      "src/test/resources"
    );
    mkdirp.sync(testResourcesDir);

    // Copy pom.xml template
    this.fs.copyTpl(
      this.templatePath("pom.xml.template"),
      this.destinationPath("pom.xml"),
      templateData
    );

    // Copy application.yml template to main resources
    this.fs.copyTpl(
      this.templatePath("src/main/resources/application.yml"),
      path.join(resourcesDir, "application.yml"),
      templateData
    );

    // Copy application-test.yml template to test resources
    this.fs.copyTpl(
      this.templatePath("src/test/resources/application-test.yml"),
      path.join(testResourcesDir, "application-test.yml"),
      templateData
    );

    // Copy all Java template files
    // this.fs.copyTpl(
    //   this.templatePath("src/main/java/**/*.java"),
    //   this.destinationPath(this.props.project_path || "."),
    //   templateData,
    //   {},
    //   { globOptions: { dot: true } }
    // );

    // Copy all test Java template files
    this.fs.copyTpl(
      this.templatePath("**/*.java"),
      this.destinationPath(this.props.project_path || "."),
      templateData,
      {},
      { globOptions: { dot: true } }
    );

    // Copy hidden files
    this.fs.copy(
      this.templatePath(".*"),
      this.destinationPath(this.props.project_path || "."),
      templateData
    );

    // Generate value objects first
    if (this.config.valueObjects) {
      this.config.valueObjects.forEach(valueObject => {
        if (valueObject.name) {
          this._generateValueObject(valueObject);
        }
      });
    }

    // Generate entities
    if (this.config.entities) {
      this.config.entities.forEach(entity => {
        if (entity.name) {
          this._generateEntity(entity);
          if (entity.crudOperations && entity.crudOperations.enabled) {
            this._generateRepository(entity);
            this._generateService(entity);
          }

          if (entity.endpoints && entity.endpoints.length > 0) {
            this._generateController(entity);
          }
        }
      });
    }

    // Generate clients
    if (this.props.clients && this.props.clients.length > 0) {
      this.props.clients.forEach(client => {
        this._generateClient(client);
      });
    }
  }

  _generateValueObject(valueObject) {
    const valueObjectDir = path.join(
      this.props.project_path || ".",
      "src/main/java",
      this.props.package_dir,
      "domain/model"
    );
    mkdirp.sync(valueObjectDir);

    const typeMap = {
      String: "String",
      Integer: "Integer",
      Long: "Long",
      Double: "Double",
      UUID: "UUID",
      BigDecimal: "BigDecimal",
      LocalDate: "LocalDate",
      LocalDateTime: "LocalDateTime"
    };

    const imports = new Set();
    if (valueObject.type === "UUID") imports.add("java.util.UUID");
    if (valueObject.type === "BigDecimal") imports.add("java.math.BigDecimal");
    if (valueObject.type === "LocalDate") imports.add("java.time.LocalDate");
    if (valueObject.type === "LocalDateTime") imports.add("java.time.LocalDateTime");

    this.fs.copyTpl(
      this.templatePath("src/main/java/domain/model/ValueObject.java.template"),
      path.join(valueObjectDir, `${valueObject.name}.java`),
      {
        package_name: this.props.base_package_name,
        valueObject: {
          name: valueObject.name,
          type: typeMap[valueObject.type] || valueObject.type,
          description: valueObject.description,
          validation: valueObject.validation
        },
        imports: Array.from(imports)
      }
    );
  }

  _generateEntity(entity) {
    const entityDir = path.join(
      this.props.project_path || ".",
      "src/main/java",
      this.props.package_dir,
      "domain/model"
    );
    mkdirp.sync(entityDir);

    const typeMap = {
      String: "String",
      Integer: "Integer",
      Long: "Long",
      Double: "Double",
      BigDecimal: "BigDecimal",
      LocalDate: "LocalDate",
      LocalDateTime: "LocalDateTime",
      List: "List",
      Map: "Map",
      Boolean: "Boolean",
      ValueObject: "ValueObject" // Add support for ValueObject type
    };

    const imports = new Set();
    entity.fields.forEach(f => {
      if (f.type === "LocalDate") imports.add("java.time.LocalDate");
      if (f.type === "LocalDateTime") imports.add("java.time.LocalDateTime");
      if (f.type === "List") imports.add("java.util.List");
      if (f.type === "Map") imports.add("java.util.Map");
      if (f.type === "BigDecimal") imports.add("java.math.BigDecimal");
      if (f.type === "ValueObject") {
        // Add import for the referenced ValueObject
        imports.add(`${this.props.base_package_name}.domain.model.${f.valueObject}`);
      }
    });

    this.fs.copyTpl(
      this.templatePath("src/main/java/domain/model/Entity.java.template"),
      path.join(entityDir, `${entity.name}.java`),
      {
        base_package_name: this.props.base_package_name,
        entity: entity,
        imports: Array.from(imports),
        fields: entity.fields.map(f => ({
          type: typeMap[f.type] || f.type,
          name: f.name,
          valueObject: f.valueObject,
          required: f.required,
          unique: f.unique,
          description: f.description,
          validations: f.validations || []
        }))
      }
    );
  }

  _generateController(entity) {
    const controllerDir = path.join(
      this.props.project_path || ".",
      "src/main/java",
      this.props.package_dir,
      "application/interfaces"
    );
    mkdirp.sync(controllerDir);

    this.fs.copyTpl(
      this.templatePath(
        "src/main/java/application/interfaces/RestController.java.template"
      ),
      path.join(controllerDir, `${entity.name}Controller.java`),
      {
        base_package_name: this.props.base_package_name,
        entity_name: entity.name,
        api_context_path: this.props.api_context_path,
        crudOperations: entity.crudOperations || { enabled: false },
        endpoints: entity.endpoints || [],
        entity: entity
      }
    );
  }

  _generateClient(client) {
    const clientDir = path.join(
      this.props.project_path || ".",
      "src/main/java",
      this.props.package_dir,
      "infrastructure/client",
      `${client.name.toLowerCase()}`
    );
    mkdirp.sync(clientDir);

    // Generate request and response objects first
    if (client.requestTypes || client.responseTypes) {
      this._generateClientObjects(client, clientDir);
    }

    // Generate the client class
    this.fs.copyTpl(
      this.templatePath(
        "src/main/java/infrastructure/client/Client.java.template"
      ),
      path.join(clientDir, `${client.name}Client.java`),
      {
        base_package_name: this.props.base_package_name,
        client_name: client.name,
        endpoints: client.endpoints || []
      }
    );
  }

  _generateClientObjects(client, clientDir) {
    const typeMap = {
      String: "String",
      Integer: "Integer",
      Long: "Long",
      Double: "Double",
      BigDecimal: "BigDecimal",
      Boolean: "Boolean",
      LocalDate: "LocalDate",
      LocalDateTime: "LocalDateTime",
      List: "List",
      Map: "Map"
    };

    // Generate request objects
    if (client.requestTypes) {
      Object.entries(client.requestTypes).forEach(([typeName, typeDef]) => {
        const imports = new Set([
          "lombok.Data",
          "lombok.NoArgsConstructor",
          "lombok.AllArgsConstructor",
          "lombok.Builder",
          "jakarta.validation.constraints.*"
        ]);

        // Add imports based on field types
        typeDef.fields.forEach(f => {
          if (f.type === "LocalDate") imports.add("java.time.LocalDate");
          if (f.type === "LocalDateTime") imports.add("java.time.LocalDateTime");
          if (f.type === "List") imports.add("java.util.List");
          if (f.type === "Map") imports.add("java.util.Map");
          if (f.type === "BigDecimal") imports.add("java.math.BigDecimal");
        });

        this.fs.copyTpl(
          this.templatePath(
            "src/main/java/infrastructure/client/Request.java.template"
          ),
          path.join(clientDir, `${typeName}.java`),
          {
            base_package_name: this.props.base_package_name,
            client_name: client.name,
            type_name: typeName,
            imports: Array.from(imports),
            fields: typeDef.fields.map(f => ({
              type: typeMap[f.type] || f.type,
              name: f.name,
              required: f.required,
              description: f.description,
              validations: f.validations || []
            }))
          }
        );
      });
    }

    // Generate response objects
    if (client.responseTypes) {
      Object.entries(client.responseTypes).forEach(([typeName, typeDef]) => {
        const imports = new Set([
          "lombok.Data",
          "lombok.NoArgsConstructor",
          "lombok.AllArgsConstructor",
          "lombok.Builder",
          "jakarta.validation.constraints.*"
        ]);

        // Add imports based on field types
        typeDef.fields.forEach(f => {
          if (f.type === "LocalDate") imports.add("java.time.LocalDate");
          if (f.type === "LocalDateTime") imports.add("java.time.LocalDateTime");
          if (f.type === "List") imports.add("java.util.List");
          if (f.type === "Map") imports.add("java.util.Map");
          if (f.type === "BigDecimal") imports.add("java.math.BigDecimal");
        });

        this.fs.copyTpl(
          this.templatePath(
            "src/main/java/infrastructure/client/Response.java.template"
          ),
          path.join(clientDir, `${typeName}.java`),
          {
            base_package_name: this.props.base_package_name,
            client_name: client.name,
            type_name: typeName,
            imports: Array.from(imports),
            fields: typeDef.fields.map(f => ({
              type: typeMap[f.type] || f.type,
              name: f.name,
              required: f.required,
              description: f.description,
              validations: f.validations || []
            }))
          }
        );
      });
    }
  }

  _generateRepository(entity) {
    const entityName = entity.name;
    const basePackageName = this.config.basePackageName;

    this.fs.copyTpl(
      this.templatePath('src/main/java/domain/repository/Repository.java.template'),
      this.destinationPath(`src/main/java/${basePackageName.replace(/\./g, '/')}/domain/repository/${entityName}Repository.java`),
      {
        base_package_name: basePackageName,
        entity_name: entityName,
        entity: entity
      }
    );
  }

  _generateService(entity) {
    const entityName = entity.name;
    const basePackageName = this.config.basePackageName;

    // Generate service interface
    this.fs.copyTpl(
      this.templatePath('src/main/java/application/service/Service.java.template'),
      this.destinationPath(`src/main/java/${basePackageName.replace(/\./g, '/')}/application/service/${entityName}Service.java`),
      {
        base_package_name: basePackageName,
        entity_name: entityName,
        entity: entity
      }
    );

    // Generate service implementation
    this.fs.copyTpl(
      this.templatePath('src/main/java/application/service/ServiceImpl.java.template'),
      this.destinationPath(`src/main/java/${basePackageName.replace(/\./g, '/')}/application/service/${entityName}ServiceImpl.java`),
      {
        base_package_name: basePackageName,
        entity_name: entityName,
        entity: entity
      }
    );
  }

  end() {
    this.log(yosay(`${chalk.green("Project setup completed successfully!")}`));
    this.log(chalk.yellow("\nNext steps:"));
    this.log("1. cd " + (this.props.project_path || "."));
    this.log("2. ./mvnw spring-boot:run");
  }
};
