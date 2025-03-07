package id.ac.ui.cs.advprog.eshop.enums;

public enum PaymentStatus {
    WAITING("WAITING"),
    SUCCESS("SUCCESS"),
    REJECTED("REJECTED");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}