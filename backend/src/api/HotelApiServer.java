package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import service.HotelService;
import model.Room;
import model.Booking;
import model.Guest;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HotelApiServer - Lightweight REST API using Java's built-in HttpServer.
 * No external dependencies needed! Runs on port 8080.
 */
public class HotelApiServer {

    private static final int PORT = 8080;
    private static HotelService hotelService;

    public static void main(String[] args) throws IOException {
        hotelService = HotelService.getInstance();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // CORS headers for React frontend
        server.createContext("/api/rooms", new RoomsHandler());
        server.createContext("/api/rooms/available", new AvailableRoomsHandler());
        server.createContext("/api/bookings", new BookingsHandler());
        server.createContext("/api/bookings/create", new CreateBookingHandler());
        server.createContext("/api/bookings/cancel", new CancelBookingHandler());
        server.createContext("/api/bookings/checkin", new CheckInHandler());
        server.createContext("/api/bookings/checkout", new CheckOutHandler());
        server.createContext("/api/stats", new StatsHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("========================================");
        System.out.println("  Hotel API Server Started!");
        System.out.println("  URL: http://localhost:" + PORT);
        System.out.println("========================================");
        System.out.println("  Endpoints:");
        System.out.println("  GET  /api/rooms           - All rooms");
        System.out.println("  GET  /api/rooms/available - Available rooms");
        System.out.println("  GET  /api/bookings        - All bookings");
        System.out.println("  POST /api/bookings/create - Create booking");
        System.out.println("  POST /api/bookings/cancel - Cancel booking");
        System.out.println("  POST /api/bookings/checkin - Check in");
        System.out.println("  POST /api/bookings/checkout - Check out");
        System.out.println("  GET  /api/stats           - Dashboard stats");
        System.out.println("========================================");
    }

    // Helper: Add CORS headers
    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    // Helper: Send JSON response
    private static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        addCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Helper: Read request body
    private static String readBody(HttpExchange exchange) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    // Helper: Extract simple JSON value
    private static String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) {
            // Try numeric value
            search = "\"" + key + "\":";
            start = json.indexOf(search);
            if (start == -1) return "";
            start += search.length();
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return json.substring(start, end).trim();
        }
        start += search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    // ========== HANDLERS ==========

