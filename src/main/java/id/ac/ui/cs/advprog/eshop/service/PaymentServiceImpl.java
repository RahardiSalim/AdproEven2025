// File: src/main/java/id/ac/ui/cs/advprog/eshop/service/PaymentServiceImpl.java
package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import java.util.List;
import java.util.Map;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    // For production, consider injecting the repository.
    public PaymentServiceImpl() {
        this.repository = new PaymentRepository();
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        // Create Payment with initial status WAITING (this constructor ignores the passed id).
        Payment payment = new Payment("ignored", method, PaymentStatus.WAITING.getValue(), paymentData, order);
        // For voucher payments, if validation passes, update status to SUCCESS.
        if (isVoucherPayment(method) && isPaymentValid(payment)) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS.getValue());
        }
        // For Cash on Delivery, the Payment model sets status based on data validity.
        repository.save(payment);
        return payment;
    }

    private boolean isVoucherPayment(String method) {
        return method.equalsIgnoreCase("VOUCHER_CODE");
    }

    private boolean isPaymentValid(Payment payment) {
        return !payment.getPaymentStatus().equals(PaymentStatus.REJECTED.getValue());
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setPaymentStatus(status);
        updateOrderStatus(payment, status);
        return payment;
    }

    private void updateOrderStatus(Payment payment, String status) {
        if (PaymentStatus.SUCCESS.getValue().equals(status)) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }
    }

    @Override
    public Payment getPayment(String paymentId) {
        return repository.getPaymentById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return repository.getAllPayments();
    }
}
