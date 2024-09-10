package example.bookify.server.statistics.web;

import example.bookify.server.statistics.model.AuthenticationStat;
import example.bookify.server.statistics.model.BookingStat;
import example.bookify.server.statistics.model.UserRegistration;
import example.bookify.server.statistics.repository.AuthenticationStatRepository;
import example.bookify.server.statistics.repository.BookingStatRepository;
import example.bookify.server.statistics.repository.UserRegistrationRepository;
import example.bookify.server.statistics.web.dto.AuthenticationStatPageResponse;
import example.bookify.server.statistics.web.dto.BookingStatPageResponse;
import example.bookify.server.statistics.web.dto.UserRegistrationPageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@DisplayName("StatisticsController Integration Tests")
class StatisticsControllerIntegrationTests extends AbstractReactiveControllerTest {

    @Autowired
    private AuthenticationStatRepository authenticationStatRepository;

    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Autowired
    private BookingStatRepository bookingStatRepository;

    private final AuthenticationStat AUTH_STAT = AuthenticationStat.builder()
            .userId(1L)
            .name("testUser")
            .device("testDevice")
            .os("testOS")
            .browser("testBrowser")
            .remoteAddress("0.0.0.0")
            .build();

    private final UserRegistration USER_REGISTRATION = UserRegistration.builder()
            .userId(1L)
            .name("testUser")
            .email("testEmail")
            .build();

    private final BookingStat BOOKING_STAT = BookingStat.builder()
            .userId(1L)
            .roomId(1L)
            .checkInDate(LocalDate.of(2024, 9, 10))
            .checkOutDate(LocalDate.of(2024, 9, 20))
            .build();

