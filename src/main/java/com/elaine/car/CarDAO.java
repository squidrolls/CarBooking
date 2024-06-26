package com.elaine.car;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CarDAO {

    private static final Car[] CARS = {
            new Car("1234", new BigDecimal("89.00"), Brand.TESLA, true),
            new Car("5678", new BigDecimal("50.00"), Brand.AUDI, false),
            new Car("5690", new BigDecimal("77.00"), Brand.MERCEDES, false),
    };

    public List<Car> getAllCars() {
        return Arrays.asList(CARS);
    }
}
