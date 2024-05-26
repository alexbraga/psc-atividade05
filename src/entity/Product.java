package entity;

import java.util.UUID;

public class Product {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private int stock;

    public Product(UUID id, String name, String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Produto:\t" + name +
               "\nDescrição:\t" + description +
               "\nPreço:\t\tR$" + price +
               "\n" + ((stock > 0) ? "Estoque:\t" + stock + " unid" : "Produto indisponível no momento");
    }
}
