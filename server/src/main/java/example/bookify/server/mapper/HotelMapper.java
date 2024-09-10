package example.bookify.server.mapper;

import example.bookify.server.model.Hotel;
import example.bookify.server.web.dto.request.HotelUpsertRequest;
import example.bookify.server.web.dto.response.HotelResponse;
import example.bookify.server.web.dto.response.HotelPageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    @Mapping(target = "distance", expression = "java(getDistance(request))")
    @Mapping(target = "rooms", expression = "java(null)")
    Hotel requestToHotel(HotelUpsertRequest request);

    @Mapping(target = "id", source = "hotelId")
    @Mapping(target = "distance", expression = "java(getDistance(request))")
    @Mapping(target = "rooms", expression = "java(null)")
    @Mapping(target = "rating", expression = "java(null)")
    @Mapping(target = "ratingsCount", expression = "java(null)")
    Hotel requestToHotel(Long hotelId, HotelUpsertRequest request);

    HotelResponse hotelToResponse(Hotel hotel);

    List<HotelResponse> hotelListToHotelResponseList(List<Hotel> hotels);

    @Mapping(target = "totalElements", expression = "java(hotelPage.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(hotelPage.getTotalPages())")
    @Mapping(target = "hotels", expression = "java(hotelListToHotelResponseList(hotelPage.getContent()))")
    HotelPageResponse hotelPageToHotelPageResponse(Page<Hotel> hotelPage);

    default Float getDistance(HotelUpsertRequest request) {
        return request.getDistance() != null
                ? (java.lang.Math.round(request.getDistance() * 100) / 100.0F)
                : null;
    }
}
