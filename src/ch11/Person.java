package ch11;

import java.util.Optional;

public class Person {
    private Optional<Car> car;

    public Optional<Car> getCar() {
        return car;
    }

    public String getCarInsuranceName(Person person) {

        Optional<Car> car = person.getCar();

        Optional<Insurance> insurance = car.get().getInsurance();

        return insurance.get().getName();
    }
}
