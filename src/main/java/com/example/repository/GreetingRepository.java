package com.example.repository;

import com.example.entity.Greeting;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Delete;
import jakarta.data.repository.Save;

import java.util.List;
import java.util.Optional;

/**
 * Jakarta Data Repository interface for Greeting entity operations
 * This interface automatically provides CRUD operations without implementation
 */
@Repository
public interface GreetingRepository extends CrudRepository<Greeting, Long> {

    /**
     * Find greetings by name
     * Jakarta Data automatically implements this method based on the method name
     */
    List<Greeting> findByName(String name);

    /**
     * Count greetings by name
     * Jakarta Data automatically implements this method based on the method name
     */
    long countByName(String name);


    /**
     * Find greetings containing name (case-insensitive)
     * Jakarta Data automatically implements this method based on the method name
     */
    List<Greeting> findByNameContainingIgnoreCase(String name);

    /**
     * Find greetings ordered by creation timestamp
     * Jakarta Data automatically implements this method based on the method name
     */
    List<Greeting> findAllByOrderByCreatedAtDesc();

    /**
     * Custom query to find greetings by name prefix
     * Uses @Query annotation for custom JPQL
     */
    @Query("SELECT g FROM Greeting g WHERE g.name LIKE :namePrefix%")
    List<Greeting> findByNamePrefix(String namePrefix);

    /**
     * Delete greetings by name
     * Jakarta Data automatically implements this method based on the method name
     */
    @Delete
    void deleteByName(String name);

    /**
     * Check if greeting exists by name
     * Jakarta Data automatically implements this method based on the method name
     */
    boolean existsByName(String name);
}
