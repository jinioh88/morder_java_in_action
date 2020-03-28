package ch06;

import ch04.Dish;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

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

        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCaloreisDish = menu.stream().collect(maxBy(dishCaloriesComparator));

        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));

        String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(","));

        int totalCalories1 = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();

        Map<Dish.Type, List<Dish>> dishesByType =
                menu.stream().collect(groupingBy(Dish::getType, filtering(d -> d.getCalories() > 500, toList())));

        Map<Dish.Type, List<String>> dishNamesByType = menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));

        Map<Boolean, Map<Dish.Type, List<Dish>>> patitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
    }
}
