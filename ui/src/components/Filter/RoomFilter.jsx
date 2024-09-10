import moment from "moment/moment"

import { DATE_FORMAT } from "../../helpers/calendar.helper"

import { Input } from "../controls/Input/Input"
import { DateInput } from "../controls/DateInput/DateInput"

export const RoomFilter = ({ filters, setFilters, placeholder }) => {
  return (
    <>
      <Input
        id="min-price-filter"
        label="Min price"
        type="number"
        debounce={500}
        placeholder={placeholder}
        min={0.1}
        max={filters.maxPrice ? filters.maxPrice : Infinity}
        step={0.1}
        value={filters.minPrice}
        onChange={(value) => {
          const minPrice = value ? parseFloat(value) : NaN
          if (minPrice && filters.maxPrice && minPrice > filters.maxPrice) {
            return setFilters( {minPrice: filters.maxPrice})
          }
          setFilters({ minPrice })
        }}
      />
      <Input
        id="max-price-filter"
        label="Max price"
        type="number"
        debounce={500}
        placeholder={placeholder}
        min={filters.minPrice ? filters.minPrice : 0.1}
        step={0.1}
        value={filters.maxPrice}
        onChange={(value) => {
          const maxPrice = value ? parseFloat(value) : NaN
          if (maxPrice && filters.minPrice && maxPrice < filters.minPrice) {
            return setFilters( {maxPrice: filters.minPrice})
          }
          setFilters({ maxPrice })
        }}
      />
      <Input
        id="guests-number-filter"
        label="Guests number"
        type="number"
        debounce={500}
        placeholder={placeholder}
        min={1}
        value={filters.guestsNumber}
        onChange={(value) => {
          const guestsNumber = value ? Number(value) : NaN
          setFilters({ guestsNumber })
        }}
      />
      <DateInput
        id={"check-in-date-filter"}
        label="Check in date"
        mode="filter"
        max={filters.checkOutDate ? moment(filters.checkOutDate).subtract(1, "days").format(DATE_FORMAT) : null}
        value={filters.checkInDate}
        onChange={(value) => {
          if (moment(value).year() <= 9999) {
            const checkInDate = moment(value).utcOffset(0, true).format(DATE_FORMAT)
            return setFilters({ checkInDate })
          }
          setFilters({ checkInDate: value ? filters.checkInDate : "" })
        }}
      />
      <DateInput
        id={"check-out-date-filter"}
        label="Check out date"
        mode="filter"
        min={filters.checkInDate ? moment(filters.checkInDate).add(1, "days").format(DATE_FORMAT) : null}
        value={filters.checkOutDate}
        onChange={(value) => {
          if (moment(value).year() <= 9999) {
            const checkOutDate = moment(value).utcOffset(0, true).format(DATE_FORMAT)
            return setFilters({ checkOutDate })
          }
          setFilters({ checkOutDate: value ? filters.checkOutDate : "" })
        }}
      />
      <Input
        id="hotel_id-filter"
        label="Hotel id"
        type="number"
        debounce={500}
        placeholder={placeholder}
        min={1}
        value={filters.hotelId}
        onChange={(value) => {
          const hotelId = value ? Number(value) : NaN
          setFilters({ hotelId })
        }}
      />
    </>
  )
}
