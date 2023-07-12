## Overview
CinemaDemo is an example of a simple seat reservation system for multiplex cinemas.
It's written in Spring framework and utilizes Hibernate ORM for effortless handling of records, Hibernate Validator for validation and other minor dependencies like Guava and Lombok. Additionally, with the advent of Spring Boot 3.1 all apps can handle Docker containers by themselves, and this feature is also used here.
All tests are written using JUnit5, Spring Boot Test and Testcontainers.

## Additional assumptions
- Every row in each screening room has the same size
- Reservation expiration date is denoted by the end of screening
- Timestamps are returned in ISO 8601 standard format

## How to start application
- Make sure Docker daemon is up and running
- In main directory, run `./gradlew bootRun`
- Enjoy!

## How to run an example reservation flow
- Run `./example_flow.sh`

## Endpoints

### GET /screenings
#### Parameters (query):
- `from` -  unix timestamp
- `to` - unix timestamp
#### Example request:
```bash
curl -v "localhost:8080/screenings?from=1814937300&to=1814952600"
```
#### Example response:
```json
[
  {
    "id": 4,
    "time": "2027-07-07T07:15:00Z",
    "movie": {
      "id": 2,
      "title": "Ęśąćż",
      "duration": "PT1H20M"
    },
    "room": {
      "id": 2,
      "numberOfRows": 4,
      "seatsPerRow": 4
    }
  }
]
```
---

### GET /screenings/{id}
#### Parameters (path):
- `id` - the id of requested screening
#### Example request:
```bash
curl -v "localhost:8080/screenings/1"
```
#### Example response:
```json
{
  "room": {
    "id": 1,
    "numberOfRows": 5,
    "seatsPerRow": 10
  },
  "availableSeatsInRows": {
    "1": [
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    ],
    "2": [
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    ],
    "3": [
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    ],
    "4": [
	  1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    ],
    "5": [
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    ]
  }
}
```
---

### POST /reservations
#### Parameters (JSON fields):
- `name` - the name of the user making a reservation
- `surname` - the surname of the user making a reservation
- `screeningId` - the ID of the desired screening
- `tickets` - an array of tickets, which consist of:
    -  `type` - the ticket type, an enum
    - `row` - the selected row
    - `seat` - the selected seat
#### Example request:
```bash
curl -v POST -H "Content-Type: application/json" \  
-d '{"name":"Maria","surname":"Skłodowska-Curie","screeningId":4,"tickets":[{"type":"ADULT","row":1,"seat":1}]}' \  
"localhost:8080/reservations"
```
#### Example response:
```json
{
  "amountToPay": 25,
  "expirationDate": "2027-07-07T09:00:00Z"
}
```
