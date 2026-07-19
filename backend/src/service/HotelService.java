package service;

import model.Room;
import model.Guest;
import model.Booking;
import repository.FileManager;
import utility.BookingIdGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HotelService {

    private List<Room> rooms;
    private List<Booking> bookings;
    private List<Guest> guests;
    private FileManager fileManager;

    private static HotelService instance;

    private HotelService() {
        this.fileManager = new FileManager();
        this.rooms = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.guests = new ArrayList<>();
        initializeRooms();
        loadData();
    }

    public static HotelService getInstance() {
        if (instance == null) {
            instance = new HotelService();
        }
        return instance;
    }

    private void initializeRooms() {
        rooms.add(new Room("S101", "Single", 1500.00, 1, 
                "Cozy single room with city view", "WiFi, TV, AC, Hot Water", 
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=400"));
        rooms.add(new Room("S102", "Single", 1500.00, 1, 
                "Comfortable single room", "WiFi, TV, AC, Hot Water",
                "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=400"));
        rooms.add(new Room("S103", "Single", 1800.00, 1, 
                "Premium single with balcony", "WiFi, TV, AC, Mini Fridge, Balcony",
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400"));

        rooms.add(new Room("D201", "Double", 2800.00, 2, 
                "Spacious double room with queen bed", "WiFi, TV, AC, Geyser, Room Service",
                "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=400"));
        rooms.add(new Room("D202", "Double", 2800.00, 2, 
                "Double room with garden view", "WiFi, TV, AC, Geyser, Room Service",
                "https://images.unsplash.com/photo-1595576508898-0ad5c879a061?w=400"));
        rooms.add(new Room("D203", "Double", 3200.00, 2, 
                "Premium double with king bed", "WiFi, Smart TV, AC, Mini Fridge",
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=400"));
        rooms.add(new Room("D204", "Double", 3200.00, 2, 
                "Double room with pool view", "WiFi, Smart TV, AC, Mini Fridge, Pool View",
                "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=400"));

        rooms.add(new Room("DL301", "Deluxe", 4500.00, 3, 
                "Luxury deluxe suite with living area", "WiFi, Smart TV, AC, Mini Bar, Jacuzzi",
                "https://images.unsplash.com/photo-1631049552057-403cdb8f0658?w=400"));
        rooms.add(new Room("DL302", "Deluxe", 4500.00, 3, 
                "Deluxe room with panoramic view", "WiFi, Smart TV, AC, Mini Bar, Jacuzzi",
                "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=400"));
        rooms.add(new Room("DL303", "Deluxe", 5000.00, 3, 
                "Premium deluxe with balcony", "WiFi, Smart TV, AC, Mini Bar, Jacuzzi, Balcony",
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=400"));

        rooms.add(new Room("ST401", "Suite", 8000.00, 4, 
                "Presidential suite with 2 bedrooms", "WiFi, Smart TV, AC, Mini Bar, Jacuzzi, Kitchen",
                "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=400"));
        rooms.add(new Room("ST402", "Suite", 8000.00, 4, 
                "Royal suite with private terrace", "WiFi, Smart TV, AC, Mini Bar, Jacuzzi, Terrace",
                "https://images.unsplash.com/photo-1631049552057-403cdb8f0658?w=400"));
        rooms.add(new Room("ST403", "Suite", 12000.00, 6, 
                "Penthouse suite - Ultimate luxury", "All amenities + Private Pool, Gym, Spa",
                "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=400"));
    }

    private void loadData() {
        List<Booking> loadedBookings = fileManager.loadBookings();
        if (loadedBookings != null) {
            bookings = loadedBookings;
            for (Booking booking : bookings) {
                if (!booking.getBookingStatus().equals("CANCELLED") && 
                    !booking.getBookingStatus().equals("CHECKED_OUT")) {
                    Room room = findRoomById(booking.getRoom().getRoomId());
                    if (room != null) {
                        room.setAvailable(false);
                    }
                }
            }
        }
        List<Guest> loadedGuests = fileManager.loadGuests();
        if (loadedGuests != null) {
            guests = loadedGuests;
        }
    }

    public List<Room> getAllRooms() { return new ArrayList<>(rooms); }
    public List<Room> getAvailableRooms() {
        return rooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
    }
    public List<Room> searchRoomsByType(String roomType) {
        return rooms.stream().filter(r -> r.getRoomType().equalsIgnoreCase(roomType)).collect(Collectors.toList());
    }
    public List<Room> searchAvailableRoomsByType(String roomType) {
        return rooms.stream().filter(r -> r.getRoomType().equalsIgnoreCase(roomType) && r.isAvailable()).collect(Collectors.toList());
    }
    public Room findRoomById(String roomId) {
        return rooms.stream().filter(r -> r.getRoomId().equalsIgnoreCase(roomId)).findFirst().orElse(null);
    }
    public List<String> getRoomTypes() {
        return rooms.stream().map(Room::getRoomType).distinct().collect(Collectors.toList());
    }

    public Booking createBooking(String guestName, String phone, String email,
                                  String idProof, String idProofNumber, String address,
                                  String roomId, LocalDate checkIn, LocalDate checkOut,
                                  int numGuests, String specialRequests) throws Exception {

        Room room = findRoomById(roomId);
        if (room == null) throw new Exception("Room not found: " + roomId);
        if (!room.isAvailable()) throw new Exception("Room already booked!");
        if (numGuests > room.getMaxCapacity()) throw new Exception("Exceeds capacity");

        String guestId = "G" + System.currentTimeMillis();
        Guest guest = new Guest(guestId, guestName, phone, email, idProof, idProofNumber, address);
        guests.add(guest);

        String bookingId = BookingIdGenerator.generateBookingId();
        Booking booking = new Booking(bookingId, guest, room, checkIn, checkOut, numGuests, specialRequests);
        bookings.add(booking);
        room.setAvailable(false);

        fileManager.saveBookings(bookings);
        fileManager.saveGuests(guests);
        return booking;
    }

    public boolean cancelBooking(String bookingId) throws Exception {
        Booking booking = findBookingById(bookingId);
        if (booking == null) throw new Exception("Booking not found");
        if (booking.getBookingStatus().equals("CANCELLED")) throw new Exception("Already cancelled");
        if (booking.getBookingStatus().equals("CHECKED_OUT")) throw new Exception("Cannot cancel checked-out");

        booking.setBookingStatus("CANCELLED");
        Room actualRoom = findRoomById(booking.getRoom().getRoomId());
        if (actualRoom != null) actualRoom.setAvailable(true);
        fileManager.saveBookings(bookings);
        return true;
    }

    public boolean checkIn(String bookingId) throws Exception {
        Booking booking = findBookingById(bookingId);
        if (booking == null) throw new Exception("Booking not found");
        if (!booking.getBookingStatus().equals("CONFIRMED")) throw new Exception("Must be CONFIRMED");
        booking.setBookingStatus("CHECKED_IN");
        fileManager.saveBookings(bookings);
        return true;
    }

    public boolean checkOut(String bookingId) throws Exception {
        Booking booking = findBookingById(bookingId);
        if (booking == null) throw new Exception("Booking not found");
        if (!booking.getBookingStatus().equals("CHECKED_IN")) throw new Exception("Must be CHECKED_IN");
        booking.setBookingStatus("CHECKED_OUT");
        Room actualRoom = findRoomById(booking.getRoom().getRoomId());
        if (actualRoom != null) actualRoom.setAvailable(true);
        fileManager.saveBookings(bookings);
        return true;
    }

    public Booking findBookingById(String bookingId) {
        return bookings.stream().filter(b -> b.getBookingId().equalsIgnoreCase(bookingId)).findFirst().orElse(null);
    }
    public List<Booking> getAllBookings() { return new ArrayList<>(bookings); }
    public List<Booking> getActiveBookings() {
        return bookings.stream().filter(b -> !b.getBookingStatus().equals("CANCELLED") && 
                   !b.getBookingStatus().equals("CHECKED_OUT")).collect(Collectors.toList());
    }
    public double getTotalRevenue() {
        return bookings.stream().filter(b -> b.getBookingStatus().equals("CHECKED_OUT"))
                .mapToDouble(Booking::getTotalAmount).sum();
    }
    public int getTotalRooms() { return rooms.size(); }
    public int getOccupiedRooms() { return (int) rooms.stream().filter(r -> !r.isAvailable()).count(); }
    public int getAvailableRoomCount() { return (int) rooms.stream().filter(Room::isAvailable).count(); }
    public int getTotalBookings() { return bookings.size(); }
    public int getActiveBookingCount() { return getActiveBookings().size(); }

    public void saveAllData() {
        fileManager.saveBookings(bookings);
        fileManager.saveGuests(guests);
    }
}
