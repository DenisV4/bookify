import { useCallback, useEffect, useMemo, useState } from "react"
import { useQueryClient } from "@tanstack/react-query"
import { getRouteApi, useRouter } from "@tanstack/react-router"

import moment from "moment/moment"

import { getCurrentUser } from "../../security/store"
import { useCreateItem } from "../../hooks/mutations/useCreateItem"
import { useThrowError } from "../../hooks/useThrowError"
import { priceToDisplay, roundNumber } from "../../helpers/helpers"
import { DATE_FORMAT } from "../../helpers/calendar.helper"

import { Title } from "../Title/Title"
import { Stars } from "../Stars/Stars"
import { Button } from "../buttons/Button/Button"
import { LoadSpinner } from "../LoadSpinner/LoadSpinner"

import style from "./Booking.module.css"

export const Booking = () => {
  const queryClient = useQueryClient()

  const routeApi = getRouteApi("/_auth/bookings/create")
  const search = routeApi.useSearch()
  const router = useRouter()

  const [isFetching, setIsFetching] = useState(false)
  const { data } = routeApi.useLoaderData()

  const checkOutDate = useMemo(() => moment(search.checkOutDate), [search])
  const checkInDate = useMemo(() => moment(search.checkInDate), [search])

  const mutation = useCreateItem("bookings")

  const handleBooking = useCallback(() => {
    setIsFetching(true)
    mutation.mutate({
      roomId: search.roomId,
      userId: getCurrentUser().id,
      checkInDate: checkInDate.format(DATE_FORMAT),
      checkOutDate: checkOutDate.format(DATE_FORMAT)
    })
  }, [checkInDate, checkOutDate, mutation, search])

  useEffect(() => {
    const mutationData = mutation.data?.data

    if (mutationData) {
      queryClient.invalidateQueries({ queryKey: ["rooms"], refetchType: "all" })
      queryClient.invalidateQueries({ queryKey: ["statistics/bookings"], refetchType: "all" })
      setIsFetching(false)
      router.navigate({ to: "/bookings", search: { pageNumber: 10_000_000 } })
    }
  }, [router, mutation, queryClient])

  useThrowError(mutation.error)

  return (
    <div className={style.wrapper} >
      <div className={style.top}>
        <Title className={style.title} type="h3" size="sm">
          Booking
        </Title>
      </div>
      <div className={style.content}>
        <div className={style.top}>
          <div className={style.hotel}>
            <span className={style.caption}>Hotel:</span>
            <span>{data.hotelName}</span>
          </div>
          <Stars />
        </div>
        <div className={style.address}>
          <span className={style.caption}>Address:</span>
          <span>
            <span className={style.city}>{data.city}</span>, {data.address}
          </span>
        </div>
        <div className={style.specification}>
          <div className={style.item}>
            <span className={style.caption}>Type:</span>
            <span>{data.name}</span>
          </div>
          <div className={style.item}>
            <span className={style.caption}>Capacity:</span>
            <span>{data.guestsNumber}</span>
          </div>
        </div>
        <div className={style.user}>
          <span className={style.caption}>Guest&apos;s name:</span>
          <span>{getCurrentUser().name}</span>
        </div>
        <div className={style.dates}>
          <div className={style.item}>
            <span className={style.caption}>Check in:</span>
            <span className={style.date}>
              {checkInDate.format("DD.MM.YYYY")} 14:00
            </span>
          </div>
          <div className={style.item}>
            <span className={style.caption}>Check out:</span>
            <span className={style.date}>
              {checkOutDate.format("DD.MM.YYYY")} 10:00
            </span>
          </div>
        </div>
        <div className={style.period}>
          <span className={style.caption}>Nights:</span>
          <span>{checkOutDate.diff(checkInDate, "days")}</span>
        </div>
        <div className={style.prices}>
          <div className={style.price}>
            {priceToDisplay(data.price)}<span>per night</span>
          </div>
          <div className={style.price}>
            {priceToDisplay(roundNumber(data.price * checkOutDate.diff(checkInDate, "days"), 2))}
            <span>total</span>
          </div>
        </div>
        <div className={style.bottom}>
          <Button
            className={style.button}
            appearance="secondary"
            onClick={() => { router.navigate({ to: search.redirect || "/rooms/book" }) }}
          >
            Cancel
          </Button>
          <Button
            className={style.button}
            onClick={handleBooking}
          >
            Book
          </Button>
        </div>
        {isFetching && <LoadSpinner />}
      </div>
    </div>
  )
}
