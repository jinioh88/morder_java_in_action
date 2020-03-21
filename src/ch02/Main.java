package ch02;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Apple apple = new Apple();
        List<Apple> inventory = new ArrayList<>();

        List<Apple> redApples = apple.filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return Color.RED.equals(apple.getColor());
            }
        });
    }
}
