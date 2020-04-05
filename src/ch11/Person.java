package ch11;

import javax.swing.text.html.Option;
import java.util.Optional;

public class Person {
    private Optional<Car> car;

    private int age;

    public int getAge() {
        return age;
    }

    public Optional<Car> getCar() {
        return car;
    }

    public String getCarInsuranceName(Optional<Person> person) {
        return person.flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName).orElse("Unkown");
    }

}
