import { defaultSearch } from "./search.helper"

import Manage from "./icons/manage.svg?react"
import Hotels from "./icons/hotels.svg?react"
import Rooms from "./icons/rooms.svg?react"
import Bookings from "./icons/bookings.svg?react"
import Users from "./icons/users.svg?react"
import Analyze from "./icons/analyze.svg?react"
import Statistics from "./icons/statistics.svg?react"
import Attempt from "./icons/attempt.svg?react"
import Api from "./icons/api.svg?react"
import Booking from "./icons/booking.svg?react"
import Rating from "./icons/rating.svg?react"

export const menuContent = [
  {
    title: "Manage",
    icon: <Manage />,
    subItems: [
      {
        label: "Hotels",
        icon: <Hotels />,
        path: "/hotels",
      },
      {
        label: "Rooms",
        icon: <Rooms />,
        path: "/rooms",
      },
      {
        label: "Bookings",
        icon: <Bookings />,
        path: "/bookings",
      },
      {
        label: "Users",
        icon: <Users />,
        path: "/users",
      },
    ]
  },
  {
    title: "Analyze",
    icon: <Analyze />,
    subItems: [
      {
        label: "Statistics",
        icon: <Statistics />,
        path: "/statistics/logins",
      },
    ]
  },
  {
    title: "Attempt",
    icon: <Attempt />,
    subItems: [
      {
        label: "API",
        icon: <Api />,
        path: "/api",
      },
      {
        label: "Booking",
        icon: <Booking />,
        path: "/rooms/book",
        search: defaultSearch.rooms,
      },
      {
        label: "Rating",
        icon: <Rating />,
        path: "/hotels/rate",
        search: defaultSearch.hotels,
      }
    ]
  },
]
