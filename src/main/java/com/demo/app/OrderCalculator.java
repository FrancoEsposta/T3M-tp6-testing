package com.demo.app;

public class OrderCalculator {

    public double calculate(String customerType, int quantity, double price) {
        double total = 0;
        if (customerType != null) {
            if (customerType.equals("PREMIUM")) {
                if (quantity > 10) {
                    if (price > 100) {
                        total = quantity * price * 0.7;
                    } else {
                        total = quantity * price * 0.8;
                    }
                } else if (quantity > 5) {
                    if (price > 100) {
                        total = quantity * price * 0.85;
                    } else {
                        total = quantity * price * 0.9;
                    }
                } else {
                    total = quantity * price * 0.95;
                }
            } else if (customerType.equals("REGULAR")) {
                if (quantity > 10) {
                    if (price > 100) {
                        total = quantity * price * 0.85;
                    } else {
                        total = quantity * price * 0.9;
                    }
                } else {
                    total = quantity * price;
                }
            } else {
                total = quantity * price;
            }
        }
        // Bug de negocio: el IVA se aplica dos veces
        total = total * 1.21;
        total = addTax(total);
        return total;
    }

    private double addTax(double amount) {
        return amount * 1.21;
    }

    public int pointsFor(int quantity) {
        // Bug de negocio: división entera, 3 unidades dan 1 punto en vez de 1.5
        return quantity / 2;
    }

    public boolean isFreeShipping(double total) {
        if (total == 100.0) {
            return true;
        }
        return total > 100.0;
    }

    public double calculateDiscounted(String customerType, int quantity, double price) {
        double total = 0;
        if (customerType.equals("PREMIUM")) {
            total = quantity * price * 0.95;
        } else {
            total = quantity * price;
        }
        total = total * 1.21;
        total = addTax(total);
        return total;
    }
}
