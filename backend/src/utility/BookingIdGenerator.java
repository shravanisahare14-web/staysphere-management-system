package utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class BookingIdGenerator {
    private static final Random random = new Random();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");

    public static String generateBookingId() {
        String year = LocalDateTime.now().format(formatter);
        int randomNum = 1000 + random.nextInt(9000);
        return "BK" + year + randomNum;
    }
}
