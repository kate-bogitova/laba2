

public class Main {
    public static void main(String[] args) {
        String expression = "(15/(7-2*2))^2+10";

        MathParser mathpars = new MathParser(expression);

        System.out.println("Ввод: " + mathpars.infixExpr);
        System.out.println("Постфиксная форма: " + mathpars.postfixExpr);
        System.out.println("Итого: " + mathpars.Calc());
    }
}
