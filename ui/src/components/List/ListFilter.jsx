import moment from "moment/moment"

import { DATE_FORMAT } from "../../helpers/calendar.helper"

import { Input } from "../controls/Input/Input"
import { DateInput } from "../controls/DateInput/DateInput"

import { Button } from "../buttons/Button/Button"
import ResetIcon from "../Filter/reset.svg?react"

import style from "./List.module.css"

const PLACE_HOLDER = "Search..."

export const ListFilter = ({ target, filters, setFilters, resetFilters }) => {
  return (
    <div className={style.filter}>
      <div className={style.filterTop}>
        <span className={style.caption}>{`Search ${target.slice(0, -1)} by`}:</span>
        <Button
          appearance="secondary"
          mode="icon"
          onClick={resetFilters}
        >
          <ResetIcon />
        </Button>
      </div>
      <div className={style.filters}>
        {target === "hotels" && (
          <Input
            id="name-filter"
            label="Name"
            placeholder={PLACE_HOLDER}
            type="text"
            debounce={500}
            value={filters.name}
            onChange={(value) => {
              const name = value ? value : ""
              setFilters({ name })
            }}
          />
        )}
        <div className={style.group}>
          <Input
            id="city-filter"
            label="City"
            placeholder={PLACE_HOLDER}
            type="text"
            debounce={500}
            value={filters.city}
            onChange={(value) => {
              const city = value ? value : ""
              setFilters({ city })
            }}
          />
          {target === "hotels" && (
            <Input
              id="address-filter"
              label="Address"
              placeholder={PLACE_HOLDER}
              type="text"
              debounce={500}
              value={filters.address}
              onChange={(value) => {
                const address = value ? value : ""
                setFilters({ address })
              }}
            />
          )}
        </div>
        {target === "hotels" && (
          <div className={style.group}>
            <Input
              id="distance-filter"
              label="Distance"
              placeholder={PLACE_HOLDER}
              type="number"
              debounce={500}
              min={1}
              value={filters.distance}
              onChange={(value) => {
                const distance = value ? Number(value) : NaN
                setFilters({ distance })
              }}
            />
            <Input
              id="rating-filter"
              label="Rating"
              type="number"
              debounce={500}
              placeholder={PLACE_HOLDER}
              min={0.1}
              step={0.1}
              value={filters.rating}
              onChange={(value) => {
                const rating = value ? parseFloat(value) : NaN
                setFilters({ rating })
              }}
            />
          </div>
        )}
        {target === "rooms" && (
          <div className={style.group}>
            <Input
              id="min-price-filter"
              label="Min price"
              placeholder={PLACE_HOLDER}
              type="number"
              debounce={500}
              min={0.1}
              max={filters.maxPrice ? filters.maxPrice : Infinity}
              step={0.1}
              value={filters.minPrice}
              onChange={(value) => {
                const minPrice = value ? parseFloat(value) : NaN
                if (minPrice && filters.maxPrice && minPrice > filters.maxPrice) {
                  return setFilters({ minPrice: filters.maxPrice })
                }
                setFilters({ minPrice })
              }}
            />
            <Input
              id="max-price-filter"
              label="Max price"
              placeholder={PLACE_HOLDER}
              type="number"
              debounce={500}
              min={filters.minPrice ? filters.minPrice : 0.1}
              step={0.1}
              value={filters.maxPrice}
              onChange={(value) => {
                const maxPrice = value ? parseFloat(value) : NaN
                if (maxPrice && filters.minPrice && maxPrice < filters.minPrice) {
                  return setFilters({ maxPrice: filters.minPrice })
                }
                setFilters({ maxPrice })
              }}
            />
          </div>
        )}
        {target === "rooms" && (
          <div className={style.group}>
            <Input
              id="type-filter"
              label="Type"
              placeholder={PLACE_HOLDER}
              type="text"
              debounce={500}
              value={filters.name}
              onChange={(value) => {
                const name = value ? value : ""
                setFilters({ name })
              }}
            />
            <Input
              id="guests-number-filter"
              label="Capacity"
              placeholder={PLACE_HOLDER}
              type="number"
              debounce={500}
              min={1}
              value={filters.guestsNumber}
              onChange={(value) => {
                const guestsNumber = value ? Number(value) : NaN
                setFilters({ guestsNumber })
              }}
            />
          </div>
        )}
        {target === "rooms" && (
          <div className={style.group}>
            <DateInput
              id={"check-in-date-filter"}
              label="Check in date"
              mode="filter"
              min={moment().add(1, "days").format(DATE_FORMAT)}
              max={
                filters.checkOutDate
                  ? moment(filters.checkOutDate).subtract(1, "days").format(DATE_FORMAT)
                  : moment().add(1, "year").format(DATE_FORMAT)
              }
              selectsStart
              startDate={filters.checkInDate}
              endDate={filters.checkOutDate}
              value={filters.checkInDate}
              onChange={(value) => {
                if (moment(value).year() <= 9999) {
                  const checkInDate = moment(value).utcOffset(0, true).format(DATE_FORMAT)
                  return setFilters({ checkInDate })
                }
                setFilters({ checkInDate: filters.checkInDate })
              }}
            />
            <DateInput
              id={"check-out-date-filter"}
              label="Check out date"
              mode="filter"
              min={
                filters.checkInDate
                  ? moment(filters.checkInDate).add(1, "days").format(DATE_FORMAT)
                  : moment().add(2, "days").format(DATE_FORMAT)
              }
              max={moment(filters.checkInDate).add(1, "year").add(30, "days").format(DATE_FORMAT)}
              selectsEnd
              startDate={filters.checkInDate}
              endDate={filters.checkOutDate}
              className={"date-input"}
              value={filters.checkOutDate}
              onChange={(value) => {
                if (moment(value).year() <= 9999) {
                  const checkOutDate = moment(value).utcOffset(0, true).format(DATE_FORMAT)
                  return setFilters({ checkOutDate })
                }
                setFilters({ checkOutDate: filters.checkOutDate })
              }}
            />
          </div>
        )}
      </div>
    </div>
  )
}
