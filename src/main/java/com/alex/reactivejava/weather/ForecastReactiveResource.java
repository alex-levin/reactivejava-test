package com.alex.reactivejava.weather;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.alex.reactivejava.weather.model.Temperature;

// http://localhost:8080/RxJavaTest/reactiveForecast
// Response time: 10 sec

@Stateless
@Path("/reactiveForecast")
public class ForecastReactiveResource {

	private final String baseUri = "http://localhost:8080/ReactiveJavaTest";

	private final Client client = ClientBuilder.newClient();

	private final WebTarget locationTarget = client.target(baseUri).path("location");

	private final WebTarget temperatureTarget = client.target(baseUri).path("temperature/{city}");

//  https://blogs.oracle.com/javamagazine/reactive-programming-with-jax-rs
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public void getLocationsWithTemperature(@Suspended final AsyncResponse async) {
//
//		long startTime = System.currentTimeMillis();
//
//		// Create a stage on retrieving locations
//		CompletionStage<List<Location>> locationCS = locationTarget.request().rx()
//				.get(new GenericType<List<Location>>() {
//				});
//
//		// By composing another stage on the location stage
//		// created above, collect the list of forecasts
//		// as in one big completion stage
//		final CompletionStage<List<Forecast>> forecastCS = locationCS.thenCompose(locations -> {
//
//			// Create a stage for retrieving forecasts
//			// as a list of completion stages
//			List<CompletionStage<Forecast>> forecastList =
//
//					
//					// Stream locations and process each
//					// location individually
//					locations.stream().map(location -> {
//
//						// Create a stage for fetching the
//						// temperature value just for one city
//						// given by its name
//						final CompletionStage<Temperature> tempCS = temperatureTarget
//								.resolveTemplate("city", location.getName()).request().rx().get(Temperature.class);
//
//						// Then create a completable future that
//						// contains an instance of forecast
//						// with location and temperature values
//						return CompletableFuture.completedFuture(new Forecast(location)).thenCombine(tempCS,
//								Forecast::setTemperature);
//					}).collect(Collectors.toList());
//
//			// Return a final completable future instance
//			// when all provided completable futures are
//			// completed
//			return CompletableFuture.allOf(forecastList.toArray(new CompletableFuture[forecastList.size()]))
//					.thenApply(v -> forecastList.stream().map(CompletionStage::toCompletableFuture)
//							.map(CompletableFuture::join).collect(Collectors.toList()));
//		});
//
//		// Create an instance of ServiceResponse,
//		// which contains the whole list of forecasts
//		// along with the processing time.
//		// Create a completed future of it and combine to
//		// forecastCS in order to retrieve the forecasts
//		// and set into service response
//		CompletableFuture.completedFuture(new ServiceResponse()).thenCombine(forecastCS, ServiceResponse::forecasts)
//				.whenCompleteAsync((response, throwable) -> {
//					response.setProcessingTime(System.currentTimeMillis() - startTime);
//					async.resume(response);
//				});
//	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void getLocationsWithTemperature(@Suspended AsyncResponse asyncResponse) {
		asyncResponse.setTimeout(5, TimeUnit.MINUTES);

		// initial completed stage to reduce all stages into one
		CompletionStage<List<Forecast>> initialStage = CompletableFuture.completedFuture(new ArrayList<>());

		List<Location> locations = locationTarget.request().get(new GenericType<List<Location>>() {
		});

		CompletionStage<List<Forecast>> finalStage = locations.stream()
				// for each location, call a service and return a CompletionStage
				.map(location -> {
					System.out.println(">>> Getting temperature for " + location.toString());
					return temperatureTarget.resolveTemplate("city", location.getName()).request().rx()
							.get(Temperature.class).thenApply(temperature -> new Forecast(location, temperature));
				})
				// reduce stages using thenCombine, which joins 2 stages into 1
				.reduce(initialStage, (combinedStage, forecastStage) -> {
					return combinedStage.thenCombine(forecastStage, (forecasts, forecast) -> {
						forecasts.add(forecast);
						return forecasts;
					});
				}, (stage1, stage2) -> null); // a combiner is not needed

		// complete the response with forecasts
		finalStage.thenAccept(forecasts -> {
			asyncResponse.resume(Response.ok(forecasts).build());
		})
				// handle an exception and complete the response with it
				.exceptionally(e -> {
					// unwrap the real exception if wrapped in CompletionException)
					Throwable cause = (e instanceof CompletionException) ? e.getCause() : e;
					asyncResponse.resume(cause);
					return null;
				});
	}
}
