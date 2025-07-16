package com.example;

import com.example.entity.Greeting;
import com.example.repository.GreetingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service class demonstrating Jakarta EE CDI and business logic with JPA persistence
 */
@ApplicationScoped
public class HelloService {

    @Inject
    private Logger logger;

    @Inject
    private GreetingRepository greetingRepository;

    public String createGreeting(String name) {
        logger.info("Creating greeting for: " + name);
        
        String processedName = (name == null || name.trim().isEmpty()) ? "Anonymous" : name.trim();
        String message = "Hello, " + processedName + "!";
        
        // Save greeting to database
        Greeting greeting = new Greeting(processedName, message, Greeting.GreetingType.CASUAL);
        greetingRepository.save(greeting);
        
        logger.info("Greeting saved to database: " + greeting);
        
        return message;
    }
    
    public String createFormalGreeting(String name) {
        logger.info("Creating formal greeting for: " + name);
        
        String processedName = (name == null || name.trim().isEmpty()) ? "Anonymous" : name.trim();
        String message = "Good day, " + processedName + "!";
        
        // Save formal greeting to database
        Greeting greeting = new Greeting(processedName, message, Greeting.GreetingType.FORMAL);
        greetingRepository.save(greeting);
        
        logger.info("Formal greeting saved to database: " + greeting);
        
        return message;
    }
    
    /**
     * Get all greetings from database
     */
    public List<Greeting> getAllGreetings() {
        logger.info("Retrieving all greetings from database");
        return greetingRepository.findAll();
    }
    
    /**
     * Get greetings by name
     */
    public List<Greeting> getGreetingsByName(String name) {
        logger.info("Retrieving greetings by name: " + name);
        return greetingRepository.findByName(name);
    }
    
    /**
     * Get greeting statistics
     */
    public GreetingStats getGreetingStats() {
        logger.info("Retrieving greeting statistics");
        long totalGreetings = greetingRepository.count();
        return new GreetingStats(totalGreetings);
    }
    
    /**
     * Get greeting count for a specific name
     */
    public long getGreetingCountByName(String name) {
        logger.info("Getting greeting count for name: " + name);
        return greetingRepository.countByName(name);
    }
    
    // Inner class for statistics
    public static class GreetingStats {
        private long totalGreetings;
        
        public GreetingStats(long totalGreetings) {
            this.totalGreetings = totalGreetings;
        }
        
        public long getTotalGreetings() {
            return totalGreetings;
        }
        
        public void setTotalGreetings(long totalGreetings) {
            this.totalGreetings = totalGreetings;
        }
    }
}
