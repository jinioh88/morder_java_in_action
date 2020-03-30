package ch09;

import ch09.strategy.Validator;

public class Main {
    public static void main(String[] args) {
        Validator numbericValidator = new Validator((String s) -> s.matches("[a-z]+"));
        boolean b1 = numbericValidator.validate("aaaa");
    }
}
