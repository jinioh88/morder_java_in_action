package ch02;

public class AppleWeightFormatter implements AppleFactoryFormatter {
    @Override
    public String format(Apple apple) {
        return String.valueOf(apple.getWeight());
    }
}
