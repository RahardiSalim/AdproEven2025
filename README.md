# Rahardi Salim | 2306201861 | Advance Programing A

# Tutorial 1 

## Exercise 1

### Clean Code Principles Applied

- **Separation of Concerns:**  
  The application is organized into four primary layers:
    - **Model Layer:** Represents the application's data structure. In this case, the Product class encapsulates product details such as `productId`, `productName`, and `productQuantity`. This layer serves as a blueprint for product entities.
    - **Controller Layer:** Handles HTTP requests and manages interactions with the web client.
    - **Service Layer:** Contains business logic and acts as an intermediary between the Controller and Repository.
    - **Repository Layer:** Manages data access (here, using an in-memory `List` for demonstration).

- **Readability and Maintainability:**
    - **Meaningful Naming:** Classes and methods are named clearly (e.g., `create`, `findAll`, `update`, `delete`), making it easy to understand what each component does.
    - **Lombok Usage:** The use of Lombok annotations (`@Getter` and `@Setter`) in the `Product` model helps reduce boilerplate code, which improves readability.
    - **RESTful Endpoints:** The endpoints (e.g., `/product/create`, `/product/edit/{id}`, `/product/delete/{id}`) follow a consistent and intuitive structure.

- **Modular Design:**  
  Each functionality is encapsulated in its own method, making the code easier to test, maintain, and extend. This modular approach supports better organization and reusability.

### Secure Coding Practices Applied

- **Unique Identifier Generation:**  
  When creating a product, a secure and random `UUID` is generated to assign a unique `productId`. This reduces the likelihood of ID collisions and makes the identifiers unpredictable.

- **Dependency Injection:**  
  Spring’s `@Autowired` annotation is used to inject dependencies, which promotes loose coupling between classes. This practice not only simplifies testing but also improves overall code security and maintainability.

- **Thymeleaf Templating:**  
  Thymeleaf is used for rendering views, and by default, it escapes HTML. This behavior helps protect against Cross-Site Scripting (XSS) attacks by ensuring that user-provided input is not rendered as raw HTML.

### Areas for Improvement

- **Input Validation:**  
  Currently, there is no explicit validation for the product fields. Enhancing the `Product` model with validation annotations (such as `@NotNull`, `@NotBlank`, and `@Size`) from the Bean Validation API (JSR-380) would ensure data integrity and prevent invalid data from being processed.
  ```java
  // Example improvement in Product.java:
  @NotBlank(message = "Product name must not be empty")
  private String productName;
  ```

- **Error Handling:**  
  The code assumes that operations such as finding or updating a product will always succeed. It would be beneficial to add error handling (e.g., checking for `null` returns and throwing custom exceptions) and to provide user-friendly error messages or views when operations fail.

- **Thread Safety:**  
  The current in-memory storage (`ArrayList`) used in `ProductRepository` is not thread-safe. While acceptable for a demo or learning project, a production-ready application should use a thread-safe collection or, preferably, a proper database to handle concurrent access.

- **CSRF Protection:**  
  Although Thymeleaf helps prevent some XSS issues, it is important to ensure that Cross-Site Request Forgery (CSRF) protection is enabled (typically by integrating Spring Security). CSRF tokens should be included in forms to protect against malicious requests.

- **Logging:**  
  Adding a logging framework (e.g., SLF4J with Logback) would improve the ability to trace and debug issues. It is important to ensure that sensitive information is not logged, in line with secure coding practices.