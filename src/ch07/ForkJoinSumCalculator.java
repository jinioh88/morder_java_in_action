package ch07;

import java.util.concurrent.RecursiveTask;

public class ForkJoinSumCalculator extends RecursiveTask<Long> {
    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 10_000;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if(length < THRESHOLD) {
            return computeSequentially();
        }

        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);
        leftTask.fork();  // 비동기로 실행

        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length/2, length/2);

        Long rightResult = rightTask.compute();  // 두 번째 task를 동기 실행. 추가 분할 발생 가능성 있음
        Long leftResult = leftTask.join();  // 첫 번째 테스크의 결과를 읽거나 아직 결과가 없으면 기다림

        return leftResult + rightResult;
    }

    private Long computeSequentially() {
        long sum = 0;
        for(int i = start; i < end; i++) {
            sum +=numbers[i];
        }
        return sum;
    }
}
