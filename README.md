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

## Link Website : yeasty-misha-rahardisalim-1e64a615.koyeb.app/

## Exercise 2

### Code Quality Issues Fixed (22/28 solve)
During this exercise, I addressed several code quality issues detected by Scorecard, PMD, and other tools. For instance:
- **Token-Permissions and Pinned-Dependencies:** I updated my GitHub Actions workflows to use strict token permissions and pinned dependencies to specific commits, ensuring a more secure and predictable build process.
- **Unused Imports and Unnecessary Modifiers:** I removed unused imports (e.g., the wildcard import in the controller) and unnecessary 'public' modifiers in interface methods, which improved the readability and cleanliness of the code.
- **Visibility of Utility Classes:** I made adjustments to utility classes so that constructors are non-private (when needed for testing), while still keeping the code design sound. Additionally, I resolved issues flagged around dependency update tools and security-policy by ensuring the appropriate license and security documents are in place.

### CI/CD Pipeline Reflection
I believe that the current implementation meets the core definitions of Continuous Integration and Continuous Deployment. My workflows automatically run a full suite of tests and perform static code analysis for every push to the default branch, which ensures that any code changes are integrated and validated as soon as possible. The deployment process is automated, meaning that successful builds trigger a redeployment of the application to a PaaS environment without manual intervention, which greatly reduces the risk of human error. Additionally, the inclusion of security scans and dependency checks in the pipeline reinforces the overall quality and stability of the codebase. The process is designed to catch issues early in the development cycle, and the clear feedback provided by the CI/CD pipeline helps maintain a high standard of code quality. Moreover, the integration of best practices like token permission restrictions and pinned dependencies adds an extra layer of security to the deployment process. Overall, this robust CI/CD setup not only accelerates the delivery cycle but also enhances the reliability and maintainability of the application.

# Tutorial 3

## Exercise

### Code Changes
To better adhere to SOLID principles, the following changes have been implemented in the Car-related components of the application:

1. **Single Responsibility Principle (SRP):**
  - **Before:** The `CarController` extended `ProductController`, causing a mix of responsibilities between two distinct domains (Products and Cars).
  - **After:** `CarController` is now independent, handling only operations related to the `Car` entity. This ensures that each controller has only one reason to change.

2. **Open/Closed Principle (OCP):**
  - By using service interfaces (e.g., `CarService`), the code is open for extension (new functionalities can be added by implementing these interfaces) while remaining closed for modification (existing classes remain unchanged).
  - New features or entities can be added by creating new implementations without altering the core logic.

3. **Liskov Substitution Principle (LSP):**
  - With the removal of inheritance between `CarController` and `ProductController`, controllers can be substituted or extended without affecting the overall behavior of the application.
  - Each controller now independently fulfills its contract.

4. **Interface Segregation Principle (ISP):**
  - The interfaces `CarService` and `ProductService` are designed to include only the methods relevant to their domain, so clients only depend on what they need.

5. **Dependency Inversion Principle (DIP):**
  - `CarController` now depends on the `CarService` interface rather than a concrete implementation like `CarServiceImpl`.
  - This decouples high-level modules (controllers) from low-level modules (service implementations), making the system easier to test and extend.

## Reflection

### 1. SOLID Principles Applied
I have implemented the following SOLID principles in this project:
- **Single Responsibility Principle (SRP):** Each class now has only one responsibility. For example, the `CarController` handles only Car-related HTTP requests, without mixing responsibilities from product-related functionality.
- **Open/Closed Principle (OCP):** The system is designed to be open for extension but closed for modification. By using service interfaces such as `CarService`, additional functionalities can be added by creating new implementations without altering the existing code.
- **Liskov Substitution Principle (LSP):** By removing inheritance between `CarController` and `ProductController`, each controller operates independently. This ensures that replacing or extending controllers does not alter the expected behavior of the application.
- **Interface Segregation Principle (ISP):** The service interfaces (`CarService` and `ProductService`) contain only the methods relevant to their respective domains, ensuring that clients only need to know the functionality they require.
- **Dependency Inversion Principle (DIP):** High-level modules (controllers) now depend on abstractions (service interfaces) rather than on concrete implementations. For example, `CarController` depends on `CarService` instead of `CarServiceImpl`.

