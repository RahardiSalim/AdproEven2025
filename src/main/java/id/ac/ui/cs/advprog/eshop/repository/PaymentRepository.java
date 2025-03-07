// File: src/main/java/id/ac/ui/cs/advprog/eshop/repository/PaymentRepository.java
package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentRepository {

    private final Map<String, Payment> paymentMap = new HashMap<>();

    /**
     * Saves the Payment object.
     * @param payment the Payment to save.
     */
    public void save(Payment payment) {
        paymentMap.put(payment.getPaymentId(), payment);
    }

    /**
     * Retrieves a Payment by its unique ID.
     * @param paymentId the ID of the Payment.
     * @return the Payment, or null if not found.
     */
    public Payment getPaymentById(String paymentId) {
        return paymentMap.get(paymentId);
    }

    /**
     * Returns a list of all stored Payment objects.
     * @return list of Payments.
     */
    public List<Payment> getAllPayments() {
        return new ArrayList<>(paymentMap.values());
    }
}
