package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private double totalAmount;
    private String bookingStatus;
    private LocalDate bookingDate;
    private String specialRequests;

    private static final double GST_RATE = 0.18;

    public Booking() {}

    public Booking(String bookingId, Guest guest, Room room, 
                   LocalDate checkInDate, LocalDate checkOutDate, 
                   int numberOfGuests, String specialRequests) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.bookingStatus = "CONFIRMED";
        this.bookingDate = LocalDate.now();
        this.specialRequests = specialRequests;
        this.totalAmount = calculateTotalAmount();
    }

    public double calculateTotalAmount() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) nights = 1;
        double roomCharges = nights * room.getPricePerNight();
        double gstAmount = roomCharges * GST_RATE;
        return roomCharges + gstAmount;
    }

    public long getNumberOfNights() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights > 0 ? nights : 1;
    }

    public double getRoomCharges() {
        return getNumberOfNights() * room.getPricePerNight();
    }

    public double getGstAmount() {
        return getRoomCharges() * GST_RATE;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public Guest getGuest() { return guest; }
    public void setGuest(Guest guest) { this.guest = guest; }
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}
