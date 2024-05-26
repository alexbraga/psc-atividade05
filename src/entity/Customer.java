package entity;

import java.util.UUID;

public class Customer {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String address;

    public Customer(UUID id, String name, String email, String password, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Customer signUp(UUID id, String name, String email, String password, String address) {
        Customer newCustomer = new Customer(id, name, email, password, address);
        System.out.println("Novo usuário cadastrado com sucesso!");
        return newCustomer;
    }

    public boolean login(String email, String password) {
        if (email.equals(this.email) && password.equals(this.password)) {
            System.out.println("\nOlá, " + name + "! :)");
            return true;
        } else {
            System.out.println("E-mail ou senha incorretos. Tente novamente");
            return false;
        }
    }
}
