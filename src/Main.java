import entity.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Product> productList = Stock.getProductList();
        Scanner scanner = new Scanner(System.in);

        UIHandler ui = new UIHandler(scanner, productList);
        ui.startUI();
        scanner.close();
    }
}