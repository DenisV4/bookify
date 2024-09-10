package example.bookify.server.repository.specification;

import example.bookify.server.model.Booking;
import example.bookify.server.model.Hotel;
import example.bookify.server.model.Room;
import example.bookify.server.web.dto.request.filter.RoomFilter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public interface RoomSpecification {

    static Specification<Room> withFilter(RoomFilter filter) {

        return Specification
                .where(byId(filter.getId()))
                .and(byName(filter.getName()))
                .and(byPrice(filter.getMinPrice(), filter.getMaxPrice()))
                .and(byGuestsNumber(filter.getGuestsNumber()))
                .and(byDates(filter.getCheckInDate(), filter.getCheckOutDate()))
                .and(byHotelId(filter.getHotelId()))
                .and(byCity(filter.getCity()));
    }

    static Specification<Room> byId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Room.Fields.id), id);
        });
    }

    static Specification<Room> byName(String name) {
        return ((root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }
            return criteriaBuilder.like(root.get(Room.Fields.name), "%" + name + "%");
        });
    }

    static Specification<Room> byPrice(Float minPrice, Float maxPrice) {
        return ((root, query, criteriaBuilder) -> {
            if (maxPrice == null && minPrice == null) {
                return null;
            }
            if (maxPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(Room.Fields.price), minPrice);
            }
            if (minPrice == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(Room.Fields.price), maxPrice);
            }
            return criteriaBuilder.between(root.get(Room.Fields.price), minPrice, maxPrice);
        });
    }

    static Specification<Room> byGuestsNumber(Integer guestsNumber) {
        return ((root, query, criteriaBuilder) -> {
            if (guestsNumber == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Room.Fields.guestsNumber), guestsNumber);
        });
    }

    static Specification<Room> byDates(LocalDate checkInDate, LocalDate checkOutDate) {
        return ((root, query, criteriaBuilder) -> {
            if (checkInDate == null || checkOutDate == null) {
                return null;
            }

            var subquery = query.subquery(Long.class);
            var bookingRoot = subquery.from(Booking.class);

            subquery.select(bookingRoot.get(Booking.Fields.room).get(Room.Fields.id));
            var overlapPredicate = criteriaBuilder.or(
                    criteriaBuilder.and(
                            criteriaBuilder
                                    .lessThanOrEqualTo(bookingRoot.get(Booking.Fields.checkInDate), checkInDate),
                            criteriaBuilder
                                    .greaterThanOrEqualTo(bookingRoot.get(Booking.Fields.checkOutDate), checkOutDate)
                    ),
                    criteriaBuilder.and(
                            criteriaBuilder
                                    .greaterThanOrEqualTo(bookingRoot.get(Booking.Fields.checkInDate), checkInDate),
                            criteriaBuilder
                                    .lessThan(bookingRoot.get(Booking.Fields.checkInDate), checkOutDate)
                    ),
                    criteriaBuilder.and(
                            criteriaBuilder
                                    .lessThanOrEqualTo(bookingRoot.get(Booking.Fields.checkOutDate), checkOutDate),
                            criteriaBuilder
                                    .greaterThan(bookingRoot.get(Booking.Fields.checkOutDate), checkInDate)
                    ));

            return criteriaBuilder.not(root.get(Room.Fields.id).in(subquery.where(overlapPredicate)));
        });
    }

    static Specification<Room> byHotelId(Long hotelId) {
        return ((root, query, criteriaBuilder) -> {
            if (hotelId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Room.Fields.hotel).get(Hotel.Fields.id), hotelId);
        });
    }

    static Specification<Room> byCity(String city) {
        return ((root, query, criteriaBuilder) -> {
            if (city == null) {
                return null;
            }
            return criteriaBuilder.like(root.get(Room.Fields.hotel).get(Hotel.Fields.city), "%" + city + "%");
        });
    }
}
