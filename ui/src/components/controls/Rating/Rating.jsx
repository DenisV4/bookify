/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useRef, useState } from "react"
import cn from "classnames"

import StarIcon from "./star.svg?react"

import style from "./Rating.module.css"

export const Rating = ({ className, rating, setRating, isEditable = false, tabIndex, ...props }) => {
  const [ratingArray, setRatingArray] = useState(new Array(5).fill(<></>))
  const ratingRef = useRef([])

  useEffect(() => { constructRating(rating) }, [rating, tabIndex])

  const changeDisplay = (index) => {
    if (!isEditable) {
      return
    }
    constructRating(index)
  }

  const handleClick = (index) => {
    if (!isEditable || !setRating) {
      return
    }
    setRating(index)
  }

  const handleKeyDown = (e) => {
    if (!isEditable || !setRating) {
      return
    }
    if (e.key === "ArrowUp" || e.key === "ArrowRight") {
      if (!rating) {
        setRating(1)
      } else {
        e.preventDefault()
        setRating(rating < 5 ? rating + 1 : 5)
      }
      ratingRef.current[rating]?.focus()
    }
    if (e.key === "ArrowDown" || e.key === "ArrowLeft") {
      e.preventDefault()
      setRating(rating > 1 ? rating - 1 : 1)
      ratingRef.current[rating - 1]?.focus()
    }
  }

  const computeTabIndex = (rating, index) => {
    if (!isEditable) {
      return -1
    }
    return index === 0 ? tabIndex ?? 0 : -1
  }

  const constructRating = (currentRating) => {
    const updatedRatingArray = ratingArray.map((item, index) => {
      return (
        <span
          key={index}
          className={cn(className, style.star, {
            [style.colored]: index < currentRating,
            [style.editable]: isEditable,
          })}
          ref={r => ratingRef.current?.push(r)}
          onMouseEnter={() => changeDisplay(index + 1)}
          onMouseLeave={() => changeDisplay(rating)}
          onClick={() => handleClick(index + 1)}
          tabIndex={computeTabIndex(rating, index)}
          onKeyDown={handleKeyDown}
        >
          <StarIcon />
        </span>
      )
    })

    setRatingArray(updatedRatingArray)
  }

  return (
    <div className={cn(className, style.wrapper)} {...props}>
      {ratingArray.map((item, index) => (<span key={index}>{item}</span>))}
    </div>
  )
}
