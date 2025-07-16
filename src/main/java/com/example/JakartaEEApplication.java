package com.example;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Jakarta EE 11 Application Configuration
 * 
 * This class configures the JAX-RS application and sets the base path for REST endpoints.
 */
@ApplicationPath("/api")
public class JakartaEEApplication extends Application {
    // The Application class is automatically configured by Jakarta EE
    // Additional configuration can be added here if needed
}
