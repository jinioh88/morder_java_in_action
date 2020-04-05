package ch11;

import java.util.Optional;

public class Insurance {
    private String name;
    public String getName() {
        return name;
    }

    public String getCarInsuranceName(Optional<Person> person, int minAge) {
        return person.filter(p -> p.getAge() >= minAge).flatMap(Person::getCar)
                .flatMap(Car::getInsurance).map(Insurance::getName).orElse("Unkown");
    }
}
