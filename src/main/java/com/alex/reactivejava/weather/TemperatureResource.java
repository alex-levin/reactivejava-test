package com.alex.reactivejava.weather;

import java.util.Random;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.alex.reactivejava.weather.model.Temperature;
import com.alex.reactivejava.weather.model.TemperatureScale;

@Stateless
@Path("/temperature")
public class TemperatureResource {

	@GET
	@Path("/{city}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAverageTemperature(@PathParam("city") String cityName) {
		System.out.println(">>> in getAverageTemperature for " + cityName);
		Temperature temperature = new Temperature();
		temperature.setTemperature((double) (new Random().nextInt(20) + 30));
		temperature.setTemperatureScale(TemperatureScale.FAHRENHEIT);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException ignored) {
		}

		return Response.ok(temperature).build();
	}
}
