package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.BookerEndpoints;
import entities.Booking;
import entities.BookingDates;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import utils.Request;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.not;

public class BookingSteps {
    Response response;

    @Given("I perform a GET call to the bookings endpoint")
    public void getBookings(){
        response = Request.get(BookerEndpoints.GET_BOOKINGS);
    }

    @Then("I verify that the status code is {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();

        if (actualStatusCode != expectedStatusCode) {
            String errorMessage = "Expected status code: " + expectedStatusCode + "\n" +
                    "Actual status code: " + actualStatusCode + "\n" +
                    "Response body: " + response.getBody().asString() + "\n" +
                    "BUG: El sistema devuelve un código 500 en lugar de 400 cuando se envían datos inválidos. Esto debería ser 'Bad Request'.";

            throw new AssertionError(errorMessage);
        }

        response.then().statusCode(expectedStatusCode);
    }

    @And("I verify that the body does not have size {int}")
    public void verifyResponseSize(int size){
        response.then().assertThat().body("size()", not(size));
    }

    @Given("I perform a GET call to the bookings endpoint with id {string}")
    public void getBookingById(String id){
        response = Request.getById(BookerEndpoints.GET_BOOKING_BY_ID, id);
    }

    @Then("The booking should have the following information")
    public void verifyBookingInformation(DataTable bookingInfo) {
        List<Map<String, String>> data = bookingInfo.asMaps(String.class, String.class);

        for (Map<String, String> row : data) {
            response.then().assertThat().body("firstname", Matchers.equalTo(row.get("firstname")));
            response.then().assertThat().body("lastname", Matchers.equalTo(row.get("lastname")));
            response.then().assertThat().body("totalprice", Matchers.equalTo(Integer.parseInt(row.get("totalprice"))));
            response.then().assertThat().body("depositpaid", Matchers.equalTo(Boolean.parseBoolean(row.get("depositpaid"))));
            response.then().assertThat().body("bookingdates.checkin", Matchers.equalTo(row.get("checkin")));
            response.then().assertThat().body("bookingdates.checkout", Matchers.equalTo(row.get("checkout")));
        }
    }

    @Given("I perform a POST call to the create booking endpoint with the following data")
    public void createBooking(DataTable bookingData) throws JsonProcessingException {

        List<Map<String, String>> data = bookingData.asMaps(String.class, String.class);
        Booking booking = getBooking(data);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);

        response = Request.post(BookerEndpoints.CREATE_BOOKING, payload);
    }

    private static Booking getBooking(List<Map<String, String>> data) {
        Map<String, String> bookingInfo = data.get(0);

        Booking booking = new Booking();
        booking.setFirstname(bookingInfo.get("firstname"));
        booking.setLastname(bookingInfo.get("lastname"));
        booking.setTotalprice(Integer.parseInt(bookingInfo.get("totalprice")));
        booking.setDepositpaid(Boolean.parseBoolean(bookingInfo.get("depositpaid")));

        BookingDates bookingDates = new BookingDates();  // Crear el objeto BookingDates
        bookingDates.setCheckin(bookingInfo.get("checkin"));
        bookingDates.setCheckout(bookingInfo.get("checkout"));
        booking.setBookingdates(bookingDates);  // Asignar las fechas al objeto Booking

        return booking;
    }

    @And("I verify the following booking data in the body response")
    public void verifyBookingDataInResponse(DataTable expectedData) {
        List<Map<String, String>> data = expectedData.asMaps(String.class, String.class);
        Map<String, String> expectedInfo = data.get(0);

        response.then().assertThat().body("booking.firstname", Matchers.equalTo(expectedInfo.get("firstname")));
        response.then().assertThat().body("booking.lastname", Matchers.equalTo(expectedInfo.get("lastname")));
        response.then().assertThat().body("booking.totalprice", Matchers.equalTo(Integer.parseInt(expectedInfo.get("totalprice"))));
        response.then().assertThat().body("booking.depositpaid", Matchers.equalTo(Boolean.parseBoolean(expectedInfo.get("depositpaid"))));
        response.then().assertThat().body("booking.bookingdates.checkin", Matchers.equalTo(expectedInfo.get("checkin")));
        response.then().assertThat().body("booking.bookingdates.checkout", Matchers.equalTo(expectedInfo.get("checkout")));
    }

    @And("I verify the error message is {string}")
    public void verifyErrorMessage(String errorMessage) {
        // Verifica el mensaje de error como texto plano
        response.then().assertThat().body(Matchers.equalTo(errorMessage));
    }

    @And("The booking should have firstname {string}")
    public void verifyBookingFirstname(String expectedFirstname) {
        response.then().assertThat().body("firstname", Matchers.equalTo(expectedFirstname));
    }
}
