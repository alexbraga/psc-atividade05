package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Stock {
    private static final List<Product> PRODUCT_LIST;

    private Stock() {
    }

    static {
        PRODUCT_LIST = new ArrayList<>();

        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Aspirador de pó", "Aspirador Electrolux 600W", 255.90, 10));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Liquidificador", "Liquidificador Philips 800W", 149.99, 10));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Ferro de Passar", "Ferro de Passar Black&Decker 2000W", 89.90, 0));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Cafeteira", "Cafeteira Três Corações 110V", 179.50, 10));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Panela Elétrica", "Panela Elétrica Oster 5 Litros", 199.99, 0));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Secador de Cabelo", "Secador Taiff 2000W", 129.99, 0));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Sanduicheira", "Sanduicheira Britânia 750W", 69.90, 10));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Ventilador", "Ventilador Arno Turbo Silencio 40cm", 279.90, 10));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Churrasqueira Elétrica", "Churrasqueira Elétrica Cadence 1800W", 229.99, 10));
        PRODUCT_LIST.add(new Product(UUID.randomUUID(), "Mixer", "Mixer Philips Walita 400W", 119.90, 10));
    }

    public static List<Product> getProductList() {
        return PRODUCT_LIST;
    }
}
