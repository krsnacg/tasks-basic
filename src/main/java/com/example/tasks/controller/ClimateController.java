package com.example.tasks.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tasks.model.Climate;
import com.example.tasks.repository.ClimateRepository;

@RestController
@RequestMapping("/clima")
public class ClimateController {

    private final ClimateRepository repository;

    public ClimateController(ClimateRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{clima_id}")
    public Climate getClimateData(@PathVariable String clima_id) {
        return repository.getClimateDataById(clima_id);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            List<Climate> climates = parseCSV(file);
            repository.saveAll(climates);
            return ResponseEntity.ok("Datos procesados correctamente");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo CSV");
        }
    }

    /**
     * Este método procesa el archivo CSV y lo convierte en una lista de objetos Climate.
     * 
     * @param file el archivo CSV
     * @return lista de objetos Climate
     * @throws IOException si ocurre un error al leer el archivo
     */
    private List<Climate> parseCSV(MultipartFile file) throws IOException {
        List<Climate> climates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) { // Suponiendo que hay 4 columnas: clima_id, precipitacion, temperatura, time
                    Climate climate = new Climate();
                    climate.setClimaId(fields[0]);
                    climate.setPrecipitacion(fields[1]);
                    climate.setTemperatura(fields[2]);
                    climate.setTime(fields[3]);
                    climates.add(climate);
                }
            }
        }
        return climates;
    }

    /**
     * This method is used to get a random climate objetc by using a 
     * random id between 1 and 1440
     */
    @GetMapping("/random")
    public Climate getRandomClimateData() {
        int randomId = (int) (Math.random() * 1440) + 1;
        String randomIdString = String.valueOf(randomId);
        System.err.println("Random id: " + randomIdString);
        return repository.getClimateDataById(randomIdString);
    }

}
