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

## Exercise 2

After writing the unit tests, I feel more confident in the robustness of our codebase. However, I also recognize that unit tests are just one part of a quality assurance strategy. Here are some of my thoughts and insights:

### Unit Testing and Code Coverage
- **Quantity of Unit Tests:**  
  There is no fixed number of unit tests per class. Instead, tests should cover every meaningful scenario: normal behavior, edge cases, and error conditions. Each public method or significant piece of logic should have tests verifying its behavior. This may mean one or several tests per method depending on complexity.

- **Ensuring Sufficient Tests:**  
  Code coverage tools (like JaCoCo) help us understand which parts of our source code are exercised by our tests. However, achieving 100% code coverage does not guarantee that the code is bug-free; it only indicates that every line is executed during testing. Quality tests should also assert that the correct outcomes occur under various conditions.

### Reflections on Creating Additional Functional Test Suites
- **Test Code Duplication:**  
  Suppose we create another functional test suite to verify the number of items in the product list using a similar setup and instance variables as in `CreateProductFunctionalTest.java`. This approach might lead to duplicate code. Repeating setup procedures and instance variables across multiple test classes violates the DRY (Don't Repeat Yourself) principle and can reduce code quality.

- **Potential Clean Code Issues:**
  - **Duplication:** Repeating the same setup code in multiple test classes can make maintenance harder. If a change is needed, it has to be replicated everywhere.
  - **Poor Abstraction:** Common functionalities, such as initializing the base URL and the WebDriver, should be abstracted into a shared base class or utility method.
  - **Readability:** Test suites should be concise and focused on what they verify. Boilerplate code that is repeated across tests distracts from the actual test logic.

- **Possible Improvements:**
  - **Refactoring Common Setup:** Move shared setup procedures (e.g., initializing the base URL, WebDriver configuration, etc.) into a common base test class. This reduces redundancy and makes tests easier to manage.
  - **Parameterized Tests:** For scenarios where the same test logic applies to multiple input values (e.g., verifying different numbers of items), parameterized tests can help reduce duplication.
  - **Clear Naming Conventions:** Ensure test methods and classes are named clearly to indicate what behavior they verify.

# Tutorial 2 

## Exercise 2

### Code Quality Issues Fixed
During this exercise, I addressed several code quality issues detected by Scorecard, PMD, and other tools. For instance:
- **Token-Permissions and Pinned-Dependencies:** I updated my GitHub Actions workflows to use strict token permissions and pinned dependencies to specific commits, ensuring a more secure and predictable build process.
- **Unused Imports and Unnecessary Modifiers:** I removed unused imports (e.g., the wildcard import in the controller) and unnecessary 'public' modifiers in interface methods, which improved the readability and cleanliness of the code.
- **Visibility of Utility Classes:** I made adjustments to utility classes so that constructors are non-private (when needed for testing), while still keeping the code design sound. Additionally, I resolved issues flagged around dependency update tools and security-policy by ensuring the appropriate license and security documents are in place.

### CI/CD Pipeline Reflection
I believe that the current implementation meets the core definitions of Continuous Integration and Continuous Deployment. My workflows automatically run a full suite of tests and perform static code analysis for every push to the default branch, which ensures that any code changes are integrated and validated as soon as possible. The deployment process is automated, meaning that successful builds trigger a redeployment of the application to a PaaS environment without manual intervention, which greatly reduces the risk of human error. Additionally, the inclusion of security scans and dependency checks in the pipeline reinforces the overall quality and stability of the codebase. The process is designed to catch issues early in the development cycle, and the clear feedback provided by the CI/CD pipeline helps maintain a high standard of code quality. Moreover, the integration of best practices like token permission restrictions and pinned dependencies adds an extra layer of security to the deployment process. Overall, this robust CI/CD setup not only accelerates the delivery cycle but also enhances the reliability and maintainability of the application.