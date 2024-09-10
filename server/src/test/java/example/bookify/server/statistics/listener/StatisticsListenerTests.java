package example.bookify.server.statistics.listener;

import example.bookify.server.config.KafkaConfig;
import example.bookify.server.event.AuthenticationEvent;
import example.bookify.server.event.BookingEvent;
import example.bookify.server.event.UserRegistrationEvent;
import example.bookify.server.statistics.mapper.AuthenticationStatMapper;
import example.bookify.server.statistics.mapper.BookingStatMapper;
import example.bookify.server.statistics.mapper.UserRegistrationMapper;
import example.bookify.server.statistics.model.AuthenticationStat;
import example.bookify.server.statistics.model.BookingStat;
import example.bookify.server.statistics.model.UserRegistration;
import example.bookify.server.statistics.service.AuthenticationStatService;
import example.bookify.server.statistics.service.BookingStatService;
import example.bookify.server.statistics.service.UserRegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(classes = {
        KafkaAutoConfiguration.class,
        KafkaConfig.class,
        StatisticsListener.class,
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("StatisticsListener Kafka Integration Tests")
class StatisticsListenerTests {

    @Value("${app.kafka.topic.statistics.authentication}")
    private String authenticationTopic;

    @Value("${app.kafka.topic.statistics.user-registration}")
    private String userRegistrationTopic;

    @Value("${app.kafka.topic.statistics.booking}")
    private String bookingTopic;

    @Autowired
    private KafkaTemplate<String, AuthenticationEvent> authKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, UserRegistrationEvent> userRegKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, BookingEvent> bookingKafkaTemplate;

    @MockBean
    private AuthenticationStatService authenticationStatService;

    @MockBean
    private UserRegistrationService userRegistrationService;

    @MockBean
    private BookingStatService bookingStatService;

    @MockBean
    private AuthenticationStatMapper authenticationStatMapper;

    @MockBean
    private UserRegistrationMapper userRegistrationMapper;

    @MockBean
    private BookingStatMapper bookingStatMapper;

    @Container
    public static KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.7.0")
    );

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    @Test
    @DisplayName("Should correctly handle AuthenticationEvent from Kafka and save the corresponding data")
    void shouldHandleAuthenticationEventAndSaveToDB() {
        var event = AuthenticationEvent.builder()
                .userId(1L)
                .name("testUser")
                .build();

        var authenticationStat = AuthenticationStat.builder()
                .userId(1L)
                .name("testUser")
                .build();

        when(authenticationStatMapper.authenticationEventToAuthenticationStat(any(AuthenticationEvent.class)))
                .thenReturn(authenticationStat);
        when(authenticationStatService.save(authenticationStat))
                .thenReturn(Mono.empty());

        authKafkaTemplate.send(authenticationTopic, event);

        await()
                .pollInterval(5, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var captor = ArgumentCaptor.forClass(AuthenticationEvent.class);
                    verify(authenticationStatMapper).authenticationEventToAuthenticationStat(captor.capture());

                    var capturedEvent = captor.getValue();
                    assertThat(capturedEvent.getUserId()).isEqualTo(event.getUserId());
                    assertThat(capturedEvent.getName()).isEqualTo(event.getName());

                    verify(authenticationStatService).save(authenticationStat);
                });
    }

    @Test
    @DisplayName("Should correctly handle UserRegistrationEvent from Kafka and save the corresponding data")
    void shouldHandleUserRegistrationEventAndSaveToDB() {
        var event = UserRegistrationEvent.builder()
                .userId(1L)
                .name("testUser")
                .build();

        var userRegistration = UserRegistration.builder()
                .userId(1L)
                .name("testUser")
                .build();

        when(userRegistrationMapper.userRegistrationEventToUserRegistration(any(UserRegistrationEvent.class)))
                .thenReturn(userRegistration);

        when(userRegistrationService.save(userRegistration))
                .thenReturn(Mono.empty());

        userRegKafkaTemplate.send(userRegistrationTopic, event);

        await()
                .pollInterval(5, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var captor = ArgumentCaptor.forClass(UserRegistrationEvent.class);
                    verify(userRegistrationMapper).userRegistrationEventToUserRegistration(captor.capture());

                    var capturedEvent = captor.getValue();
                    assertThat(capturedEvent.getUserId()).isEqualTo(event.getUserId());
                    assertThat(capturedEvent.getName()).isEqualTo(event.getName());

                    verify(userRegistrationService).save(userRegistration);
                });
    }

    @Test
    @DisplayName("Should correctly handle BookingEvent from Kafka and save the corresponding data")
    void shouldHandleBookingEventAndSaveToDB() {
        var event = BookingEvent.builder()
                .userId(1L)
                .roomId(1L)
                .checkInDate(LocalDate.of(2024, 9, 10))
                .checkOutDate(LocalDate.of(2024, 9, 20))
                .build();

        var bookingStat = BookingStat.builder()
                .userId(1L)
                .roomId(1L)
                .checkInDate(LocalDate.of(2024, 9, 10))
                .checkOutDate(LocalDate.of(2024, 9, 20))
                .build();

        when(bookingStatMapper.bookingEventToBookingStat(any(BookingEvent.class)))
                .thenReturn(bookingStat);

        when(bookingStatService.save(bookingStat))
                .thenReturn(Mono.empty());

        bookingKafkaTemplate.send(bookingTopic, event);

        await()
                .pollInterval(5, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var captor = ArgumentCaptor.forClass(BookingEvent.class);
                    verify(bookingStatMapper).bookingEventToBookingStat(captor.capture());

                    var capturedEvent = captor.getValue();
                    assertThat(capturedEvent.getUserId()).isEqualTo(event.getUserId());
                    assertThat(capturedEvent.getRoomId()).isEqualTo(event.getRoomId());
                    assertThat(capturedEvent.getCheckInDate()).isEqualTo(event.getCheckInDate());
                    assertThat(capturedEvent.getCheckOutDate()).isEqualTo(event.getCheckOutDate());

                    verify(bookingStatService).save(bookingStat);
                });
    }
}
