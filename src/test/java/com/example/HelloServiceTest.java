package com.example;

import com.example.entity.Greeting;
import com.example.repository.GreetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HelloService with JPA functionality
 */
class HelloServiceTest {

    @Mock
    private Logger logger;

    @Mock
    private GreetingRepository greetingRepository;

    @InjectMocks
    private HelloService helloService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGreeting() {
        // Given
        String name = "John";
        Greeting savedGreeting = new Greeting(name, "Hello, John!", Greeting.GreetingType.CASUAL);
        when(greetingRepository.save(any(Greeting.class))).thenReturn(savedGreeting);

        // When
        String result = helloService.createGreeting(name);

        // Then
        assertEquals("Hello, John!", result);
        verify(greetingRepository, times(1)).save(any(Greeting.class));
        verify(logger, times(2)).info(anyString());
    }

    @Test
    void testCreateGreetingWithNullName() {
        // Given
        Greeting savedGreeting = new Greeting("Anonymous", "Hello, Anonymous!", Greeting.GreetingType.CASUAL);
        when(greetingRepository.save(any(Greeting.class))).thenReturn(savedGreeting);

        // When
        String result = helloService.createGreeting(null);

        // Then
        assertEquals("Hello, Anonymous!", result);
        verify(greetingRepository, times(1)).save(any(Greeting.class));
    }

    @Test
    void testCreateGreetingWithEmptyName() {
        // Given
        Greeting savedGreeting = new Greeting("Anonymous", "Hello, Anonymous!", Greeting.GreetingType.CASUAL);
        when(greetingRepository.save(any(Greeting.class))).thenReturn(savedGreeting);

        // When
        String result = helloService.createGreeting("   ");

        // Then
        assertEquals("Hello, Anonymous!", result);
        verify(greetingRepository, times(1)).save(any(Greeting.class));
    }

    @Test
    void testCreateFormalGreeting() {
        // Given
        String name = "Jane";
        Greeting savedGreeting = new Greeting(name, "Good day, Jane!", Greeting.GreetingType.FORMAL);
        when(greetingRepository.save(any(Greeting.class))).thenReturn(savedGreeting);

        // When
        String result = helloService.createFormalGreeting(name);

        // Then
        assertEquals("Good day, Jane!", result);
        verify(greetingRepository, times(1)).save(any(Greeting.class));
        verify(logger, times(2)).info(anyString());
    }

    @Test
    void testGetAllGreetings() {
        // Given
        List<Greeting> greetings = Arrays.asList(
            new Greeting("John", "Hello, John!", Greeting.GreetingType.CASUAL),
            new Greeting("Jane", "Good day, Jane!", Greeting.GreetingType.FORMAL)
        );
        when(greetingRepository.findAll()).thenReturn(greetings.stream());

        // When
        List<Greeting> result = helloService.getAllGreetings();

        // Then
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Jane", result.get(1).getName());
        verify(greetingRepository, times(1)).findAll();
    }

    @Test
    void testGetGreetingsByName() {
        // Given
        String name = "John";
        List<Greeting> greetings = Arrays.asList(
            new Greeting(name, "Hello, John!", Greeting.GreetingType.CASUAL),
            new Greeting(name, "Good day, John!", Greeting.GreetingType.FORMAL)
        );
        when(greetingRepository.findByName(name)).thenReturn(greetings);

        // When
        List<Greeting> result = helloService.getGreetingsByName(name);

        // Then
        assertEquals(2, result.size());
        assertEquals(name, result.get(0).getName());
        assertEquals(name, result.get(1).getName());
        verify(greetingRepository, times(1)).findByName(name);
    }

    @Test
    void testGetGreetingStats() {
        // Given
        long totalCount = 5L;
        when(greetingRepository.findAll()).thenReturn(Stream.of(
            new Greeting("Test1", "Hello, Test1!", Greeting.GreetingType.CASUAL),
            new Greeting("Test2", "Hello, Test2!", Greeting.GreetingType.CASUAL),
            new Greeting("Test3", "Hello, Test3!", Greeting.GreetingType.FORMAL),
            new Greeting("Test4", "Hello, Test4!", Greeting.GreetingType.FORMAL),
            new Greeting("Test5", "Hello, Test5!", Greeting.GreetingType.CASUAL)
        ));

        // When
        HelloService.GreetingStats result = helloService.getGreetingStats();

        // Then
        assertEquals(totalCount, result.getTotalGreetings());
        verify(greetingRepository, times(1)).findAll();
    }

    @Test
    void testGetGreetingCountByName() {
        // Given
        String name = "John";
        long count = 3L;
        when(greetingRepository.countByName(name)).thenReturn(count);

        // When
        long result = helloService.getGreetingCountByName(name);

        // Then
        assertEquals(count, result);
        verify(greetingRepository, times(1)).countByName(name);
    }
}
