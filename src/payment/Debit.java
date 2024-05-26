package payment;

public class Debit implements Payment {
    @Override
    public String processPayment(double payment) {
        return "Débito no valor de R$" + payment + " realizado com sucesso";
    }
}
