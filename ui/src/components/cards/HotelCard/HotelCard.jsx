import { useCallback, useEffect, useState } from "react"
import { useRouter } from "@tanstack/react-router"

import { CustomScroll } from "react-custom-scroll"

import { useUpdateItem } from "../../../hooks/mutations/useUpdateItem"
import { useThrowError } from "../../../hooks/useThrowError"
import { getRandomInt } from "../../../helpers/helpers"

import { Stars } from "../../Stars/Stars"
import { DummyImg } from "../../DummyImg/DummyImg"
import { Rating } from "../../controls/Rating/Rating"
import { Button } from "../../buttons/Button/Button"
import { LoadSpinner } from "../../LoadSpinner/LoadSpinner"

import style from "./HotelCard.module.css"

export const HotelCard = ({ item }) => {
  const router = useRouter()

  const [score, setScore] = useState(Math.round(item.rating))
  const [stars, setStars] = useState(0)
  const [isFetching, setIsFetching] = useState(false)

  const mutation = useUpdateItem("hotels/rate")

  const handleRating = useCallback(() => {
    setIsFetching(true)
    mutation.mutate({
      id: item.id,
      body: { score: score }
    })
  }, [item, mutation, score])

  useEffect(() => {
    setStars(getRandomInt(1, 5))
  }, [item])

  useEffect(() => {
    if (mutation.isSuccess) {
      setIsFetching(false)
      router.navigate({ to: "/hotels", search: { id: item.id } })
    }
  }, [router, mutation, item])

  useThrowError(mutation.error)

  return (
    <>
      <div className={style.top}>
        <div className={style.hotel}>
          {item.name}
        </div>
        <Stars stars={stars} />
        <div className={style.address}>
          <span>{item.city}</span>, {item.address}
        </div>
      </div>
      <DummyImg resource={"hotels"} />
      <CustomScroll>
        <div className={style.title}>{item.title}</div>
      </CustomScroll>
      <div className={style.item}>
        <span className={style.caption}>Distance:</span>
        <span>{item.distance}</span>
      </div>
      <div className={style.rating}>
        <div className={style.item}>
          <span className={style.caption}>Rating:</span>
          <span className={style.value}>
            {item.rating?.toFixed(1)}
          </span>
        </div>
        <div className={style.item}>
          <span className={style.caption}>Count:</span>
          <span>{item.ratingsCount}</span>
        </div>
      </div>
      <div className={style.bottom}>
        <Rating rating={score} isEditable setRating={setScore} />
        <Button
          size="sm"
          onClick={handleRating}
          disabled={score === Math.round(item.rating) || isFetching}
        >
          Rate
        </Button>
      </div>
      {isFetching && <LoadSpinner size="sm" />}
    </>
  )
}
