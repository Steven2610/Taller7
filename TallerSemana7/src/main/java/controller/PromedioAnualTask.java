package controller;

import model.ClimaA;
import model.ClimaB;

import java.util.List;
import java.util.concurrent.Callable;

public class PromedioAnualTask implements Callable<ClimaB> {
    private final List<ClimaA> datos;

    public PromedioAnualTask(List<ClimaA> datos) {
        this.datos = datos;
    }

    @Override
    public ClimaB call() {
        double tempAvg = datos.stream().mapToDouble(ClimaA::temp).average().orElse(0.0);
        double humidityAvg = datos.stream().mapToDouble(ClimaA::humidity).average().orElse(0.0);
        double windSpeedAvg = datos.stream().mapToDouble(ClimaA::windSpeed).average().orElse(0.0);
        double visibilityAvg = datos.stream().mapToDouble(ClimaA::visibility).average().orElse(0.0);
        double pressureAvg = datos.stream().mapToDouble(ClimaA::pressure).average().orElse(0.0);

        return new ClimaB(tempAvg, humidityAvg, windSpeedAvg, visibilityAvg, pressureAvg);
    }
}
