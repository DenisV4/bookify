import { Input } from "../controls/Input/Input"

export const HotelFilter = ({ filters, setFilters, placeholder }) => {
  return (
    <>
      <Input
        id="title-filter"
        label="Title"
        type="text"
        debounce={500}
        placeholder={placeholder}
        value={filters.title}
        onChange={(value) => {
          const title = value ? value : ""
          setFilters({ title })
        }}
      />
      <Input
        id="city-filter"
        label="City"
        type="text"
        debounce={500}
        placeholder={placeholder}
        value={filters.city}
        onChange={(value) => {
          const city = value ? value : ""
          setFilters({ city })
        }}
      />
      <Input
        id="address-filter"
        label="Address"
        type="text"
        debounce={500}
        placeholder={placeholder}
        value={filters.address}
        onChange={(value) => {
          const address = value ? value : ""
          setFilters({ address })
        }}
      />
      <Input
        id="distance-filter"
        label="Distance"
        type="number"
        debounce={500}
        placeholder={placeholder}
        min={0.1}
        step={0.1}
        value={filters.distance}
        onChange={(value) => {
          const distance = value ? parseFloat(value) : NaN
          setFilters({ distance })
        }}
      />
      <Input
        id="rating-filter"
        label="Rating"
        type="number"
        debounce={500}
        placeholder={placeholder}
        min={0.1}
        step={0.1}
        value={filters.rating}
        onChange={(value) => {
          const rating = value ? parseFloat(value) : NaN
          setFilters({ rating })
        }}
      />
      <Input
        id="ratings-count-filter"
        label="Ratings count"
        type="number"
        debounce={500}
        placeholder={placeholder}
        min={1}
        value={filters.ratingsCount}
        onChange={(value) => {
          const ratingsCount = value ? Number(value) : NaN
          setFilters({ ratingsCount })
        }}
      />
    </>
  )
}
