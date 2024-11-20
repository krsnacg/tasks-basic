package com.example.tasks.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Climate {
    
    private String climaId;
    private String precipitacion;
    private String temperatura;
    private String time;

    @DynamoDbPartitionKey
    public String getClimaId() {
        return this.climaId;
    }
    public void setClimaId(String clima_id) {
        this.climaId = clima_id;
    }
    public String getPrecipitacion() {
        return precipitacion;
    }
    public void setPrecipitacion(String precipitacion) {
        this.precipitacion = precipitacion;
    }
    public String getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    
}
