package tech.firanek.cinemademo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tech.firanek.cinemademo.controller.CinemaController;
import tech.firanek.cinemademo.data.ReservationRequest;
import tech.firanek.cinemademo.entity.Ticket;
import tech.firanek.cinemademo.entity.TicketType;
import tech.firanek.cinemademo.repository.ReservationRepository;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CinemaController.class)
@AutoConfigureDataJpa
@Testcontainers
class CinemaControllerTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:lts");

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void clearDatabase(@Autowired ReservationRepository reservationRepository) {
        reservationRepository.deleteAll();
    }

    @Test
    void shouldReturnErrorWhenNoParametersProvided() throws Exception {
        mvc.perform(get("/screenings"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldReturnScreeningsBetweenTimestamps() throws Exception {
        mvc.perform(get("/screenings")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("from", "1688714100")
                        .param("to", "1688729400"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(3))
                .andExpect(jsonPath("$[0].movie.title").value("Ęśąćż"))
                .andExpect(jsonPath("$[1].movie.title").value("Ferdydurke"))
                .andExpect(jsonPath("$[2].movie.title").value("NullPointerException: the movie"))
                .andReturn();
    }

    @Test
    void shouldReturnErrorWhenNonexistentScreeningIdRequested() throws Exception {
        mvc.perform(get("/screenings/0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No entity associated with provided id 0"))
                .andReturn();
    }

    @Test
    void shouldReturnScreeningById() throws Exception {
        mvc.perform(get("/screenings/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.room.numberOfRows").value(5))
                .andExpect(jsonPath("$.availableSeatsInRows.1.size()").value(10))
                .andReturn();
    }

    @Test
    void shouldReserveSelectedSeat() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Maria", "Skłodowska-Curie", 1L, new Ticket(TicketType.ADULT, 1, 1))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amountToPay").value(25D))
                .andExpect(jsonPath("$.expirationDate").value("2023-07-07T09:00:00Z"))
                .andReturn();
    }


    @Test
    void shouldReturnErrorWhenNonexistentSeatNumberProvided() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Maria", "Skłodowska-Curie", 1L, new Ticket(TicketType.ADULT, 1, 100))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Provided an inexistent seat number"))
                .andReturn();
    }

    @Test
    void shouldReturnErrorWhenNonexistentRowNumberProvided() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Maria", "Skłodowska-Curie", 1L, new Ticket(TicketType.ADULT, 100, 1))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Provided an inexistent row number"))
                .andReturn();
    }


    @Test
    void shouldReturnErrorWhenNonexistentScreeningIdProvided() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Maria", "Skłodowska-Curie", 0L, new Ticket(TicketType.ADULT, 1, 1))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No entity associated with provided id 0"))
                .andReturn();
    }

    @Test
    void shouldReturnErrorWhenNoTicketsSelected() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Maria", "Skłodowska-Curie", 1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors present: Did not provide any tickets"))
                .andReturn();
    }

    @Test
    void shouldReturnErrorWhenSelectedSeatsAreNotNextToEachOther() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Maria", "Skłodowska-Curie", 1, new Ticket(TicketType.ADULT, 1, 1), new Ticket(TicketType.ADULT, 1, 3))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("There is an empty space between selected seats"))
                .andReturn();
    }

    @Test
    void shouldReturnErrorWhenReservingAlreadyTakenSeat() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Karol", "Krawczyk", 1L, new Ticket(TicketType.ADULT, 1, 1))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amountToPay").value(25D))
                .andExpect(jsonPath("$.expirationDate").value("2023-07-07T09:00:00Z"))
                .andReturn();

        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Jan", "Malinowski", 1L, new Ticket(TicketType.STUDENT, 1, 1))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Provided a seat that is already booked"))
                .andReturn();
    }


    @Test
    void shouldReturnErrorWhenReservingTheSameSeatMultipleTimes() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Maria", "Skłodowska-Curie", 1,
                                new Ticket(TicketType.ADULT, 1, 1), new Ticket(TicketType.CHILD, 1, 1))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Provided multiple tickets for one seat"))
                .andReturn();
    }

    @Test
    void shouldReturnErrorWhenInvalidNameProvided() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("maria", "Skłodowska-Curie", 1L, new Ticket(TicketType.ADULT, 1, 1))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors present: Provided name is invalid"))
                .andReturn();

        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Maria", "Skłodowska-", 1L, new Ticket(TicketType.ADULT, 1, 1))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors present: Provided surname is invalid"))
                .andReturn();
    }

    @Test
    void shouldReturnErrorWhenTooShortNameProvided() throws Exception {
        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Az", "Xyz", 1L, new Ticket(TicketType.ADULT, 1, 1))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors present: Provided name is too short"))
                .andReturn();

        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.reservationJson("Xyz", "Az", 1L, new Ticket(TicketType.ADULT, 1, 1))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors present: Provided surname is too short"))
                .andReturn();
    }

    @SneakyThrows
    private String reservationJson(final String name, final String surname, long screeningId, Ticket... tickets) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(new ReservationRequest(name,
                surname,
                screeningId,
                new HashSet<>(Arrays.asList(tickets))));
    }

}