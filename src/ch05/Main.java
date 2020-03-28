package ch05;

import ch04.Dish;
import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french", false, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season", false, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH)
        );

        List<Dish> vegetarianMenu = menu.stream().filter(Dish::isVegetarian).limit(3).collect(Collectors.toList());

        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream().filter(i -> i%2 == 0).distinct().collect(Collectors.toList());

        List<Dish> sliceMenu1 = menu.stream().takeWhile(dish -> dish.getCalories() < 320).collect(Collectors.toList());
        List<Dish> meatMenu =
                menu.stream().filter(d -> d.getType() == (Dish.Type.MEAT)).limit(3).collect(Collectors.toList());

        List<Integer> dishNames = menu.stream().map(Dish::getName).map(String::length).collect(Collectors.toList());

        List<String> words = Arrays.asList("Morern", "Java", "In", "Action");
        List<String> uniqueChars = words.stream().map(word -> word.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
    }
}
