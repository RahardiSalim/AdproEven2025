// File: src/main/java/id/ac/ui/cs/advprog/eshop/model/Payment.java
package id.ac.ui.cs.advprog.eshop.model;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

import java.util.Map;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class Payment {

    private final String paymentId;
    private final String paymentMethod;
    private String paymentStatus;
    private final Map<String, String> paymentData;
    private final Order order;

    public Payment(String id, String paymentMethod, String paymentStatus, Map<String, String> paymentData, Order order) {
        // Validate order
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        // Validate payment data
        if (paymentData == null || paymentData.isEmpty()) {
            throw new IllegalArgumentException("Payment data cannot be empty");
        }
        // Validate payment method
        if (!paymentMethod.equals(PaymentMethod.CASH_ON_DELIVERY.getValue()) &&
                !paymentMethod.equals(PaymentMethod.VOUCHER_CODE.getValue())) {
            throw new IllegalArgumentException("Invalid payment method");
        }

        // Always generate a new paymentId regardless of the provided id.
        this.paymentId = UUID.randomUUID().toString();
        this.paymentMethod = paymentMethod;
        this.order = order;
        this.paymentData = paymentData;

        // Process based on payment method:
        if (paymentMethod.equals(PaymentMethod.CASH_ON_DELIVERY.getValue())) {
            // For Cash on Delivery, require "address" and "deliveryFee" to be non-empty.
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");
            if (address == null || address.trim().isEmpty() ||
                    deliveryFee == null || deliveryFee.trim().isEmpty()) {
                this.paymentStatus = PaymentStatus.REJECTED.getValue();
            } else {
                this.paymentStatus = paymentStatus;
            }
        } else if (paymentMethod.equals(PaymentMethod.VOUCHER_CODE.getValue())) {
            // For Voucher Code, validate the code. If invalid, mark as REJECTED.
            String voucherCode = paymentData.get("voucherCode");
            if (!isValidVoucherCode(voucherCode)) {
                this.paymentStatus = PaymentStatus.REJECTED.getValue();
            } else {
                this.paymentStatus = paymentStatus;
            }
        } else {
            // Should not reach here due to earlier validation.
            this.paymentStatus = paymentStatus;
        }
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String status) {
        this.paymentStatus = status;
    }

    public Map<String, String> getPaymentData() {
        return paymentData;
    }

    public Order getOrder() {
        return order;
    }

    // Helper method to validate voucher code format.
    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null || voucherCode.length() != 16) {
            return false;
        }
        if (!voucherCode.startsWith("ESHOP")) {
            return false;
        }
        int digitCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }
        return digitCount == 8;
    }
}
