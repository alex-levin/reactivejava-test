package com.alex.reactivejava.weather;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.alex.reactivejava.weather.model.Temperature;

// Test: http://localhost:8080/RxJavaTest/forecast
// Response time: 30 sec

@Stateless
@Path("/forecast")
public class ForecastResource {

	private final String baseUri = "http://localhost:8080/ReactiveJavaTest";

	private final Client client = ClientBuilder.newClient();

	private final WebTarget locationTarget = client.target(baseUri).path("location");

	private final WebTarget temperatureTarget = client.target(baseUri).path("temperature/{city}");

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationsWithTemperature() {
		long startTime = System.currentTimeMillis();
		ServiceResponse response = new ServiceResponse();

		List<Location> locations = locationTarget.request().get(new GenericType<List<Location>>() {
		});

		locations.forEach(location -> {
			Temperature temperature = temperatureTarget.resolveTemplate("city", location.getName()).request()
					.get(Temperature.class);
			Forecast forecast = new Forecast(location, temperature);
			response.getForecasts().add(forecast);
		});
		long endTime = System.currentTimeMillis();
		response.setProcessingTime(endTime - startTime);

		return Response.ok(response).build();
	}
}