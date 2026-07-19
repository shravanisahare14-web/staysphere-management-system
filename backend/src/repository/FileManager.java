package repository;

import model.Booking;
import model.Guest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String BOOKINGS_FILE = "data/bookings.dat";
    private static final String GUESTS_FILE = "data/guests.dat";

    public void saveBookings(List<Booking> bookings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Booking> loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKINGS_FILE))) {
            return (List<Booking>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public void saveGuests(List<Guest> guests) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GUESTS_FILE))) {
            oos.writeObject(guests);
        } catch (IOException e) {
            System.err.println("Error saving guests: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Guest> loadGuests() {
        File file = new File(GUESTS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GUESTS_FILE))) {
            return (List<Guest>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
