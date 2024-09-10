import { useEffect, useState } from "react"
import { useLocation, useRouter } from "@tanstack/react-router"

import { CustomScroll } from "react-custom-scroll"

import { getRandomInt, priceToDisplay } from "../../../helpers/helpers"

import { Stars } from "../../Stars/Stars"
import { DummyImg } from "../../DummyImg/DummyImg"
import { Button } from "../../buttons/Button/Button"

import style from "./RoomCard.module.css"

export const RoomCard = ({ item, filters }) => {
  const router = useRouter()
  const location = useLocation()

  const [stars, setStars] = useState(0)

  useEffect(() => {
    setStars(getRandomInt(1, 5))
  }, [item])

  return (
    <>
      <div className={style.top}>
        <div className={style.hotel}>
          {item.hotelName}
        </div>
        <Stars stars={stars} />
        <div className={style.address}>
          <span>{item.city}</span>, {item.address}
        </div>
      </div>
      <DummyImg resource={"rooms"} />
      <CustomScroll>
        <div className={style.description}>
          {item.description}
        </div>
      </CustomScroll>
      <div className={style.specification}>
        <div className={style.item}>
          <span className={style.caption}>Type:</span>
          <span>{item.name}</span>
        </div>
        <div className={style.item}>
          <span className={style.caption}>Capacity:</span>
          <span>{item.guestsNumber}</span>
        </div>
      </div>
      <div className={style.bottom}>
        <div className={style.price}>
          {priceToDisplay(item.price)}<span>per night</span>
        </div>
        <Button
          size="sm"
          onClick={() => {
            router.navigate({
              to: "/bookings/create",
              search: {
                redirect: location.href,
                roomId: item.id,
                checkInDate: filters.checkInDate,
                checkOutDate: filters.checkOutDate,
              },
            })
          }}
        >
          Book
        </Button>
      </div>
    </>
  )
}
