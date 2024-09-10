package example.bookify.server.mapper;

import example.bookify.server.model.Hotel;
import example.bookify.server.model.Room;
import example.bookify.server.service.HotelService;
import example.bookify.server.web.dto.request.RoomUpsertRequest;
import example.bookify.server.web.dto.response.RoomPageResponse;
import example.bookify.server.web.dto.response.RoomResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RoomMapper {

    protected HotelService hotelService;

    @Autowired
    public void setHotelService(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Mapping(target = "hotel", expression = "java(getHotel(request))")
    @Mapping(target = "price", expression = "java(getPrice(request))")
    @Mapping(target = "bookings", expression = "java(null)")
    public abstract Room requestToRoom(RoomUpsertRequest request);

    @Mapping(target = "id", source = "roomId")
    @Mapping(target = "hotel", expression = "java(getHotel(request))")
    @Mapping(target = "price", expression = "java(getPrice(request))")
    @Mapping(target = "bookings", expression = "java(null)")
    public abstract Room requestToRoom(Long roomId, RoomUpsertRequest request);

    @Mapping(target = "hotelId", expression = "java(room.getHotel().getId())")
    @Mapping(target = "hotelName", expression = "java(room.getHotel().getName())")
    @Mapping(target = "city", expression = "java(room.getHotel().getCity())")
    @Mapping(target = "address", expression = "java(room.getHotel().getAddress())")
    public abstract RoomResponse roomToResponse(Room room);

    public abstract List<RoomResponse> roomListToRoomResponseList(List<Room> rooms);

    @Mapping(target = "totalElements", expression = "java(roomPage.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(roomPage.getTotalPages())")
    @Mapping(target = "rooms", expression = "java(roomListToRoomResponseList(roomPage.getContent()))")
    public abstract RoomPageResponse roomPageToRoomPageResponse(Page<Room> roomPage);

    protected Hotel getHotel(RoomUpsertRequest request) {
        return request.getHotelId() == null
                ? null
                : hotelService.findById(request.getHotelId());
    }

    protected Float getPrice(RoomUpsertRequest request) {
        return request.getPrice() != null
                ? (java.lang.Math.round(request.getPrice() * 100) / 100.0F)
                : null;
    }
}
