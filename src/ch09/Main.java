package ch09;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Validator validator1 = new Validator(new IsNumeric());
        boolean b1 = validator1.validate("aaaa");

        Validator validator2 = new Validator(new IsAllLowerCase());
        boolean b2 = validator2.validate("bbbb");
    }
}
