// File: src/main/java/id/ac/ui/cs/advprog/eshop/service/PaymentService.java
package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import java.util.List;

public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService() {
        this.repository = new PaymentRepository();
    }

    /**
     * Creates a Payment, saves it, and returns it.
     * For voucher payments, if the voucher is valid, status is set to SUCCESS;
     * for COD, if data is invalid, status is set to REJECTED.
     */
    public Payment addPayment(Order order, String method, java.util.Map<String, String> paymentData) {
        // Create Payment with initial status WAITING.
        Payment payment = new Payment("ignored", method, PaymentStatus.WAITING.getValue(), paymentData, order);

        // For voucher payment: if the Payment constructor did not mark it REJECTED, set to SUCCESS.
        if (method.equalsIgnoreCase("VOUCHER_CODE")) {
            if (!payment.getPaymentStatus().equals(PaymentStatus.REJECTED.getValue())) {
                payment.setPaymentStatus(PaymentStatus.SUCCESS.getValue());
            }
        }
        // For COD, the Payment model already sets status based on data validity.

        repository.save(payment);
        return payment;
    }

    /**
     * Updates the Payment status and, accordingly, the Order's status.
     */
    public Payment setStatus(Payment payment, String status) {
        payment.setPaymentStatus(status);
        if (status.equals(PaymentStatus.SUCCESS.getValue())) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else if (status.equals(PaymentStatus.REJECTED.getValue())) {
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }
        return payment;
    }

    public Payment getPayment(String paymentId) {
        return repository.getPaymentById(paymentId);
    }

    public List<Payment> getAllPayments() {
        return repository.getAllPayments();
    }
}
