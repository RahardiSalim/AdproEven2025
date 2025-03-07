// File: src/test/java/id/ac/ui/cs/advprog/eshop/repository/PaymentRepositoryTest.java
package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {

    private PaymentRepository repository;
    private Order sampleOrder;
    private Map<String, String> codData;
    private Map<String, String> voucherData;
    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        // Initialize repository.
        repository = new PaymentRepository();

        // Create a sample order with one product.
        List<Product> products = new ArrayList<>();
        Product prod = new Product();
        prod.setProductId("prod-001");
        prod.setProductName("Product A");
        prod.setProductQuantity(1);
        products.add(prod);

        sampleOrder = Order.builder()
                .id("order-001")
                .products(products)
                .orderTime(1708560000L)
                .author("Tester")
                .status(OrderStatus.WAITING_PAYMENT.getValue())
                .build();

        // Prepare valid data for Cash on Delivery and Voucher Code.
        codData = new HashMap<>();
        codData.put("address", "123 Main Street");
        codData.put("deliveryFee", "5000");

        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        // Create two Payment objects using the 5-argument constructor.
        payment1 = new Payment("dummy-id", PaymentMethod.CASH_ON_DELIVERY.getValue(), PaymentStatus.WAITING.getValue(), codData, sampleOrder);
        payment2 = new Payment("dummy-id", PaymentMethod.VOUCHER_CODE.getValue(), PaymentStatus.WAITING.getValue(), voucherData, sampleOrder);
    }

    @Test
    public void testSaveAndRetrievePayment() {
        repository.save(payment1);
        Payment retrieved = repository.getPaymentById(payment1.getPaymentId());
        assertNotNull(retrieved, "Retrieved payment should not be null.");
        assertEquals(payment1.getPaymentId(), retrieved.getPaymentId(), "Payment IDs should match.");
        assertEquals(payment1.getPaymentMethod(), retrieved.getPaymentMethod(), "Payment methods should match.");
    }

    @Test
    public void testGetAllPayments() {
        repository.save(payment1);
        repository.save(payment2);
        List<Payment> allPayments = repository.getAllPayments();
        assertEquals(2, allPayments.size(), "There should be two saved payments.");
        assertTrue(allPayments.contains(payment1), "List should contain the first payment.");
        assertTrue(allPayments.contains(payment2), "List should contain the second payment.");
    }

    @Test
    public void testGetPaymentByIdNotFound() {
        Payment retrieved = repository.getPaymentById("non-existent-id");
        assertNull(retrieved, "Retrieving a non-existent payment should return null.");
    }
}
