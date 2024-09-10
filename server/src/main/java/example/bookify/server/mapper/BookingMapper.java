package example.bookify.server.mapper;

import example.bookify.server.event.BookingEvent;
import example.bookify.server.model.Booking;
import example.bookify.server.service.RoomService;
import example.bookify.server.service.UserService;
import example.bookify.server.web.dto.request.BookingRequest;
import example.bookify.server.web.dto.response.BookingResponse;
import example.bookify.server.web.dto.response.BookingPageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BookingMapper {

    protected RoomService roomService;
    protected UserService userService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Mapping(target = "room", expression = "java(roomService.findById(request.getRoomId()))")
    @Mapping(target = "user", expression = "java(userService.findById(request.getUserId()))")
    public abstract Booking requestToBooking(BookingRequest request);

    @Mapping(target = "roomId", expression = "java(booking.getRoom().getId())")
    @Mapping(target = "userId", expression = "java(booking.getUser().getId())")
    public abstract BookingResponse bookingToResponse(Booking booking);

    @Mapping(target = "roomId", expression = "java(booking.getRoom().getId())")
    @Mapping(target = "userId", expression = "java(booking.getUser().getId())")
    public abstract BookingEvent bookingToBookingEvent(Booking booking);

    public abstract List<BookingResponse> bookingListToBookingResponseList(List<Booking> bookingList);

    @Mapping(target = "totalElements", expression = "java(bookingPage.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(bookingPage.getTotalPages())")
    @Mapping(target = "bookings", expression = "java(bookingListToBookingResponseList(bookingPage.getContent()))")
    public abstract BookingPageResponse pageToBookingPageResponse(Page<Booking> bookingPage);
}
