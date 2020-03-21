package ch02;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Apple apple = new Apple();
        List<Apple> inventory = new ArrayList<>();

        List<Apple> redApples = apple.filterApples(inventory, (Apple a) -> Color.RED.equals(a.getColor()));
        inventory.sort((Apple a1, Apple a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())));
    }
}
