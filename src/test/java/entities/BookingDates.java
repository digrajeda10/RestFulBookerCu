package entities;

import lombok.Getter;
import lombok.Setter;

public class BookingDates {  // Clase para manejar las fechas de checkin y checkout
    @Getter @Setter
    private String checkin;
    @Getter @Setter
    private String checkout;
}
