package domains;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingId {
    private int bookingid;
    private Booking booking;

    /*
        {
    "bookingid": 37,
    "booking": {
        ...
    }
}
 */

    public BookingId() {
    }

    @Override
    public String toString() {
        return "BookingId{" +
                "bookingid=" + bookingid +
                ", booking=" + booking +
                '}';
    }
}
