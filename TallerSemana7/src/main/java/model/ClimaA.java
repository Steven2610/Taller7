package model;

import java.time.OffsetDateTime;

public record ClimaA(OffsetDateTime date, double temp, double humidity, double windSpeed, double visibility, double pressure) {
}
