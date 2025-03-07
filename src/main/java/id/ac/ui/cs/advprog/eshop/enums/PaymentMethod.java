package id.ac.ui.cs.advprog.eshop.enums;

public enum PaymentMethod {
    CASH_ON_DELIVERY("CASH_ON_DELIVERY"),
    VOUCHER_CODE("VOUCHER_CODE");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}