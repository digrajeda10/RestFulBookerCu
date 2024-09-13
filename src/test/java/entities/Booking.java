package entities;

import lombok.Getter;
import lombok.Setter;

public class Booking {
    @Getter @Setter
    private String firstname;
    @Getter @Setter
    private String lastname;
    @Getter @Setter
    private int totalprice;
    @Getter @Setter
    private boolean depositpaid;
    @Getter @Setter
    private BookingDates bookingdates;  // Agrega un objeto anidado para las fechas
}

