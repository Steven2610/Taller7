import controller.PromedioAnualTask;
import controller.controlador;
import model.ClimaA;
import model.ClimaB;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String path = "C:/Users/Usuario/Documents/TallerSemana7/TallerSemana7/src/main/resources/weatherHistory.csv";
        controlador ctrl = new controlador(path);

        List<ClimaA> data;
        try {
            data = ctrl.getDataPublic();
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo CSV: " + e.getMessage());
            return;
        }

        // Promedios por año
        Map<Integer, List<ClimaA>> datosPorAnio = data.stream().collect(Collectors.groupingBy(clima -> clima.date().getYear()));

        ExecutorService executor = Executors.newFixedThreadPool(datosPorAnio.size());
        Map<Integer, Future<ClimaB>> resultados = new HashMap<>();

        for (var entry : datosPorAnio.entrySet()) {
            int anio = entry.getKey();
            List<ClimaA> datos = entry.getValue();
            resultados.put(anio, executor.submit(new PromedioAnualTask(datos)));
        }

        System.out.println(" Promedios anuales:");
        for (Map.Entry<Integer, Future<ClimaB>> entry : resultados.entrySet()) {
            int anio = entry.getKey();
            ClimaB promedio = entry.getValue().get();
            System.out.printf("Año %d → Temp: %.2f, Hum: %.2f, Viento: %.2f, Visib: %.2f, Presión: %.2f%n",
                    anio, promedio.temp(), promedio.humidity(), promedio.windSpeed(),
                    promedio.visibility(), promedio.pressure());
        }

        executor.shutdown();

        //  Valores extremos
        Optional<ClimaA> masFrio = data.stream().min(Comparator.comparingDouble(ClimaA::temp));
        Optional<ClimaA> masCalor = data.stream().max(Comparator.comparingDouble(ClimaA::temp));
        Optional<ClimaA> menorVisibilidad = data.stream().min(Comparator.comparingDouble(ClimaA::visibility));
        Optional<ClimaA> mayorVisibilidad = data.stream().max(Comparator.comparingDouble(ClimaA::visibility));
        Optional<ClimaA> menorHumedad = data.stream().min(Comparator.comparingDouble(ClimaA::humidity));
        Optional<ClimaA> mayorHumedad = data.stream().max(Comparator.comparingDouble(ClimaA::humidity));
        Optional<ClimaA> menorViento = data.stream().min(Comparator.comparingDouble(ClimaA::windSpeed));
        Optional<ClimaA> mayorViento = data.stream().max(Comparator.comparingDouble(ClimaA::windSpeed));

        System.out.println("\n Momentos extremos registrados:");

        masFrio.ifPresent(c -> System.out.printf("️ Más frío: %.2f°C en %s%n", c.temp(), c.date()));
        masCalor.ifPresent(c -> System.out.printf(" Más calor: %.2f°C en %s%n", c.temp(), c.date()));
        menorVisibilidad.ifPresent(c -> System.out.printf(" Menor visibilidad: %.2f km en %s%n", c.visibility(), c.date()));
        mayorVisibilidad.ifPresent(c -> System.out.printf(" Mayor visibilidad: %.2f km en %s%n", c.visibility(), c.date()));
        menorHumedad.ifPresent(c -> System.out.printf(" Menor humedad: %.2f%% en %s%n", c.humidity(), c.date()));
        mayorHumedad.ifPresent(c -> System.out.printf(" Mayor humedad: %.2f%% en %s%n", c.humidity(), c.date()));
        menorViento.ifPresent(c -> System.out.printf(" Menor velocidad del viento: %.2f km/h en %s%n", c.windSpeed(), c.date()));
        mayorViento.ifPresent(c -> System.out.printf(" Mayor velocidad del viento: %.2f km/h en %s%n", c.windSpeed(), c.date()));
    }
}
