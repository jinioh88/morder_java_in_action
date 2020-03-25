package ch04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

        // for-each 외부반복
        List<String> names = new ArrayList<>();
        for(Dish dish : menu) {
            names.add(dish.getName());
        }

        // 내부적으로 숨겨진 외부 반복
        Iterator<Dish> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Dish dish = iterator.next();
            names.add(dish.getName());
        }

        // 내부반복
        List<String> nameStream =  menu.stream().map(Dish::getName).collect(Collectors.toList());

        List<Dish> highCaloriecDishes = menu.stream().filter(m -> m.getCalories() > 300).collect(Collectors.toList());
        Iterator<Dish> iterator1 = menu.iterator();
        while(iterator1.hasNext()) {
            Dish dish = iterator.next();
            if(dish.getCalories() > 300) {
                highCaloriecDishes.add(dish);
            }
        }
    }
}
