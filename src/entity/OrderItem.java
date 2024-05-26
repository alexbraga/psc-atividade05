package entity;

import java.util.UUID;

public class OrderItem {
    private UUID id;
    private double price;
    private int quantity;
    private Product product;
    private Order order;

    public OrderItem(UUID id, double price, int quantity, Product product, Order order) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.product = product;
        this.order = order;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double calculateSubTotal() {
        return price * quantity;
    }
}
