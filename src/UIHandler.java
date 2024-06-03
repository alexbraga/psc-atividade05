import entity.Customer;
import entity.Order;
import entity.OrderItem;
import entity.Product;
import payment.CreditCard;
import payment.Debit;
import payment.Payment;
import payment.Pix;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class UIHandler {
    private final Scanner SCANNER;
    private final List<Product> PRODUCT_LIST;

    public UIHandler(Scanner scanner, List<Product> productList) {
        this.SCANNER = scanner;
        this.PRODUCT_LIST = productList;
    }

    public void startUI() {
        System.out.println("Seja bem-vindo(a)!");
        mainMenu();
    }

    private void mainMenu() {
        System.out.println("Selecione uma opção:");
        System.out.println("\t1. Exibir todos os produtos");
        System.out.println("\t2. Pesquisar um produto");
        System.out.println("\t3. Sair");
        System.out.print("\nDigite o número da opção desejada (1-3): ");

        int option = SCANNER.nextInt();
        SCANNER.nextLine();

        selectOption(option, PRODUCT_LIST);
    }

    private void selectOption(int option, List<Product> productList) {
        switch (option) {
            case 1:
                for (Product product : productList) {
                    System.out.println();
                    System.out.println(product.toString());
                }

                mainMenu();
            case 2:
                searchProduct();
                break;
            case 3:
                System.out.println("Obrigado pela visita! Volte sempre!");
                System.exit(0);
            default:
                System.out.println("Opção inválida. Tente novamente\n");
                mainMenu();
        }
    }

    private void searchProduct() {
        Order order = new Order(UUID.randomUUID(), null, null);
        boolean searchAgain;

        do {
            searchAgain = false;
            String query = getQuery();
            Product product = findProduct(query);

            if (product != null) {
                displayProduct(product);
                searchAgain = handleProductSelection(product, order);
            } else {
                System.out.println("Produto não encontrado. Tente novamente");
            }

        } while (searchAgain);
    }

    private String getQuery() {
        System.out.println("\nDigite o nome do produto desejado:");
        return SCANNER.nextLine();
    }

    private Product findProduct(String query) {
        for (Product product : PRODUCT_LIST) {
            String productName = product.getName().toLowerCase();
            String productDescription = product.getDescription().toLowerCase();
            String queryToLowercase = query.toLowerCase();

            if (productName.contains(queryToLowercase) || productDescription.contains(queryToLowercase)) {
                return product;
            }
        }

        return null;
    }

    private void displayProduct(Product product) {
        System.out.println();
        System.out.println(product);
    }

    private boolean handleProductSelection(Product product, Order order) {
        if (product.getStock() > 0) {
            System.out.print("\nDeseja adicionar " + product.getDescription() + " ao carrinho? (S/N): ");
            String response = SCANNER.nextLine();

            if (response.equalsIgnoreCase("S")) {
                return addToCart(product, order);
            }
        }

        return true;
    }

    private boolean addToCart(Product product, Order order) {
        boolean searchAgain;

        while (true) {
            int quantity = getQuantity();

            if (quantity > product.getStock() || quantity == 0) {
                displayStockError(product, quantity);
            } else {
                addProductToOrder(product, order, quantity);
                searchAgain = handleCheckoutOption(order);
                break;
            }
        }

        return searchAgain;
    }

    private int getQuantity() {
        System.out.print("Quantas unidades deseja adicionar? ");
        int quantity = SCANNER.nextInt();
        SCANNER.nextLine();

        return quantity;
    }

    private void displayStockError(Product product, int quantity) {
        System.out.println();
        System.out.println("Não foi possível adicionar " + product.getDescription() + " ao carrinho");
        System.out.println("Quantidade solicitada: " + quantity);
        System.out.println("Quantidade em estoque: " + product.getStock());
        System.out.println("Por favor, selecione uma quantidade entre 1 e " + product.getStock());
    }

    private void addProductToOrder(Product product, Order order, int quantity) {
        OrderItem item = new OrderItem(UUID.randomUUID(), product.getPrice(), quantity, product, order);
        order.getItems().add(item);
        System.out.println("Produto adicionado com sucesso!");
    }

    private boolean handleCheckoutOption(Order order) {
        System.out.println("\nO que deseja fazer?");
        System.out.println("\t1. Continuar comprando");
        System.out.println("\t2. Finalizar compra");
        System.out.print("\nDigite o número da opção desejada (1-2): ");

        int option = SCANNER.nextInt();
        SCANNER.nextLine();

        if (option == 2) {
            proceedToCheckout(order);
            return false;
        }

        return true;
    }

    private void proceedToCheckout(Order order) {
        System.out.println("Para continuar, crie uma nova conta");
        Customer customer = registerNewCustomer();
        boolean isLoggedIn = login(customer);
        order.setCustomer(customer);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDateTime = now.format(formatter);
        order.setMoment(Instant.parse(formattedDateTime));

        if (isLoggedIn) {
            displayOrderSummary(order);
            Payment paymentMethod = getPaymentMethod();
            confirmOrder(order, paymentMethod);
        }
    }

    private void displayOrderSummary(Order order) {
        System.out.println();
        System.out.println("RESUMO DO PEDIDO " + order.getId());
        System.out.println(order.getMoment());
        System.out.println();

        for (OrderItem item : order.getItems()) {
            String format = "%-55s%-30s%-20s%-20s%n";
            String description = "Produto: " + item.getProduct().getDescription();
            String price = "Preço unitário: R$" + item.getPrice();
            String quantity = "Quantidade: " + item.getQuantity();
            String subTotal = "Subtotal: R$" + item.calculateSubTotal();

            System.out.printf(format, description, price, quantity, subTotal);
        }

        double total = order.calculateTotal();
        System.out.println("TOTAL: R$" + total);

        System.out.println("\nENDEREÇO DE ENTREGA");
        System.out.println(order.getCustomer().getName());
        System.out.println(order.getCustomer().getAddress());
    }

    private Payment getPaymentMethod() {
        System.out.println("\nSelecione a forma de pagamento:");
        System.out.println("\t1. Crédito");
        System.out.println("\t2. Débito");
        System.out.println("\t3. Pix");
        System.out.print("\nDigite o número da opção desejada (1-3): ");

        int option = SCANNER.nextInt();
        SCANNER.nextLine();

        switch (option) {
            case 1:
                System.out.println("Insira o número do seu cartão:");
                String cardNumber = SCANNER.nextLine();
                return new CreditCard(cardNumber);
            case 2:
                return new Debit();
            case 3:
                return new Pix();
            default:
                System.out.println("Opção inválida. Tente novamente.");
                return getPaymentMethod();
        }
    }

    private void confirmOrder(Order order, Payment paymentMethod) {
        System.out.print("\nDeseja confirmar o pedido? (S/N): ");
        String response = SCANNER.nextLine();

        if (response.equalsIgnoreCase("S")) {
            double total = order.calculateTotal();
            System.out.println(payment(paymentMethod, total));
            System.out.println("Obrigado pela compra! Volte sempre!");
        } else {
            System.out.println("Pedido cancelado.");
        }
    }

    private String payment(Payment paymentMethod, double value) {
        return paymentMethod.processPayment(value);
    }

    private Customer registerNewCustomer() {
        System.out.println("\nInsira os solicitados dados a seguir");
        System.out.print("Nome: ");
        String name = SCANNER.nextLine();
        System.out.print("E-mail: ");
        String email = SCANNER.nextLine();
        System.out.print("Senha: ");
        String password = SCANNER.nextLine();
        System.out.print("Endereço: ");
        String address = SCANNER.nextLine();

        return Customer.signUp(UUID.randomUUID(), name, email, password, address);
    }

    private boolean login(Customer customer) {
        System.out.println("\nLogin");
        System.out.println("Insira seu e-mail: ");
        String existingEmail = SCANNER.nextLine();
        System.out.println("Insira sua senha: ");
        String existingPassword = SCANNER.nextLine();

        return customer.login(existingEmail, existingPassword);
    }
}
