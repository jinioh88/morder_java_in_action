package ch08;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<String> friends = List.of("Sejin", "NamTack", "YongNam");

        friends.removeIf(f -> f.length() > 10);

        Map.of(1,"hi", 2, "hello").forEach();
    }
}
