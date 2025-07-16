package com.example;

import com.example.entity.Greeting;
import com.example.repository.GreetingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service class demonstrating Jakarta EE CDI and business logic with Jakarta Data persistence
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
        return greetingRepository.findAll().collect(Collectors.toList());
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
        long totalGreetings = greetingRepository.findAll().count();
        return new GreetingStats(totalGreetings);
    }
    
    /**
     * Get greeting count for a specific name
     */
    public long getGreetingCountByName(String name) {
        logger.info("Getting greeting count for name: " + name);
        return greetingRepository.countByName(name);
    }
    
    /**
     * Get greeting by ID
     */
    public Optional<Greeting> getGreetingById(Long id) {
        logger.info("Retrieving greeting by ID: " + id);
        return greetingRepository.findById(id);
    }
    
    /**
     * Delete greeting by ID
     */
    public boolean deleteGreeting(Long id) {
        logger.info("Deleting greeting by ID: " + id);
        Optional<Greeting> greeting = greetingRepository.findById(id);
        if (greeting.isPresent()) {
            greetingRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Get ordered greetings by creation date (newest first)
     * Demonstrates Jakarta Data derived query methods
     */
    public List<Greeting> getOrderedGreetings() {
        logger.info("Retrieving greetings ordered by creation date");
        return greetingRepository.findAllByOrderByCreatedAtDesc();
    }
    
    /**
     * Search greetings by name containing a substring (case-insensitive)
     * Demonstrates Jakarta Data derived query methods with conditions
     */
    public List<Greeting> searchGreetingsByName(String nameSubstring) {
        logger.info("Searching greetings by name containing: " + nameSubstring);
        return greetingRepository.findByNameContainingIgnoreCase(nameSubstring);
    }
    
    /**
     * Get greetings by name prefix using custom query
     * Demonstrates Jakarta Data custom @Query annotation
     */
    public List<Greeting> getGreetingsByNamePrefix(String prefix) {
        logger.info("Getting greetings by name prefix: " + prefix);
        return greetingRepository.findByNamePrefix(prefix);
    }
    
    /**
     * Check if a greeting exists for a given name
     * Demonstrates Jakarta Data exists query methods
     */
    public boolean greetingExistsForName(String name) {
        logger.info("Checking if greeting exists for name: " + name);
        return greetingRepository.existsByName(name);
    }
    
    /**
     * Delete all greetings for a specific name
     * Demonstrates Jakarta Data delete methods
     */
    public void deleteGreetingsByName(String name) {
        logger.info("Deleting all greetings for name: " + name);
        greetingRepository.deleteByName(name);
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
