package controller;

import model.ClimaA;
import model.ClimaB;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class controlador implements Callable<ClimaB> {
    private final String path2Data;

    public controlador(String path2Data) {
        this.path2Data = path2Data;
    }

    @Override
    public ClimaB call() {
        try {
            var data = getDataAsList(path2Data);
            var tempAvg = data.stream().mapToDouble(ClimaA::temp).average().orElse(0.0);
            var humidityAvg = data.stream().mapToDouble(ClimaA::humidity).average().orElse(0.0);
            var windSpeedAvg = data.stream().mapToDouble(ClimaA::windSpeed).average().orElse(0.0);
            var visibilityAvg = data.stream().mapToDouble(ClimaA::visibility).average().orElse(0.0);
            var pressureAvg = data.stream().mapToDouble(ClimaA::pressure).average().orElse(0.0);
            return new ClimaB(tempAvg, humidityAvg, windSpeedAvg, visibilityAvg, pressureAvg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ClimaB(-1, -1, -1, -1, -1);
    }

    public List<ClimaA> getDataPublic() throws IOException {
        return getDataAsList(path2Data);
    }

    private List<ClimaA> getDataAsList(String path2Data) throws IOException {
        List<ClimaA> output = new ArrayList<>();
        var csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        // Define un formateador compatible con tu fecha, incluyendo milisegundos y offset sin ':'
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");

        try (
                Reader reader = Files.newBufferedReader(Paths.get(path2Data));
                CSVParser parser = CSVParser.parse(reader, csvFormat)
        ) {
            for (var record : parser) {
                try {
                    String rawDate = record.get("Formatted Date");
                    OffsetDateTime date = OffsetDateTime.parse(rawDate, formatter);

                    double temp = Double.parseDouble(record.get("Temperature (C)"));
                    double humidity = Double.parseDouble(record.get("Humidity"));
                    double windSpeed = Double.parseDouble(record.get("Wind Speed (km/h)"));
                    double visibility = Double.parseDouble(record.get("Visibility (km)"));
                    double pressure = Double.parseDouble(record.get("Pressure (millibars)"));

                    output.add(new ClimaA(date, temp, humidity, windSpeed, visibility, pressure));
                } catch (Exception e) {
                    System.err.println("Error al procesar línea: " + e.getMessage());
                }
            }
        }

        return output;
    }
}
