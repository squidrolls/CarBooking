package com.elaine.booking;

import com.elaine.car.Car;
import com.elaine.car.CarService;
import com.elaine.user.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CarBookingService {

    private final CarBookingDao carBookingDao;
    private final CarService carService;

    public CarBookingService(CarBookingDao carBookingDao, CarService carService) {
        this.carBookingDao = carBookingDao;
        this.carService = carService;
    }


    public UUID bookCar(User user, String regNumber) {
        List<Car> availableCars = getAvailableCars();

        if (availableCars.isEmpty()) {
            throw new IllegalStateException("No car available for renting");
        }

        return availableCars.stream()
                .filter(car -> car.getRegNumber().equals(regNumber))
                .findFirst()
                .map(car -> {
                    Car carToBook = carService.getCar(regNumber);
                    UUID bookingId = UUID.randomUUID();
                    carBookingDao.book(new CarBooking(bookingId, user, carToBook, LocalDateTime.now()));
                    return bookingId;
                })
                .orElseThrow(() -> new IllegalStateException("Already booked. car with regNumber " + regNumber));
    }

    public List<Car> getUserBookedCars(UUID userId) {
        List<CarBooking> carBookings = carBookingDao.getCarBookings();

        return carBookings.stream()
                .filter(carBooking -> carBooking != null && carBooking.getUser().getId().equals(userId))
                .map(CarBooking :: getCar)
                .collect(Collectors.toList());
    }


    public List<Car> getAvailableCars() {
        return getCars(carService.getAllCars());
    }

    public List<Car> getAvailableElectricCars() {
        return getCars(carService.getAllElectricCars());
    }

    private List<Car> getCars(List<Car> cars) {

        // no cars in the system yet
        if (cars.isEmpty()) {
            return Collections.emptyList();
        }

        List<CarBooking> carBookings = carBookingDao.getCarBookings();

        // no bookings yet therefore all cars are available
        if (carBookings.isEmpty()) {
            return cars;
        }

        List<Car> availableCars = new ArrayList<>();

        Set<Car> bookedCars = carBookings.stream().filter(Objects::nonNull).map(CarBooking::getCar).collect(Collectors.toSet());
        return cars.stream()
                .filter(car -> !bookedCars.contains(car))
                .collect(Collectors.toList());
//        // populate available cars
//        for (Car car : cars) {
//            // lets check if car part of any booking.
//            // if not then its available but this time we add it to available cars
//            boolean booked = false;
//            for (CarBooking carBooking : carBookings) {
//                if (carBooking == null || !carBooking.getCar().equals(car)) {
//                    continue;
//                }
//                booked = true;
//            }
//            if (!booked) {
//                availableCars.add(car);
//            }
//        }
//
//        return availableCars;
    }

    public List<CarBooking> getBookings() {
        return carBookingDao.getCarBookings();
    }
}
