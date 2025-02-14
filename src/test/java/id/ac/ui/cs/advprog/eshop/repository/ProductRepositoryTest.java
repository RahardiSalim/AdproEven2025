package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository(); // Ensure fresh repository for each test
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product savedProduct = productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertNotNull(savedProduct);
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testCreateProductWithNull_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productRepository.create(null);
        });
        assertEquals("Product cannot be null.", exception.getMessage());
    }

    @Test
    void testCreateProductWithEmptyName_ShouldThrowException() {
        Product product = new Product();
        product.setProductId("some-id");
        product.setProductName("");
        product.setProductQuantity(10);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productRepository.create(product);
        });
        assertEquals("Product name cannot be empty.", exception.getMessage());
    }

    @Test
    void testCreateProductWithNegativeQuantity_ShouldThrowException() {
        Product product = new Product();
        product.setProductId("some-id");
        product.setProductName("Invalid Product");
        product.setProductQuantity(-10);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productRepository.create(product);
        });
        assertEquals("Product quantity cannot be negative.", exception.getMessage());
    }

    @Test
    void testFindById_ProductNotFound_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productRepository.findById("non-existent-id");
        });
        assertEquals("Product with ID non-existent-id not found.", exception.getMessage());
    }

    @Test
    void testUpdateProduct_Success() {
        Product product = new Product();
        product.setProductId("valid-id");
        product.setProductName("Old Product");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("valid-id");
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductQuantity(150);

        Product result = productRepository.update(updatedProduct);
        assertNotNull(result);
        assertEquals("Updated Product", result.getProductName());
        assertEquals(150, result.getProductQuantity());
    }

    @Test
    void testUpdateProductWithNull_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productRepository.update(null);
        });
        assertEquals("Updated product cannot be null.", exception.getMessage());
    }

    @Test
    void testUpdateNonExistentProduct_ShouldThrowException() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("non-existent-id");
        updatedProduct.setProductName("Non-existent Product");
        updatedProduct.setProductQuantity(50);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productRepository.update(updatedProduct);
        });
        assertEquals("Cannot update: Product with ID non-existent-id not found.", exception.getMessage());
    }

    @Test
    void testDeleteProduct_Success() {
        Product product = new Product();
        product.setProductId("deletable-id");
        product.setProductName("Product to Delete");
        product.setProductQuantity(10);
        productRepository.create(product);

        boolean deleted = productRepository.delete("deletable-id");
        assertTrue(deleted);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productRepository.findById("deletable-id");
        });
        assertEquals("Product with ID deletable-id not found.", exception.getMessage());
    }

    @Test
    void testDeleteNonExistentProduct_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productRepository.delete("non-existent-id");
        });
        assertEquals("Cannot delete: Product with ID non-existent-id not found.", exception.getMessage());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext(), "Product list should be empty");
    }

    @Test
    void testFindAllWithMultipleProducts() {
        Product product1 = new Product();
        product1.setProductId("id-1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("id-2");
        product2.setProductName("Product 2");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        assertEquals("id-1", productIterator.next().getProductId());
        assertEquals("id-2", productIterator.next().getProductId());
        assertFalse(productIterator.hasNext());
    }
}
