import cn from "classnames"

import StarIcon from "./star.svg?react"

import style from "./Stars.module.css"

export const Stars = ({ className, stars, ...props }) => {
  return (
    <div className={cn(className, style.stars)} {...props}>
      {[...Array(stars)].map((_, index) => (
        <StarIcon key={index} />
      ))}
    </div>
  )
}
