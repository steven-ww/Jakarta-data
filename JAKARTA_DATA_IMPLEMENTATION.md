# Jakarta Data Implementation

## Overview

This project demonstrates the proper implementation of Jakarta Data with the `@Repository` annotation, replacing traditional JPA repository implementations with a simple interface-based approach.

## What is Jakarta Data?

Jakarta Data is a specification that provides a consistent programming model for data access in Jakarta EE applications. It defines a repository abstraction that allows you to work with data using simple interfaces without implementing boilerplate code.

## Key Features Implemented

### 1. Repository Interface with @Repository Annotation

The `GreetingRepository` interface is now properly implemented as a Jakarta Data repository:

```java
@Repository
public interface GreetingRepository extends CrudRepository<Greeting, Long> {
    // No implementation needed - Jakarta Data provides it automatically
}
```

### 2. Automatic CRUD Operations

By extending `CrudRepository<Greeting, Long>`, the repository automatically provides:
- `save(Greeting)` - Save or update a greeting
- `findById(Long)` - Find greeting by ID
- `findAll()` - Get all greetings (returns Stream)
- `deleteById(Long)` - Delete greeting by ID
- `existsById(Long)` - Check if greeting exists

### 3. Derived Query Methods

Jakarta Data automatically implements query methods based on method names:

```java
// Find greetings by name
List<Greeting> findByName(String name);

// Count greetings by name
long countByName(String name);

// Find greetings containing name (case-insensitive)
List<Greeting> findByNameContainingIgnoreCase(String name);

// Find greetings ordered by creation timestamp
List<Greeting> findAllByOrderByCreatedAtDesc();

// Check if greeting exists by name
boolean existsByName(String name);
```

### 4. Custom Queries with @Query

For complex queries, you can use the `@Query` annotation:

```java
@Query("SELECT g FROM Greeting g WHERE g.name LIKE :namePrefix%")
List<Greeting> findByNamePrefix(String namePrefix);
```

### 5. Delete Operations

Jakarta Data supports delete operations through derived method names:

```java
@Delete
void deleteByName(String name);
```

## Benefits Over Traditional JPA Repository

### Before (Traditional JPA Repository):
- Required a concrete class with `@ApplicationScoped`
- Needed to inject `EntityManager`
- Had to manually write all CRUD operations
- Required transaction management annotations
- Lots of boilerplate code

### After (Jakarta Data Repository):
- Simple interface with `@Repository`
- No `EntityManager` injection needed
- Automatic CRUD operations
- Automatic transaction management
- Minimal boilerplate code

## Implementation Details

### Dependencies
```xml
<dependency>
    <groupId>jakarta.data</groupId>
    <artifactId>jakarta.data-api</artifactId>
    <version>1.0.1</version>
    <scope>provided</scope>
</dependency>
```

### Service Layer Integration
The service layer uses the repository interface just like any other injected dependency:

```java
@ApplicationScoped
public class HelloService {
    @Inject
    private GreetingRepository greetingRepository;
    
    public List<Greeting> getAllGreetings() {
        return greetingRepository.findAll().collect(Collectors.toList());
    }
}
```

Note: `findAll()` returns a `Stream<Greeting>` in Jakarta Data, not a `List`.

## Method Naming Conventions

Jakarta Data follows Spring Data naming conventions:

- `findBy*` - Query methods
- `countBy*` - Count methods
- `deleteBy*` - Delete methods
- `existsBy*` - Existence check methods
- `*OrderBy*` - Ordering methods
- `*IgnoreCase` - Case-insensitive operations
- `*Containing*` - Like operations

## Testing

The test cases have been updated to work with Jakarta Data:

- Mock the repository interface instead of concrete class
- Handle `Stream` return types from `findAll()`
- Test derived query methods
- Verify proper method calls

## Runtime Behavior

At runtime, the Jakarta Data provider (like EclipseLink or Hibernate) will:
1. Scan for `@Repository` interfaces
2. Generate implementations automatically
3. Handle query parsing and execution
4. Manage transactions automatically
5. Provide connection pooling and caching

## Migration Path

If you have existing JPA repositories, you can migrate to Jakarta Data by:
1. Adding Jakarta Data dependency
2. Converting repository classes to interfaces
3. Adding `@Repository` annotation
4. Extending `CrudRepository` or `BasicRepository`
5. Removing manual JPA code
6. Updating service layer to handle Stream return types

This implementation showcases the power and simplicity of Jakarta Data for modern Jakarta EE applications.
