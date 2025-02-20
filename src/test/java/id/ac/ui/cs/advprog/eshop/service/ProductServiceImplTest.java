package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        productService = new ProductServiceImpl();
        // Manually inject the repository (no extra library needed)
        // The field in ProductServiceImpl is private, so we can do:
        // productService.productRepository = productRepository;
        // But that's not possible directly. We'll use reflection:

        try {
            var field = ProductServiceImpl.class.getDeclaredField("productRepository");
            field.setAccessible(true);
            field.set(productService, productRepository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up test due to reflection error: " + e.getMessage());
        }
    }

    @Test
    void testCreate() {
        Product product = new Product();
        product.setProductName("Service Product");
        product.setProductQuantity(10);

        Product created = productService.create(product);
        assertNotNull(created.getProductId(), "Product ID should be generated");
        assertEquals("Service Product", created.getProductName());
        assertEquals(10, created.getProductQuantity());
    }

    @Test
    void testFindAll_Empty() {
        List<Product> all = productService.findAll();
        assertTrue(all.isEmpty(), "Initially, repository should be empty");
    }

    @Test
    void testFindAll_NonEmpty() {
        Product product1 = new Product();
        product1.setProductName("Product 1");
        product1.setProductQuantity(5);
        productService.create(product1);

        Product product2 = new Product();
        product2.setProductName("Product 2");
        product2.setProductQuantity(15);
        productService.create(product2);

        List<Product> all = productService.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testFindById_Success() {
        Product product = new Product();
        product.setProductName("FindMe");
        product.setProductQuantity(20);
        productService.create(product);

        // Grab the generated ID
        String id = product.getProductId();
        Product found = productService.findById(id);
        assertNotNull(found);
        assertEquals("FindMe", found.getProductName());
        assertEquals(20, found.getProductQuantity());
    }

    @Test
    void testFindById_Failure() {
        // This should throw an IllegalArgumentException
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            productService.findById("invalid-id");
        });
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void testUpdate_Success() {
        Product product = new Product();
        product.setProductName("Before Update");
        product.setProductQuantity(1);
        productService.create(product);

        product.setProductName("After Update");
        product.setProductQuantity(99);

        Product updated = productService.update(product);
        assertNotNull(updated);
        assertEquals("After Update", updated.getProductName());
        assertEquals(99, updated.getProductQuantity());
    }

    @Test
    void testUpdate_Failure() {
        Product product = new Product();
        product.setProductId("invalid-id");
        product.setProductName("No exist");
        product.setProductQuantity(10);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            productService.update(product);
        });
        assertTrue(ex.getMessage().contains("Cannot update"));
    }

    @Test
    void testDelete_Success() {
        Product product = new Product();
        product.setProductName("To be deleted");
        product.setProductQuantity(5);
        productService.create(product);

        String id = product.getProductId();
        productService.delete(id);

        // Now findById should fail
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            productService.findById(id);
        });
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void testDelete_Failure() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            productService.delete("non-existent-id");
        });
        assertTrue(ex.getMessage().contains("Cannot delete"));
    }
}
