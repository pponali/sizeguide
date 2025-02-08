# Size Guide Service

A Spring Boot service for managing size guides with MongoDB integration, featuring Excel file upload capabilities, validation, and email notifications.

## Features

- Upload size guide data via Excel files
- Validate size guide data through scheduled jobs
- Email notifications for validation results
- S3 integration for template storage
- OpenTelemetry integration for observability
- Swagger/OpenAPI documentation
- MongoDB for data storage
- RESTful API endpoints

## Prerequisites

- Java 17 or higher
- MongoDB 4.4 or higher
- Maven 3.6 or higher
- AWS account with S3 access
- SMTP server for email notifications

## Configuration

The application requires several environment variables to be set:

```bash
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/sizeguide

# AWS
AWS_ACCESS_KEY=your_access_key
AWS_SECRET_KEY=your_secret_key
AWS_REGION=your_region
S3_BUCKET_NAME=your_bucket_name

# SMTP
SMTP_USERNAME=your_smtp_username
SMTP_PASSWORD=your_smtp_password

# OpenTelemetry
OTLP_ENDPOINT=your_otlp_endpoint
```

## Building the Application

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080 by default.

## API Documentation

Once the application is running, you can access the Swagger UI at:
http://localhost:8080/api/v1/swagger-ui.html

## API Endpoints

- POST /api/v1/sizeguides/upload - Upload size guide Excel file
- GET /api/v1/sizeguides/{sizeGuideId} - Get size guide by ID
- GET /api/v1/sizeguides/template - Download size guide template

## Excel Template Format

The Excel file should contain the following columns:

- P_SIZEGUIDEID
- P_DIMENSION
- P_UNIT
- P_SIZE
- P_VALUE
- P_CATEGORYCODE
- P_BRAND
- P_FITTYPE
- P_IMAGEURL

## Validation Rules

- Size Guide ID is required and must be unique
- At least one dimension is required
- Dimension name, unit, and value are required
- Values must be numeric
- No duplicate dimensions allowed for the same size and unit

## Monitoring

The application integrates with OpenTelemetry for distributed tracing. Traces are exported to the configured OTLP endpoint.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
