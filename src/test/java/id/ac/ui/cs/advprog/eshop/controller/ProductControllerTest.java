package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHome() {
        // When
        String viewName = productController.getHome(model);

        // Then
        assertEquals("index", viewName);
        verifyNoInteractions(productService);
    }

    @Test
    void testCreateProductPage() {
        // When
        String viewName = productController.createProductPage(model);

        // Then
        assertEquals("createProduct", viewName);
        verify(model).addAttribute(eq("product"), any(Product.class));
        verifyNoMoreInteractions(model, productService);
    }

    @Test
    void testCreateProductPost() {
        // Given
        Product product = new Product();

        // When
        String viewName = productController.createProductPost(product, model);

        // Then
        assertEquals("redirect:list", viewName);
        verify(productService).create(product);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void testProductListPage() {
        // Given
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        when(productService.findAll()).thenReturn(products);

        // When
        String viewName = productController.productListPage(model);

        // Then
        assertEquals("productList", viewName);
        verify(productService).findAll();
        verify(model).addAttribute("products", products);
        verifyNoMoreInteractions(productService, model);
    }

    @Test
    void testEditProductPage() {
        // Given
        String productId = "some-id";
        Product product = new Product();
        when(productService.findById(productId)).thenReturn(product);

        // When
        String viewName = productController.editProductPage(productId, model);

        // Then
        assertEquals("editProduct", viewName);
        verify(productService).findById(productId);
        verify(model).addAttribute("product", product);
        verifyNoMoreInteractions(productService, model);
    }

    @Test
    void testEditProductPost() {
        // Given
        Product product = new Product();

        // When
        String viewName = productController.editProductPost(product, model);

        // Then
        assertEquals("redirect:list", viewName);
        verify(productService).update(product);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void testDeleteProduct() {
        // Given
        String productId = "some-id";

        // When
        String viewName = productController.deleteProduct(productId);

        // Then
        assertEquals("productList", viewName);
        verify(productService).delete(productId);
        verifyNoMoreInteractions(productService);
    }
}
