package ch13;

public interface A {
    default void hello() {
        System.out.println("A");
    }
}
