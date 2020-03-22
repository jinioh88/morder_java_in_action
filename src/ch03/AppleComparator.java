package ch03;

import ch02.Apple;

import java.util.Comparator;

public class AppleComparator implements Comparator<Apple> {
    @Override
    public int compare(Apple a1, Apple a2) {
        return String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight()));
    }
}
