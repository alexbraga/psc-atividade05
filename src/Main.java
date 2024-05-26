import entity.*;
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

public class Main {
    public static void main(String[] args) {
        List<Product> productList = Stock.getProductList();
        Scanner scanner = new Scanner(System.in);

        startUI(scanner, productList);
        scanner.close();
    }

    private static void startUI(Scanner scanner, List<Product> productList) {
        System.out.println("Seja bem-vindo(a)!");
        mainMenu(scanner, productList);
    }

    private static void mainMenu(Scanner scanner, List<Product> productList) {
        System.out.println("Selecione uma opção:" +
                           "\n\t1. Exibir todos os produtos" +
                           "\n\t2. Pesquisar um produto" +
                           "\n\t3. Sair");

        System.out.print("\nDigite o número da opção desejada (1-3): ");

        int option = scanner.nextInt();
        scanner.nextLine();

        selectOption(option, productList, scanner);
    }

    private static void selectOption(int option, List<Product> productList, Scanner scanner) {
        switch (option) {
            case 1:
                for (Product product : productList) {
                    System.out.println();
                    System.out.println(product.toString());
                    System.out.println();
                }

                mainMenu(scanner, productList);
            case 2:
                boolean searchAgain = true;
                searchProduct(scanner, productList, searchAgain);
                break;
            case 3:
                System.out.println("Obrigado pela visita! Volte sempre!");
                System.exit(0);
            default:
                System.out.println("Opção inválida. Tente novamente\n");
                mainMenu(scanner, productList);
        }
    }

    private static void searchProduct(Scanner scanner, List<Product> productList, boolean searchAgain) {
        Order order = new Order(UUID.randomUUID(), null, null);

        do {
            boolean isFound = false;
            System.out.println("\nDigite o nome do produto desejado:");
            String query = scanner.nextLine();

            if (!query.isEmpty()) {
                for (Product product : productList) {
                    String productName = product.getName().toLowerCase();
                    String productDescription = product.getDescription().toLowerCase();
                    String queryToLowercase = query.toLowerCase();

                    if (productName.contains(queryToLowercase) ||
                        productDescription.contains(queryToLowercase)) {
                        System.out.println();
                        System.out.println(product);
                        isFound = true;

                        if (product.getStock() > 0) {
                            System.out.print("Deseja adicionar " + product.getDescription() + " ao carrinho? (S/N): ");
                            String response = scanner.nextLine();

                            if (response.equalsIgnoreCase("S")) {
                                searchAgain = addToCart(scanner, searchAgain, product, order);
                            }
                        }
                    }
                }

                if (!isFound) {
                    System.out.println("Produto não encontrado. Tente novamente");
                }
            } else {
                System.out.println("Produto não encontrado. Tente novamente");
            }

        } while (searchAgain);
    }

    private static boolean addToCart(Scanner scanner, boolean searchAgain, Product product, Order order) {
        while (true) {
            System.out.print("Quantas unidades deseja adicionar? ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            if (quantity > product.getStock() || quantity == 0) {
                System.out.println();
                System.out.println("Não foi possível adicionar " + product.getDescription() + " ao carrinho");
                System.out.println("Quantidade solicitada: " + quantity);
                System.out.println("Quantidade em estoque: " + product.getStock());
                System.out.println("Por favor, selecione uma quantidade entre 0 e " + product.getStock());
            } else {
                OrderItem item = new OrderItem(UUID.randomUUID(), product.getPrice(), quantity, product, order);
                order.getItems().add(item);

                System.out.println("Produto adicionado com sucesso!");
                System.out.println("O que deseja fazer?");
                System.out.println("\t1. Continuar comprando");
                System.out.println("\t2. Finalizar compra");
                System.out.print("\nDigite o número da opção desejada (1-2): ");

                int option = scanner.nextInt();
                scanner.nextLine();

                if (option == 2) {
                    proceedToCheckout(scanner, order);
                    searchAgain = false;
                }

                break;
            }
        }

        return searchAgain;
    }

    private static void proceedToCheckout(Scanner scanner, Order order) {
        System.out.println("Para continuar, crie uma nova conta");
        Customer customer = registerNewCustomer(scanner);
        boolean isLoggedIn = login(scanner, customer);
        order.setCustomer(customer);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDateTime = now.format(formatter);
        order.setMoment(Instant.parse(formattedDateTime));

        if (isLoggedIn) {
            System.out.println("Selecione a forma de pagamento:" +
                               "\n\t1. Crédito" +
                               "\n\t2. Débito" +
                               "\n\t3. Pix");
            System.out.print("\nDigite o número da opção desejada (1-3): ");
            int option = scanner.nextInt();
            scanner.nextLine();

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

            System.out.print("\nDeseja confirmar o pedido? (S/N): ");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("S")) {
                Payment paymentMethod = null;

                switch (option) {
                    case 1:
                        System.out.println("Insira o número do seu cartão:");
                        String cardNumber = scanner.nextLine();
                        paymentMethod = new CreditCard(cardNumber);
                        break;
                    case 2:
                        paymentMethod = new Debit();
                        break;
                    case 3:
                        paymentMethod = new Pix();
                        break;
                    default:
                        break;
                }

                System.out.println(payment(paymentMethod, total));
                System.out.println("Obrigado pela compra! Volte sempre!");
            }
        }
    }

    private static String payment(Payment paymentMethod, double value) {
        return paymentMethod.processPayment(value);
    }

    private static Customer registerNewCustomer(Scanner scanner) {
        System.out.println("\nInsira os solicitados dados a seguir");
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();
        System.out.print("Endereço: ");
        String address = scanner.nextLine();

        return Customer.signUp(UUID.randomUUID(), name, email, password, address);
    }

    private static boolean login(Scanner scanner, Customer customer) {
        System.out.println("\nLogin");
        System.out.println("Insira seu e-mail: ");
        String existingEmail = scanner.nextLine();
        System.out.println("Insira sua senha: ");
        String existingPassword = scanner.nextLine();

        return customer.login(existingEmail, existingPassword);
    }
}