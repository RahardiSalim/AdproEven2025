package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

public class Payment {

    private String paymentId;
    private String orderId;
    private String paymentMethod;
    private String paymentStatus;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String orderId, String paymentMethod, String paymentStatus,
                   Map<String, String> paymentData, Order order) {
        // TODO: Implement the constructor logic later.
    }

    public String getPaymentId() {
        return null;
    }

    public String getPaymentMethod() {
        return null;
    }

    public String getPaymentStatus() {
        return null;
    }

    public Map<String, String> getPaymentData() {
        return null;
    }

    public Order getOrder() {
        return null;
    }

    public void setPaymentStatus(String paymentStatus) {
        // TODO: Implement status update logic later.
    }
}
