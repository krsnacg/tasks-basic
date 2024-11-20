package com.example.tasks.controller.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@Configuration
public class DynamoDBEnhancedConfig {

    @Value("${aws.accessKeyId}")
    private String acesskey;

    @Value("${aws.secretKey}")
    private String secretkey;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .credentialsProvider(StaticCredentialsProvider
            .create(AwsBasicCredentials.create(acesskey, secretkey)))
            .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        // createTableIfNotExists(dynamoDbClient);
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    }

    /**
     * Verifica si la tabla 'climate' existe y la crea si no existe.
     */
    private void createTableIfNotExists(DynamoDbClient dynamoDbClient) {
        try {
            // Verificar si la tabla existe
            dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName("climate").build());
            System.out.println("La tabla 'climate' ya existe.");
        } catch (ResourceNotFoundException e) {
            // Si la tabla no existe, crearla
            createTable(dynamoDbClient);
        }
    }

    /**
     * Crea la tabla 'climate' si no existe.
     */
    private void createTable(DynamoDbClient dynamoDbClient) {
        String tableId = "";
        try {
            CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .tableName("climate")
                .keySchema(KeySchemaElement.builder().attributeName("clima_id").keyType(KeyType.HASH).build())  // Clave primaria (partition key)
                .attributeDefinitions(
                    AttributeDefinition.builder().attributeName("clima_id").attributeType(ScalarAttributeType.S).build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder()
                    .readCapacityUnits(5L)
                    .writeCapacityUnits(5L)
                    .build())
                .build();
            
            

            CreateTableResponse result = dynamoDbClient.createTable(createTableRequest);
            tableId = result.tableDescription().tableId();
            System.out.println("Tabla 'climate' creada exitosamente. ID: " + tableId);
        } catch (DynamoDbException e) {
            System.err.println("Error al crear la tabla " + tableId + ": " + e.getMessage());
        }
    }
    
}
