import cn from "classnames"

import { RoomCard } from "../RoomCard/RoomCard"
import { HotelCard } from "../HotelCard/HotelCard"

import style from "./Card.module.css"

export const Card = ({ className, target, ...props }) => {
  return (
    <div className={cn(className, style.card)}>
      {target === "rooms" && <RoomCard {...props} />}
      {target === "hotels" && <HotelCard {...props} />}
    </div>
  )
}
