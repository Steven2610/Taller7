package controller;
import model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class controlador implements Callable<ClimaB> {
    private final String path2Data;


    public controlador(String path2Data) {
        this.path2Data = path2Data;
    }

    public ClimaB call() {

        try {
            var data = getDataAsList(path2Data);
            var tempAvg = data.stream().mapToDouble(ClimaA::temp).average().orElse(0.0);
            var humidityAvg = data.stream().mapToDouble(ClimaA::humidity).average().orElse(0.0);
            var windSpeedAvg = data.stream().mapToDouble(ClimaA::windSpeed).average().orElse(0.0);
            var visibilityAvg = data.stream().mapToDouble(ClimaA::visibility).average().orElse(0.0);
            var pressureAvg =data.stream().mapToDouble(ClimaA::pressure).average().orElse(0.0);

            return new ClimaB(tempAvg, humidityAvg, windSpeedAvg, visibilityAvg, pressureAvg);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return new ClimaB( -1,-1,-1,-1,-1);

    }

    //Read csv using Apache Commons CSV
    private List<ClimaA> getDataAsList(String path2Data) throws IOException {
        List<ClimaA> output = new ArrayList<>();
        var csvFormat = CSVFormat
                .RFC4180
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .get();
        try(Reader reader = Files.newBufferedReader(Paths.get(path2Data));
            CSVParser parser = CSVParser.parse(reader, csvFormat)) {

            for(var csvRecord : parser) {
                var temp = Double.parseDouble(csvRecord.get("Temperature (C)"));
                var humidity = Double.parseDouble(csvRecord.get("Humidity"));
                var windSpeed = Double.parseDouble(csvRecord.get("Wind Speed (km/h)"));
                var visibility = Double.parseDouble(csvRecord.get("Visibility (km)"));
                var pressure = Double.parseDouble(csvRecord.get("Pressure (millibars)"));

                output.add(new ClimaA(temp, humidity, windSpeed, visibility, pressure));
            }
        }

        return output;
    }

}