package payment;

public class CreditCard implements Payment{
    private final String cardNumber;

    public CreditCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String processPayment(double payment) {
        return "Pagamento autorizado no cartão " + cardNumber + " no valor de R$" + payment;
    }
}
