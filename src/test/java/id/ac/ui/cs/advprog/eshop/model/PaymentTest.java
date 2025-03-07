package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import java.util.Arrays;
import static org.junit.Assert.*;

public class PaymentTest {

    private PaymentService paymentService;
    private PaymentRepository paymentRepository;

    @Before
    public void setUp() {
        paymentRepository = new PaymentRepository();
        paymentService = new PaymentService(paymentRepository);
    }

    // Helper method to create an order
    private Order createOrder(String orderId, String status) {
        return new Order(orderId, Arrays.asList(new Product("product1", 100)), System.currentTimeMillis(), "author", status);
    }

    // Testing Payment Model Creation
    @Test
    public void testPaymentModelCreation() {
        Order order = createOrder("1", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("1", "Voucher", "SUCCESS", paymentData, order);

        // Assert that the payment object is created correctly
        assertNotNull(payment);
        assertEquals("1", payment.getId());
        assertEquals("Voucher", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(order, payment.getOrder());
    }

    // Testing Payment by Voucher
    @Test
    public void testPaymentByVoucherValidCode() {
        Order order = createOrder("1", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = paymentService.addPayment(order, "Voucher", paymentData);

        assertNotNull(payment);
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("1", payment.getOrder().getId());  // Ensure payment is linked to the correct order
    }

    @Test
    public void testPaymentByVoucherInvalidCode() {
        Order order = createOrder("2", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALIDCODE123");

        Payment payment = paymentService.addPayment(order, "Voucher", paymentData);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
    }

    // Testing Cash on Delivery
    @Test
    public void testPaymentByCashOnDeliveryValidData() {
        Order order = createOrder("3", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "123 Main St");
        paymentData.put("deliveryFee", "10");

        Payment payment = paymentService.addPayment(order, "Cash on Delivery", paymentData);

        assertNotNull(payment);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    public void testPaymentByCashOnDeliveryMissingAddress() {
        Order order = createOrder("4", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("deliveryFee", "10");

        Payment payment = paymentService.addPayment(order, "Cash on Delivery", paymentData);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    public void testPaymentByCashOnDeliveryMissingFee() {
        Order order = createOrder("5", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "123 Main St");

        Payment payment = paymentService.addPayment(order, "Cash on Delivery", paymentData);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
    }

    // Testing Bank Transfer
    @Test
    public void testPaymentByBankTransferValidData() {
        Order order = createOrder("6", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank A");
        paymentData.put("referenceCode", "123456");

        Payment payment = paymentService.addPayment(order, "Bank Transfer", paymentData);

        assertNotNull(payment);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    public void testPaymentByBankTransferMissingBankName() {
        Order order = createOrder("7", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("referenceCode", "123456");

        Payment payment = paymentService.addPayment(order, "Bank Transfer", paymentData);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    public void testPaymentByBankTransferMissingReferenceCode() {
        Order order = createOrder("8", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank A");

        Payment payment = paymentService.addPayment(order, "Bank Transfer", paymentData);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
    }

    // Testing Payment Status Update
    @Test
    public void testSetPaymentStatusToSuccess() {
        Order order = createOrder("9", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = paymentService.addPayment(order, "Voucher", paymentData);
        paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", payment.getOrder().getStatus());  // Ensure order status is updated to SUCCESS
    }

    @Test
    public void testSetPaymentStatusToRejected() {
        Order order = createOrder("10", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALIDCODE123");

        Payment payment = paymentService.addPayment(order, "Voucher", paymentData);
        paymentService.setStatus(payment, "REJECTED");

        assertEquals("FAILED", payment.getStatus());
        assertEquals("FAILED", payment.getOrder().getStatus());  // Ensure order status is updated to FAILED
    }

    // Testing Edge Case for Payment Data
    @Test
    public void testInvalidVoucherCodeFormat() {
        Order order = createOrder("11", "WAITING_PAYMENT");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOPABCD1234");

        Payment payment = paymentService.addPayment(order, "Voucher", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }
}