### 2. Advantages of Applying SOLID Principles
- **SRP:**
  - *Advantage:* Simplifies maintenance and testing by ensuring each class has a clear, single responsibility. For instance, if the business logic for Car management changes, only the `CarController` and `CarService` need to be updated without affecting other parts of the system.
- **OCP:**
  - *Advantage:* Allows the codebase to be easily extended. If a new type of vehicle needs to be supported, a new service and controller can be added without modifying the existing, stable code. This minimizes regression risks.
- **LSP:**
  - *Advantage:* Guarantees that subclasses can replace their parent classes without breaking the application. With independent controllers, changes or extensions in one module do not impact the functionality of others, leading to a more robust system.
- **ISP:**
  - *Advantage:* Reduces unnecessary dependencies. Clients only interact with methods that are relevant to them, which improves code readability and maintainability. This leads to a cleaner design and easier future modifications.
- **DIP:**
  - *Advantage:* Enhances flexibility and testability by decoupling high-level modules from low-level implementations. By programming against interfaces, the application can easily switch to a different implementation (for example, for unit testing with mocks) without altering the overall design.

### 3. Disadvantages of Not Applying SOLID Principles
- **Without SRP:**
  - *Disadvantage:* Classes that handle multiple responsibilities become difficult to maintain and test. For example, if `CarController` also managed product functionality, a change in product logic could inadvertently break Car operations.
- **Without OCP:**
  - *Disadvantage:* Every change or new feature would require modification of existing classes, leading to a fragile codebase prone to bugs and regressions. The code would be less modular, making future extensions challenging.
- **Without LSP:**
  - *Disadvantage:* Subclasses might not work as expected when substituted for their parent classes. This could result in inconsistent behavior and bugs that are hard to trace, reducing the reliability of the application.
- **Without ISP:**
  - *Disadvantage:* Clients would be forced to depend on large interfaces containing irrelevant methods, resulting in unnecessary coupling and complexity. This not only makes the code harder to understand but also increases the risk of unintended side effects when changes occur.
- **Without DIP:**
  - *Disadvantage:* High-level modules would be tightly coupled to low-level modules, making the system inflexible and difficult to test. Changes in low-level implementations would ripple through the entire system, leading to a less maintainable and more error-prone codebase.


# Tutorial 4

## Reflection

### 1. Reflection on TDD Flow (Based on Percival, 2017)

I followed a Test-Driven Development (TDD) workflow in this exercise:

- Red: I started by writing failing tests that expressed the desired functionality or edge cases (e.g., empty products, invalid status).
- Green: I then wrote just enough implementation code to make these tests pass.
- Refactor: After the tests passed, I refactored both the production code and test code where necessary for clarity and maintainability.

Was the TDD flow useful enough?

Yes,t he TDD cycle helped me think through the requirements and keep my focus on small, incremental steps. I caught potential edge cases (like an empty product list or invalid status) earlier than I might have otherwise.

What could be improved? 

Next time, I plan to:
- Write more tests around boundary conditions (e.g., product quantity being zero or negative).
- Ensure every piece of refactoring is accompanied by updated or additional tests if needed.
- Use a continuous integration (CI) setup to run tests automatically for better feedback loops.

### 2. Reflection on F.I.R.S.T. Principles

The F.I.R.S.T. principles for unit tests are:

- Fast: Tests should run quickly.
- Independent (or Isolated): Tests should not depend on each other’s state.
- Repeatable: Tests should yield the same results every time, no matter the environment.
- Self-validating: Tests should provide a clear pass/fail outcome.
- Timely: Tests should be written at the right time (ideally, during or before development).

Have the tests followed these principles?

- Fast: The tests currently run quickly because they operate on small sets of data in memory.
- Independent: Each test sets up its own data (or clears it) so that the tests do not interfere with one another.
- Repeatable: Because the tests do not depend on external systems (like a database or network calls) and rely on in-memory lists, they are repeatable in any environment.
- Self-validating: Each test uses assertEquals or assertThrows, providing a clear pass/fail result without manual inspection.
- Timely: We wrote these tests as we developed the code (TDD), so they guided the implementation.

What could be improved?

- I can add more explicit test cases for corner scenarios (e.g., negative quantities, extremely large numbers) to strengthen the reliability of these tests.
- As the project grows, I might incorporate mocking frameworks for more complex dependencies, keeping tests both fast and isolated.