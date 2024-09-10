
export const formDefaultValues = {
  login: {
    name: undefined,
    password: undefined,
    remember: false,
  },

  registration: {
    name: undefined,
    email: undefined,
    password: undefined,
  },

  hotels: {
    name: undefined,
    title: undefined,
    city: undefined,
    address: undefined,
    distance: undefined,
  },

  rooms: {
    name: undefined,
    description: undefined,
    number: undefined,
    price: undefined,
    guestsNumber: undefined,
    hotelId: undefined,
  },

  users: {
    name: undefined,
    email: undefined,
    password: undefined,
    roles: undefined,
  },

  bookings: {
    roomId: undefined,
    userId: undefined,
    checkInDate: undefined,
    checkOutDate: undefined,
  },

  filter: {
    pageNumber: 0,
    pageSize: 2147483647,
    id: undefined,
    hotelId: undefined,
    name: undefined,
    title: undefined,
    city: undefined,
    address: undefined,
    distance: undefined,
    rating: undefined,
    ratingsCount: undefined,
    minPrice: undefined,
    maxPrice: undefined,
    guestsNumber: undefined,
    checkInDate: undefined,
    checkOutDate: undefined,
  }
}

export const userRoleOptions = [
  { value: "User", label: "User" },
  { value: "Admin", label: "Admin" },
]
