package com.example;

import com.example.entity.Greeting;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Sample REST resource demonstrating Jakarta EE 11 features with Jakarta Data persistence
 */
@Path("/hello")
@ApplicationScoped
public class HelloResource {

    @Inject
    private Logger logger;

    @Inject
    private HelloService helloService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(@QueryParam("name") String name) {
        logger.info("Hello endpoint called with name: " + name);
        
        String message = helloService.createGreeting(name);
        
        return Response.ok(new HelloResponse(message)).build();
    }

    @GET
    @Path("/formal")
    @Produces(MediaType.APPLICATION_JSON)
    public Response formalHello(@QueryParam("name") String name) {
        logger.info("Formal hello endpoint called with name: " + name);
        
        String message = helloService.createFormalGreeting(name);
        
        return Response.ok(new HelloResponse(message)).build();
    }

    @GET
    @Path("/greetings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGreetings() {
        logger.info("Get all greetings endpoint called");
        
        List<Greeting> greetings = helloService.getAllGreetings();
        
        return Response.ok(greetings).build();
    }

    @GET
    @Path("/greetings/by-name")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGreetingsByName(@QueryParam("name") String name) {
        logger.info("Get greetings by name endpoint called with name: " + name);
        
        if (name == null || name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Name parameter is required")).build();
        }
        
        List<Greeting> greetings = helloService.getGreetingsByName(name.trim());
        
        return Response.ok(greetings).build();
    }

    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGreetingStats() {
        logger.info("Get greeting stats endpoint called");
        
        HelloService.GreetingStats stats = helloService.getGreetingStats();
        
        return Response.ok(stats).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGreetingCountByName(@QueryParam("name") String name) {
        logger.info("Get greeting count by name endpoint called with name: " + name);
        
        if (name == null || name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Name parameter is required")).build();
        }
        
        long count = helloService.getGreetingCountByName(name.trim());
        
        return Response.ok(new CountResponse(name.trim(), count)).build();
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok(new HealthResponse("UP", "Jakarta EE 11 Application")).build();
    }
    
    @GET
    @Path("/greetings/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGreetingById(@PathParam("id") Long id) {
        logger.info("Get greeting by ID endpoint called with ID: " + id);
        
        Optional<Greeting> greeting = helloService.getGreetingById(id);
        
        if (greeting.isPresent()) {
            return Response.ok(greeting.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Greeting not found with ID: " + id)).build();
        }
    }
    
    @DELETE
    @Path("/greetings/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGreeting(@PathParam("id") Long id) {
        logger.info("Delete greeting endpoint called with ID: " + id);
        
        boolean deleted = helloService.deleteGreeting(id);
        
        if (deleted) {
            return Response.ok(new SuccessResponse("Greeting deleted successfully")).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Greeting not found with ID: " + id)).build();
        }
    }

    // Response DTOs
    public static class HelloResponse {
        private String message;
        private long timestamp;

        public HelloResponse() {}

        public HelloResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class HealthResponse {
        private String status;
        private String application;

        public HealthResponse() {}

        public HealthResponse(String status, String application) {
            this.status = status;
            this.application = application;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }
    }

    public static class ErrorResponse {
        private String error;
        private long timestamp;

        public ErrorResponse() {}

        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class CountResponse {
        private String name;
        private long count;

        public CountResponse() {}

        public CountResponse(String name, long count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
    
    public static class SuccessResponse {
        private String message;
        private long timestamp;
        
        public SuccessResponse() {}
        
        public SuccessResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
