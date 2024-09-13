Feature: Booking API tests

  Background: The user is working with the Booking API

  Scenario: Get all bookings
    Given I perform a GET call to the bookings endpoint
    Then I verify that the status code is 200
    And I verify that the body does not have size 0

  Scenario: Get a specific booking by ID
    Given I perform a GET call to the bookings endpoint with id "1"
    Then I verify that the status code is 200
    And The booking should have the following information
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   |
      | Sally     | Jones    | 110        | false       | 2021-09-14 | 2023-04-25 |

  Scenario: Create a new booking
    Given I perform a POST call to the create booking endpoint with the following data
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   |
      | John      | Doe      | 150        | true        | 2024-09-12 | 2024-09-15 |
    Then I verify that the status code is 200
    And I verify the following booking data in the body response
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   |
      | John      | Doe      | 150        | true        | 2024-09-12 | 2024-09-15 |

  Scenario: Get a non-existent booking
    Given I perform a GET call to the bookings endpoint with id "9999"
    Then I verify that the status code is 404
    And I verify the error message is "Not Found"

  Scenario: Create a booking with invalid data
    Given I perform a POST call to the create booking endpoint with the following data
      | firstname | lastname | totalprice | depositpaid | checkin | checkout |
      | ""        | ""       | 0          | true        |         |          |
    Then I verify that the status code is 400
    And I verify the error message is "Bad Request"

  Scenario: Filter bookings by firstname
    Given I perform a GET call to the bookings endpoint with id "1"
    Then I verify that the status code is 200
    And The booking should have firstname "Sally"

