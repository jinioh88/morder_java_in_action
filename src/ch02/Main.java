package ch02;

import ch03.BufferedReaderProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Apple apple = new Apple();
        List<Apple> inventory = new ArrayList<>();

        inventory.sort(Comparator.comparing(Apple::getWeight));

        List<Apple> redApples = apple.filterApples(inventory, (Apple a) -> Color.RED.equals(a.getColor()));
        inventory.sort((Apple a1, Apple a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())));

        List<String> listOfStrings = new ArrayList<>();
        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
        List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);

        forEach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.println(i));

        List<Integer> l= map(Arrays.asList("lambdas", "in", "action"), (String s) -> s.length());

        IntPredicate evenNumbers = (int i) -> i % 2 == 0; // Integer가 아닌 int다.
        evenNumbers.test(1000);

        Callable<Integer> c = () -> 42;
        PrivilegedAction<Integer> p = () -> 42;

        Comparator<Apple> c1 = (Apple a1, Apple a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())); // 형식 추론 안함
        Comparator<Apple> c2 = (a1, a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())); // 형식 추론

        ToIntFunction<String> stringToInt = Integer::parseInt;
        BiPredicate<List<String>, String> contains = List::contains;

        Supplier<Apple> cc = () -> new Apple();
        Supplier<Apple> cc2 = Apple::new;
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for(T t : list) {
            result.add(f.apply(t));
        }
        return result;
    }

    public static <T> void forEach(List<T> list, Consumer<T> c) {
        for(T t : list) {
            c.accept(t);
        }
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> results = new ArrayList<>();
        for(T t : list) {
            if(p.test(t)) {
                results.add(t);
            }
        }
        return results;
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return p.process(br);
        }
    }
}