    static class RoomsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            List<Room> rooms = hotelService.getAllRooms();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < rooms.size(); i++) {
                Room r = rooms.get(i);
                json.append(String.format(
                    "{\"roomId\":\"%s\",\"roomType\":\"%s\",\"pricePerNight\":%.2f,\"available\":%b,\"maxCapacity\":%d,\"description\":\"%s\",\"amenities\":\"%s\",\"imageUrl\":\"%s\"}",
                    r.getRoomId(), r.getRoomType(), r.getPricePerNight(), r.isAvailable(),
                    r.getMaxCapacity(), r.getDescription(), r.getAmenities(), r.getImageUrl()));
                if (i < rooms.size() - 1) json.append(",");
            }
            json.append("]");
            sendJson(exchange, 200, json.toString());
        }
    }

    static class AvailableRoomsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            List<Room> rooms = hotelService.getAvailableRooms();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < rooms.size(); i++) {
                Room r = rooms.get(i);
                json.append(String.format(
                    "{\"roomId\":\"%s\",\"roomType\":\"%s\",\"pricePerNight\":%.2f,\"available\":%b,\"maxCapacity\":%d,\"description\":\"%s\",\"amenities\":\"%s\",\"imageUrl\":\"%s\"}",
                    r.getRoomId(), r.getRoomType(), r.getPricePerNight(), r.isAvailable(),
                    r.getMaxCapacity(), r.getDescription(), r.getAmenities(), r.getImageUrl()));
                if (i < rooms.size() - 1) json.append(",");
            }
            json.append("]");
            sendJson(exchange, 200, json.toString());
        }
    }

    static class BookingsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            List<Booking> bookings = hotelService.getAllBookings();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                json.append(String.format(
                    "{\"bookingId\":\"%s\",\"guestName\":\"%s\",\"roomId\":\"%s\",\"roomType\":\"%s\",\"checkInDate\":\"%s\",\"checkOutDate\":\"%s\",\"numberOfGuests\":%d,\"totalAmount\":%.2f,\"bookingStatus\":\"%s\",\"specialRequests\":\"%s\"}",
                    b.getBookingId(), b.getGuest().getName(), b.getRoom().getRoomId(),
                    b.getRoom().getRoomType(), b.getCheckInDate(), b.getCheckOutDate(),
                    b.getNumberOfGuests(), b.getTotalAmount(), b.getBookingStatus(),
                    b.getSpecialRequests() != null ? b.getSpecialRequests() : ""));
                if (i < bookings.size() - 1) json.append(",");
            }
            json.append("]");
            sendJson(exchange, 200, json.toString());
        }
    }

    static class CreateBookingHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            String body = readBody(exchange);
            try {
                String guestName = extractJsonValue(body, "guestName");
                String phone = extractJsonValue(body, "phone");
                String email = extractJsonValue(body, "email");
                String idProof = extractJsonValue(body, "idProof");
                String idProofNumber = extractJsonValue(body, "idProofNumber");
                String address = extractJsonValue(body, "address");
                String roomId = extractJsonValue(body, "roomId");
                String checkIn = extractJsonValue(body, "checkInDate");
                String checkOut = extractJsonValue(body, "checkOutDate");
                int numGuests = Integer.parseInt(extractJsonValue(body, "numberOfGuests"));
                String specialRequests = extractJsonValue(body, "specialRequests");

                Booking booking = hotelService.createBooking(
                    guestName, phone, email, idProof, idProofNumber, address,
                    roomId, LocalDate.parse(checkIn), LocalDate.parse(checkOut),
                    numGuests, specialRequests);

                sendJson(exchange, 200, String.format(
                    "{\"success\":true,\"bookingId\":\"%s\",\"totalAmount\":%.2f,\"message\":\"Booking created successfully\"}",
                    booking.getBookingId(), booking.getTotalAmount()));
            } catch (Exception e) {
                sendJson(exchange, 400, String.format("{\"success\":false,\"message\":\"%s\"}", e.getMessage()));
            }
        }
    }

    static class CancelBookingHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            String body = readBody(exchange);
            String bookingId = extractJsonValue(body, "bookingId");
            try {
                hotelService.cancelBooking(bookingId);
                sendJson(exchange, 200, "{\"success\":true,\"message\":\"Booking cancelled\"}");
            } catch (Exception e) {
                sendJson(exchange, 400, String.format("{\"success\":false,\"message\":\"%s\"}", e.getMessage()));
            }
        }
    }

    static class CheckInHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            String body = readBody(exchange);
            String bookingId = extractJsonValue(body, "bookingId");
            try {
                hotelService.checkIn(bookingId);
                sendJson(exchange, 200, "{\"success\":true,\"message\":\"Check-in successful\"}");
            } catch (Exception e) {
                sendJson(exchange, 400, String.format("{\"success\":false,\"message\":\"%s\"}", e.getMessage()));
            }
        }
    }

    static class CheckOutHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            String body = readBody(exchange);
            String bookingId = extractJsonValue(body, "bookingId");
            try {
                hotelService.checkOut(bookingId);
                sendJson(exchange, 200, "{\"success\":true,\"message\":\"Check-out successful\"}");
            } catch (Exception e) {
                sendJson(exchange, 400, String.format("{\"success\":false,\"message\":\"%s\"}", e.getMessage()));
            }
        }
    }

    static class StatsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            int totalRooms = hotelService.getTotalRooms();
            int occupied = hotelService.getOccupiedRooms();
            int available = hotelService.getAvailableRoomCount();
            double occupancyRate = totalRooms > 0 ? (occupied * 100.0 / totalRooms) : 0;
            double revenue = hotelService.getTotalRevenue();
            int totalBookings = hotelService.getTotalBookings();
            int activeBookings = hotelService.getActiveBookingCount();

            String json = String.format(
                "{\"totalRooms\":%d,\"occupiedRooms\":%d,\"availableRooms\":%d,\"occupancyRate\":%.1f,\"totalRevenue\":%.2f,\"totalBookings\":%d,\"activeBookings\":%d}",
                totalRooms, occupied, available, occupancyRate, revenue, totalBookings, activeBookings);
            sendJson(exchange, 200, json);
        }
    }
}
