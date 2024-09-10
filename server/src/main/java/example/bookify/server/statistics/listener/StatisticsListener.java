package example.bookify.server.statistics.listener;

import example.bookify.server.event.AuthenticationEvent;
import example.bookify.server.event.BookingEvent;
import example.bookify.server.event.UserRegistrationEvent;
import example.bookify.server.statistics.mapper.AuthenticationStatMapper;
import example.bookify.server.statistics.mapper.BookingStatMapper;
import example.bookify.server.statistics.mapper.UserRegistrationMapper;
import example.bookify.server.statistics.service.AuthenticationStatService;
import example.bookify.server.statistics.service.BookingStatService;
import example.bookify.server.statistics.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsListener {

    private final AuthenticationStatService authenticationStatService;
    private final UserRegistrationService userRegistrationService;
    private final BookingStatService bookingStatService;

    private final AuthenticationStatMapper authenticationStatMapper;
    private final UserRegistrationMapper userRegistrationMapper;
    private final BookingStatMapper bookingStatMapper;

    @KafkaListener(topics = "${app.kafka.topic.statistics.authentication}")
    public void onAuthenticationEvent(AuthenticationEvent event) {
        var authentication = authenticationStatMapper.authenticationEventToAuthenticationStat(event);
        authenticationStatService.save(authentication).subscribe();
    }

    @KafkaListener(topics = "${app.kafka.topic.statistics.user-registration}")
    public void onUserRegistrationEvent(UserRegistrationEvent event) {
        var userRegistration = userRegistrationMapper.userRegistrationEventToUserRegistration(event);
        userRegistrationService.save(userRegistration).subscribe();
    }

    @KafkaListener(topics = "${app.kafka.topic.statistics.booking}")
    public void onBookingEvent(BookingEvent event) {
        var userBooking = bookingStatMapper.bookingEventToBookingStat(event);
        bookingStatService.save(userBooking).subscribe();
    }
}
