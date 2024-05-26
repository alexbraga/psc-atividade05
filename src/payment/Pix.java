package payment;

public class Pix implements Payment {
    @Override
    public String processPayment(double payment) {
        return "Pix no valor de R$" + payment + " realizado com sucesso";
    }
}
