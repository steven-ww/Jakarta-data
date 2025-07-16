package com.example.repository;

import com.example.entity.Greeting;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Repository class for Greeting entity operations
 */
@ApplicationScoped
public class GreetingRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    /**
     * Save a greeting to the database
     */
    @Transactional
    public Greeting save(Greeting greeting) {
        logger.info("Saving greeting: " + greeting);
        
        if (greeting.getId() == null) {
            entityManager.persist(greeting);
            logger.info("Greeting persisted with ID: " + greeting.getId());
        } else {
            greeting = entityManager.merge(greeting);
            logger.info("Greeting updated with ID: " + greeting.getId());
        }
        
        return greeting;
    }

    /**
     * Find greeting by ID
     */
    public Optional<Greeting> findById(Long id) {
        logger.info("Finding greeting by ID: " + id);
        Greeting greeting = entityManager.find(Greeting.class, id);
        return Optional.ofNullable(greeting);
    }

    /**
     * Find all greetings
     */
    public List<Greeting> findAll() {
        logger.info("Finding all greetings");
        TypedQuery<Greeting> query = entityManager.createNamedQuery("Greeting.findAll", Greeting.class);
        return query.getResultList();
    }

    /**
     * Find greetings by name
     */
    public List<Greeting> findByName(String name) {
        logger.info("Finding greetings by name: " + name);
        TypedQuery<Greeting> query = entityManager.createNamedQuery("Greeting.findByName", Greeting.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    /**
     * Count greetings by name
     */
    public Long countByName(String name) {
        logger.info("Counting greetings by name: " + name);
        TypedQuery<Long> query = entityManager.createNamedQuery("Greeting.countByName", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    /**
     * Find greetings with pagination
     */
    public List<Greeting> findAll(int firstResult, int maxResults) {
        logger.info("Finding greetings with pagination: firstResult=" + firstResult + ", maxResults=" + maxResults);
        TypedQuery<Greeting> query = entityManager.createNamedQuery("Greeting.findAll", Greeting.class);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    /**
     * Delete greeting by ID
     */
    @Transactional
    public boolean deleteById(Long id) {
        logger.info("Deleting greeting by ID: " + id);
        Optional<Greeting> greeting = findById(id);
        if (greeting.isPresent()) {
            entityManager.remove(greeting.get());
            logger.info("Greeting deleted with ID: " + id);
            return true;
        }
        logger.warning("Greeting not found for deletion with ID: " + id);
        return false;
    }

    /**
     * Count total greetings
     */
    public Long count() {
        logger.info("Counting total greetings");
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(g) FROM Greeting g", Long.class);
        return query.getSingleResult();
    }
}
