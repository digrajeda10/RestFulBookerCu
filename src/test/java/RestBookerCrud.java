import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Booking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class RestBookerCrud {

    @Test
    public void getBookingsTest() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        Response response = RestAssured
                .when().get("/booking");
        response.then().assertThat().statusCode(200);
        response.then().log().body();  // Imprime el cuerpo de la respuesta
    }

    @Test
    public void getBookingByIdTest() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        // Obtener reserva con ID 1
        Response response = RestAssured
                .given().pathParam("id", "1")
                .when().get("/booking/{id}");
        response.then().assertThat().statusCode(200);
        response.then().log().body();
        response.then().assertThat().body("bookingid", Matchers.equalTo(1));
    }

    @Test
    public void postBookingTest() throws JsonProcessingException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        // Crear una nueva reserva
        Booking booking = new Booking();
        booking.setFirstname("John");
        booking.setLastname("Doe");
        booking.setTotalprice(150);
        booking.setDepositpaid(true);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);
        System.out.println(payload);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload)
                .when().post("/booking");

        response.then().assertThat().statusCode(200);
        response.then().log().body();  // Imprime el cuerpo de la respuesta
    }
}
