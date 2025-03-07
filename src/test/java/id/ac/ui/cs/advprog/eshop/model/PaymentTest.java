// File: src/test/java/id/ac/ui/cs/advprog/eshop/model/PaymentModelTest.java
package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    private Map<String, String> voucherInfo;
    private Map<String, String> codInfo;
    private Order sampleOrder;

    @BeforeEach
    public void setUp() {
        // Prepare valid voucher info.
        voucherInfo = new HashMap<>();
        voucherInfo.put("voucherCode", "ESHOP1234ABC5678");

        // Prepare valid Cash on Delivery info.
        codInfo = new HashMap<>();
        codInfo.put("address", "123 Main Street");
        codInfo.put("deliveryFee", "5000");

        // Create a sample order with one product.
        List<Product> products = new ArrayList<>();
        Product prod = new Product();
        prod.setProductId("prod-001");
        prod.setProductName("Sample Product");
        prod.setProductQuantity(5);
        products.add(prod);

        sampleOrder = Order.builder()
                .id("order-001")
                .products(products)
                .orderTime(1708560000L)
                .author("Tester")
                .status(OrderStatus.WAITING_PAYMENT.getValue())
                .build();
    }

    @Test
    public void testIdGenerationOnCreation() {
        // Verify that a Payment instance auto-generates a non-null ID.
        Payment payment = new Payment("temp-id", PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.WAITING.getValue(), codInfo, sampleOrder);
        assertNotNull(payment.getPaymentId(), "Payment ID should be auto-generated and not null.");
    }

    @Test
    public void testRejectsUnknownPaymentMethod() {
        // An invalid payment method should throw an exception.
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new Payment("temp-id", "UNKNOWN_METHOD", PaymentStatus.WAITING.getValue(), codInfo, sampleOrder)
        );
        assertTrue(ex.getMessage().contains("Invalid payment method"), "Unknown payment method should be rejected.");
    }

    @Test
    public void testNullOrderNotAccepted() {
        // Payment creation should fail if the order is null.
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new Payment("temp-id", PaymentMethod.CASH_ON_DELIVERY.getValue(), PaymentStatus.WAITING.getValue(), codInfo, null)
        );
        assertTrue(ex.getMessage().contains("Order cannot be null"), "A null order should cause an exception.");
    }

    @Test
    public void testEmptyPaymentDataIsInvalid() {
        // An empty payment data map should not be accepted.
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new Payment("temp-id", PaymentMethod.CASH_ON_DELIVERY.getValue(), PaymentStatus.WAITING.getValue(), new HashMap<>(), sampleOrder)
        );
        assertTrue(ex.getMessage().contains("Payment data cannot be empty"), "Empty payment data must be rejected.");
    }

    @Test
    public void testValidCODMaintainsWaitingStatus() {
        // A valid COD payment should keep its initial WAITING status.
        Payment payment = new Payment("temp-id", PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.WAITING.getValue(), codInfo, sampleOrder);
        assertEquals(PaymentStatus.WAITING.getValue(), payment.getPaymentStatus(),
                "Valid cash on delivery payment should remain in WAITING status.");
    }

    @Test
    public void testInvalidCODDataResultsInRejection() {
        // A COD payment with missing essential info should be marked as REJECTED.
        Map<String, String> incompleteCodInfo = new HashMap<>(codInfo);
        incompleteCodInfo.put("address", ""); // Simulate missing address.
        Payment payment = new Payment("temp-id", PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.WAITING.getValue(), incompleteCodInfo, sampleOrder);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus(),
                "Incomplete COD data should set status to REJECTED.");
    }

    @Test
    public void testVoucherPaymentValidCodeDoesNotAutoReject() {
        // A payment with a valid voucher code should not be auto-rejected.
        Payment payment = new Payment("temp-id", PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.WAITING.getValue(), voucherInfo, sampleOrder);
        assertEquals(PaymentStatus.WAITING.getValue(), payment.getPaymentStatus(),
                "A valid voucher code should leave the status unchanged.");
    }

    @Test
    public void testVoucherPaymentInvalidCodeLeadsToRejection() {
        // Change the voucher code so it becomes invalid.
        Map<String, String> invalidVoucher = new HashMap<>(voucherInfo);
        invalidVoucher.put("voucherCode", "ESHOP1234ABC567X"); // deliberately invalid.
        Payment payment = new Payment("temp-id", PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.WAITING.getValue(), invalidVoucher, sampleOrder);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus(),
                "An invalid voucher code should cause the payment to be rejected.");
    }

    @Test
    public void testStatusUpdateFunctionality() {
        // Verify that updating the payment status works as expected.
        Payment payment = new Payment("temp-id", PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.WAITING.getValue(), codInfo, sampleOrder);
        payment.setPaymentStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getPaymentStatus(),
                "Payment status should update to SUCCESS.");
    }

    @Test
    public void testPaymentDataReferenceIntegrity() {
        // Ensure that modifying the retrieved payment data map affects the internal state.
        Payment payment = new Payment("temp-id", PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.WAITING.getValue(), codInfo, sampleOrder);
        Map<String, String> retrievedData = payment.getPaymentData();
        retrievedData.put("note", "extra info");
        assertEquals("extra info", payment.getPaymentData().get("note"),
                "Changes to the payment data map should be reflected in the payment object.");
    }
}
