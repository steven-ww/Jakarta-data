package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * JPA Entity for storing greetings in the database
 */
@Entity
@Table(name = "greetings")
@NamedQueries({
    @NamedQuery(name = "Greeting.findAll", query = "SELECT g FROM Greeting g ORDER BY g.createdAt DESC"),
    @NamedQuery(name = "Greeting.findByName", query = "SELECT g FROM Greeting g WHERE g.name = :name ORDER BY g.createdAt DESC"),
    @NamedQuery(name = "Greeting.countByName", query = "SELECT COUNT(g) FROM Greeting g WHERE g.name = :name")
})
public class Greeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 255)
    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "greeting_type", length = 50)
    @Enumerated(EnumType.STRING)
    private GreetingType greetingType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public Greeting() {}

    public Greeting(String name, String message, GreetingType greetingType) {
        this.name = name;
        this.message = message;
        this.greetingType = greetingType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GreetingType getGreetingType() {
        return greetingType;
    }

    public void setGreetingType(GreetingType greetingType) {
        this.greetingType = greetingType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", greetingType=" + greetingType +
                ", createdAt=" + createdAt +
                '}';
    }

    public enum GreetingType {
        CASUAL,
        FORMAL
    }
}
