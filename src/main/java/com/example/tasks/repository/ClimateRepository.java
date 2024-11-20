package com.example.tasks.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.tasks.model.Climate;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

@Repository
public class ClimateRepository {
    
    private final DynamoDbTable<Climate> climateTable;
    private final DynamoDbClient dynamoDbClient;

    public ClimateRepository(DynamoDbEnhancedClient enhancedClient, DynamoDbClient dynamoDbClient) {
        this.climateTable = enhancedClient.table("datos_clima", TableSchema.fromBean(Climate.class));
        this.dynamoDbClient = dynamoDbClient;
    }

    public Climate getClimateDataById(String climaId) {
        // Key key = Key.builder().partitionValue(climaId).build();
        // System.out.println("Buscando clima con key " + key.partitionKeyValue());
        // return climateTable.getItem(r -> r.key(key)); // add a .sortValue("someValue") if you have a sort key
        GetItemRequest request = GetItemRequest.builder()
                .tableName("datos_clima") // Cambia por el nombre de tu tabla
                .key(Map.of("clima_id", AttributeValue.builder().s(climaId).build()))
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();

        if (item.isEmpty()) {
            return null;
        }

        Climate climateData = new Climate();
        climateData.setClimaId(item.get("clima_id").s());
        climateData.setPrecipitacion(item.get("precipitacion").s());
        climateData.setTemperatura(item.get("temperatura").s());
        climateData.setTime(item.get("time").s());

        return climateData;
    }

    public void saveClimate(Climate climate) {
        climateTable.putItem(climate);
    }

    public void saveAll(List<Climate> climates) {
        int i = 0;
        for (Climate climate : climates) {
            System.out.println("Guardando clima " + i++);
            climateTable.putItem(climate);
        }
    }

}
