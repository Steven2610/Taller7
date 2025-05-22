package model;


import java.util.Date;

public record ClimaA(Date date, double temp, double humidity, double windSpeed, double visibility, double pressure) {

}
