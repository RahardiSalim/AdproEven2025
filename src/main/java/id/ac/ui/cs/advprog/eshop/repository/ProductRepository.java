package id.ac.ui.cs.advprog.eshop.repository;

import java.util.UUID;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    // Create product with validation
    public Product create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getProductQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative.");
        }

        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productData.add(product);
        return product;
    }

    // Find product by ID with validation
    public Product findById(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty.");
        }
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        throw new IllegalArgumentException("Product with ID " + productId + " not found.");
    }

    // Update product with validation
    public Product update(Product updatedProduct) {
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Updated product cannot be null.");
        }
        if (updatedProduct.getProductId() == null || updatedProduct.getProductId().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty.");
        }
        if (updatedProduct.getProductName() == null || updatedProduct.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (updatedProduct.getProductQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative.");
        }

        for (Product product : productData) {
            if (product.getProductId().equals(updatedProduct.getProductId())) {
                product.setProductName(updatedProduct.getProductName());
                product.setProductQuantity(updatedProduct.getProductQuantity());
                return product;
            }
        }
        throw new IllegalArgumentException("Cannot update: Product with ID " + updatedProduct.getProductId() + " not found.");
    }

    // Delete product with validation
    public boolean delete(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty.");
        }

        Iterator<Product> iterator = productData.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId().equals(productId)) {
                iterator.remove();
                return true; // Return true if deleted
            }
        }
        throw new IllegalArgumentException("Cannot delete: Product with ID " + productId + " not found.");
    }

    // Find all products
    public Iterator<Product> findAll() {
        return productData.iterator();
    }
}
