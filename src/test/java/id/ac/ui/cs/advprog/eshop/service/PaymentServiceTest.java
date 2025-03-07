// File: src/test/java/id/ac/ui/cs/advprog/eshop/service/PaymentServiceTest.java
package id.ac.ui.cs.advprog.eshop.service;

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

public class PaymentServiceTest {
    private PaymentService paymentService;
    private Order order;
    private Map<String, String> voucherDataValid;
    private Map<String, String> voucherDataInvalid;
    private Map<String, String> codDataValid;
    private Map<String, String> codDataInvalid;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();

        // Create a sample order with one product.
        List<Product> products = new ArrayList<>();
        Product p = new Product();
        p.setProductId("p1");
        p.setProductName("Product 1");
        p.setProductQuantity(1);
        products.add(p);
        order = Order.builder()
                .id("order-001")
                .products(products)
                .orderTime(System.currentTimeMillis())
                .author("Tester")
                .status(OrderStatus.WAITING_PAYMENT.getValue())
                .build();

        // Prepare valid voucher data.
        voucherDataValid = new HashMap<>();
        voucherDataValid.put("voucherCode", "ESHOP1234ABC5678");

        // Prepare invalid voucher data.
        voucherDataInvalid = new HashMap<>();
        voucherDataInvalid.put("voucherCode", "INVALIDVOUCHERCODE");

        // Prepare valid COD data.
        codDataValid = new HashMap<>();
        codDataValid.put("address", "123 Main St");
        codDataValid.put("deliveryFee", "5000");

        // Prepare invalid COD data (empty address).
        codDataInvalid = new HashMap<>();
        codDataInvalid.put("address", "");
        codDataInvalid.put("deliveryFee", "5000");
    }

    @Test
    public void testAddPaymentVoucherValid() {
        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), voucherDataValid);
        // Expect valid voucher automatically sets status to SUCCESS.
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getPaymentStatus());
    }

    @Test
    public void testAddPaymentVoucherInvalid() {
        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), voucherDataInvalid);
        // Expect invalid voucher causes status to be REJECTED.
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus());
    }

    @Test
    public void testAddPaymentCODValid() {
        Payment payment = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), codDataValid);
        // For valid COD data, status remains as provided (WAITING).
        assertEquals(PaymentStatus.WAITING.getValue(), payment.getPaymentStatus());
    }

    @Test
    public void testAddPaymentCODInvalid() {
        Payment payment = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), codDataInvalid);
        // Invalid COD data (empty address) should cause status REJECTED.
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus());
    }

    @Test
    public void testSetStatusUpdatesOrder() {
        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), voucherDataValid);
        // Update status to REJECTED: Order status should become FAILED.
        paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());

        // Then update status to SUCCESS: Order status should become SUCCESS.
        paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getPaymentStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    public void testGetPaymentAndGetAllPayments() {
        Payment p1 = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), voucherDataValid);
        Payment p2 = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), codDataValid);
        Payment retrieved = paymentService.getPayment(p1.getPaymentId());
        assertNotNull(retrieved);
        assertEquals(p1.getPaymentId(), retrieved.getPaymentId());

        List<Payment> payments = paymentService.getAllPayments();
        assertTrue(payments.contains(p1));
        assertTrue(payments.contains(p2));
    }
}
