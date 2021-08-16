package com.alex.reactivejava.weather;

import com.alex.reactivejava.weather.model.Temperature;

public class Forecast {

    private Location location;
    private Temperature temperature;

    public Forecast(Location location, Temperature temperature) {
        this.location = location;
        this.temperature = temperature;
    }

    public Location getLocation() {
        return location;
    }
    public Temperature getTemperature() {
        return temperature;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }
    
}