    @BeforeEach
    public void init() {
        authenticationStatRepository.deleteAll().block();
        userRegistrationRepository.deleteAll().block();
        bookingStatRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should return OK response with authentication statistics page")
    void shouldReturnOkResponseWithAuthenticationStatisticsPage() {
        authenticationStatRepository.save(AUTH_STAT).block();

        webTestClient.get().uri("/api/statistics/logins?pageNumber=0&pageSize=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthenticationStatPageResponse.class)
                .consumeWith(response -> {
                    var authStatPage = response.getResponseBody();

                    assertThat(authStatPage).isNotNull();
                    assertThat(authStatPage.getTotalElements()).isEqualTo(1);
                    assertThat(authStatPage.getTotalPages()).isEqualTo(1);
                    assertThat(authStatPage.getLogins()).hasSize(1);
                    assertThat(authStatPage.getLogins().get(0).getUserId()).isEqualTo(AUTH_STAT.getUserId());
                    assertThat(authStatPage.getLogins().get(0).getName()).isEqualTo(AUTH_STAT.getName());
                    assertThat(authStatPage.getLogins().get(0).getDevice()).isEqualTo(AUTH_STAT.getDevice());
                    assertThat(authStatPage.getLogins().get(0).getOs()).isEqualTo(AUTH_STAT.getOs());
                    assertThat(authStatPage.getLogins().get(0).getBrowser()).isEqualTo(AUTH_STAT.getBrowser());
                    assertThat(authStatPage.getLogins().get(0).getRemoteAddress()).isEqualTo(AUTH_STAT.getRemoteAddress());
                    assertThat(authStatPage.getLogins().get(0).getTimestamp()).isInThePast();
                });
    }

    @Test
    @DisplayName("Should return OK response with user registration statistics page")
    void shouldReturnOkResponseWithUserRegistrationStatisticsPage() {
        userRegistrationRepository.save(USER_REGISTRATION).block();

        webTestClient.get().uri("/api/statistics/registrations?pageNumber=0&pageSize=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserRegistrationPageResponse.class)
                .consumeWith(response -> {
                    var userRegPage = response.getResponseBody();

                    assertThat(userRegPage).isNotNull();
                    assertThat(userRegPage.getTotalElements()).isEqualTo(1);
                    assertThat(userRegPage.getTotalPages()).isEqualTo(1);
                    assertThat(userRegPage.getRegistrations()).hasSize(1);
                    assertThat(userRegPage.getRegistrations().get(0).getUserId()).isEqualTo(USER_REGISTRATION.getUserId());
                    assertThat(userRegPage.getRegistrations().get(0).getName()).isEqualTo(USER_REGISTRATION.getName());
                    assertThat(userRegPage.getRegistrations().get(0).getEmail()).isEqualTo(USER_REGISTRATION.getEmail());
                    assertThat(userRegPage.getRegistrations().get(0).getTimestamp()).isInThePast();
                });
    }

    @Test
    @DisplayName("Should return OK response with booking statistics page")
    void shouldReturnOkResponseWithBookingStatisticsPage() {
        bookingStatRepository.save(BOOKING_STAT).block();

        webTestClient.get().uri("/api/statistics/bookings?pageNumber=0&pageSize=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookingStatPageResponse.class)
                .consumeWith(response -> {
                    var bookingStatPage = response.getResponseBody();

                    assertThat(bookingStatPage).isNotNull();
                    assertThat(bookingStatPage.getTotalElements()).isEqualTo(1);
                    assertThat(bookingStatPage.getTotalPages()).isEqualTo(1);
                    assertThat(bookingStatPage.getBookings()).hasSize(1);
                    assertThat(bookingStatPage.getBookings().get(0).getUserId()).isEqualTo(BOOKING_STAT.getUserId());
                    assertThat(bookingStatPage.getBookings().get(0).getRoomId()).isEqualTo(BOOKING_STAT.getRoomId());
                    assertThat(bookingStatPage.getBookings().get(0).getCheckInDate()).isEqualTo(BOOKING_STAT.getCheckInDate());
                    assertThat(bookingStatPage.getBookings().get(0).getCheckOutDate()).isEqualTo(BOOKING_STAT.getCheckOutDate());
                    assertThat(bookingStatPage.getBookings().get(0).getTimestamp()).isInThePast();
                });
    }

    @Test
    @DisplayName("Should download zip with statistics csv files and compare with expected file names and content")
    void shouldDownloadStatisticsFileAndCompareWithExpected(){
        var expectedCsvFileNames = List.of("authentications.csv", "user-registrations.csv", "bookings.csv");

        authenticationStatRepository.save(AUTH_STAT).block();
        userRegistrationRepository.save(USER_REGISTRATION).block();
        bookingStatRepository.save(BOOKING_STAT).block();

        webTestClient.get().uri("/api/statistics/download")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(
                        HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"statistics.zip\""
                )
                .expectHeader().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .expectBody(Resource.class)
                .consumeWith(response -> {
                    var resource = response.getResponseBody();
                    assertThat(resource).isNotNull();
                    assertThat(resource).isInstanceOf(ByteArrayResource.class);

                    try (var zipInputStream = new ZipInputStream(resource.getInputStream())) {
                        ZipEntry entry;
                        while ((entry = zipInputStream.getNextEntry()) != null) {
                            var fileName = entry.getName();
                            assertThat(expectedCsvFileNames).contains(fileName);

                            var actualContent = new String(zipInputStream.readAllBytes(), StandardCharsets.UTF_8);
                            assertThat(actualContent).contains("time");

                            var timeValuePattern = "(?m)\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.(\\d|\\w)*";
                            assertThat(actualContent).containsPattern(timeValuePattern);

                            actualContent = actualContent
                                    .replaceAll(timeValuePattern, "1000-01-01T00:00:00.0")
                                    .replaceAll(",*$", "");

                            var expectedContent = Files.readString(
                                    Path.of("src/test/resources/expected/" + fileName),
                                    StandardCharsets.UTF_8
                            );

                            assertThat(actualContent).isEqualToIgnoringNewLines(expectedContent);
                        }
                    } catch (IOException exc) {
                        fail("Failed to read zip content", exc);
                    }
                });
    }
}
