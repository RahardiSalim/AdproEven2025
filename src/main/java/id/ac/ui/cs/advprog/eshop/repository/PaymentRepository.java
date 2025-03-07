// File: src/main/java/id/ac/ui/cs/advprog/eshop/repository/PaymentRepository.java
package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentRepository {

    private final List<Payment> payments = new ArrayList<>();

    /**
     * Saves a new Payment to the repository.
     * @param payment the Payment object to save.
     */
    public void save(Payment payment) {
        payments.add(payment);
    }

    /**
     * Retrieves a Payment by its unique ID.
     * @param paymentId the ID of the Payment.
     * @return the matching Payment, or null if not found.
     */
    public Payment getPaymentById(String paymentId) {
        Optional<Payment> result = payments.stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst();
        return result.orElse(null);
    }

    /**
     * Returns a list of all stored Payment objects.
     * @return a new list containing all Payments.
     */
    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }
}
