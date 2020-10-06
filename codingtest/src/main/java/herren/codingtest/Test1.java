package herren.codingtest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Test1 {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 8, 3, 6, 5);

        Test1 test = new Test1();
        System.out.println(test.reverseNumber(numbers));
    }

    public List<Integer> reverseNumber(List<Integer> numbers) {
        return numbers.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }
}
