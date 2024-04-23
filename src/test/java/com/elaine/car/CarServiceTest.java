package com.elaine.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarServiceTest {
    private CarDAO carDAO;
    private CarService carService;

    @BeforeEach
    public void setUp() {
        carDAO = mock(CarDAO.class);
        carService = new CarService(carDAO);
    }

    @Test
    public void getAllCars_ShouldReturnAllCars() {
        Car car1 = new Car("ABC123", new BigDecimal("99.99"), Brand.MERCEDES, false);
        Car car2 = new Car("XYZ789", new BigDecimal("149.99"), Brand.TESLA, true);
        when(carDAO.getAllCars()).thenReturn(Arrays.asList(car1, car2));

        List<Car> cars = carService.getAllCars();

        assertThat(cars).hasSize(2);
        assertThat(cars).containsExactlyInAnyOrder(car1, car2);
    }

    @Test
    public void getCar_ShouldReturnCarWhenRegNumberMatches() {
        Car car = new Car("ABC123", new BigDecimal("99.99"), Brand.MERCEDES, false);
        when(carDAO.getAllCars()).thenReturn(Arrays.asList(car));

        Car foundCar = carService.getCar("ABC123");

        assertThat(foundCar).isEqualToComparingFieldByField(car);
    }

    @Test
    public void getCar_ShouldThrowExceptionWhenRegNumberDoesNotMatch() {
        when(carDAO.getAllCars()).thenReturn(Arrays.asList());

        assertThatThrownBy(() -> carService.getCar("ABC123"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Car with reg ABC123 not found");
    }

    @Test
    public void getAllElectricCars_ShouldReturnOnlyElectricCars() {
        Car electricCar = new Car("XYZ789", new BigDecimal("149.99"), Brand.TESLA, true);
        Car nonElectricCar = new Car("ABC123", new BigDecimal("99.99"), Brand.MERCEDES, false);
        when(carDAO.getAllCars()).thenReturn(Arrays.asList(electricCar, nonElectricCar));

        List<Car> electricCars = carService.getAllElectricCars();

        assertThat(electricCars)
                .hasSize(1)
                .allMatch(Car::isElectric);
    }
}