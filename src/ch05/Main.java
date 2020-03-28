package ch05;

import ch04.Dish;
import org.w3c.dom.ls.LSOutput;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

        if(menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("채식 주의자");
        }

        int sum = numbers.stream().reduce(0, Integer::sum);
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        Optional<Integer> min = numbers.stream().reduce(Integer::min);

        long count = menu.stream().count();

        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed();

        OptionalInt maxCalories = menu.stream().mapToInt(Dish::getCalories).max();
        int max1 = maxCalories.orElse(1);  // 값이 없을때 기본값 명시

        Stream<String> strStream = Stream.of("Mordern", "Java", "In", "Action");

        Stream.iterate(0, i -> i + 2).limit(10).forEach(System.out::println);

        Stream.generate(Math::random).limit(5).forEach(System.out::println);
    }
}
