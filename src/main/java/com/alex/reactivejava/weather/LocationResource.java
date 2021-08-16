package com.alex.reactivejava.weather;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/location")
public class LocationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocations() {

		List<Location> locations = new ArrayList<>();
		locations.add(new Location("London"));
		locations.add(new Location("Istanbul"));
		locations.add(new Location("Prague"));

		return Response.ok(new GenericEntity<List<Location>>(locations) {
		}).build();
	}
}
