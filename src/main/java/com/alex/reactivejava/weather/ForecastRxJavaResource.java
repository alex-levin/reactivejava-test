package com.alex.reactivejava.weather;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

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

import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvoker;
import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvokerProvider;

import com.alex.reactivejava.weather.model.Temperature;

import io.reactivex.Flowable;

// https://github.com/ReactiveX/RxJava
// https://mvnrepository.com/artifact/io.reactivex.rxjava2/rxjava
// RxFlowableInvokerProvider and RxFlowableInvoker are part of Jersey
// https://mvnrepository.com/artifact/org.glassfish.jersey.core/jersey-client
// https://eclipse-ee4j.github.io/jersey.github.io/documentation/3.0.0/user-guide.html
// https://www.baeldung.com/jax-rs-reactive-client
// https://learning.oreilly.com/library/view/java-ee-8/9781788475143/aca32671-db4a-4780-a29c-d43cc8d93d42.xhtml
// https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/rx-client.html

@Stateless
@Path("/rxJavaForecast")
public class ForecastRxJavaResource {

	private final String baseUri = "http://localhost:8080/RxJavaTest";

	private final Client client = ClientBuilder.newClient();

	private final WebTarget locationTarget = client.target(baseUri).path("location");

	private final WebTarget temperatureTarget = client.target(baseUri).path("temperature/{city}");

//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public void getLocationsWithTemperature(@Suspended AsyncResponse asyncResponse) {
//		asyncResponse.setTimeout(5, TimeUnit.MINUTES);
//
//		List<Location> locations = locationTarget.request().get(new GenericType<List<Location>>() {
//		});
//
//		Stream<Flowable<Forecast>> futureForecasts = locations.stream().map(location -> {
//			return temperatureTarget.register(RxFlowableInvokerProvider.class)
//					.resolveTemplate("city", location.getName()).request().rx(RxFlowableInvoker.class)
//					.get(Temperature.class).map(temperature -> new Forecast(location, temperature));
//		});
//
//		Iterable<Flowable<Forecast>> iFutureForecasts = futureForecasts::iterator;
//		Flowable.concat(iFutureForecasts).doOnNext(forecast -> {
//			response.getForecasts().add(forecast);
//		}).doOnComplete(() -> {
//			asyncResponse.resume(Response.ok(response).build());
//		}).doOnError(asyncResponse::resume).subscribe();
//
//	}
}
