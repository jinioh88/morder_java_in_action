package ch11;

public class Person {
    private Car car;

    public Car getCar() {
        return car;
    }

    public String getCarInsuranceName(Person person) {
        if(person != null) {
            Car car = person.getCar();
            if(car != null) {
                Insurance insurance = car.getInsurance();
                if(insurance != null) {
                    return insurance.getName();
                }
            }
        }
        return "Unkown";
    }
}
