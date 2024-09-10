import cn from "classnames"

import Spinner from "./spinner.svg?react"

import style from "./LoadSpinner.module.css"

export const LoadSpinner = ({ className, size = "lg" }) => {
  return (
    <Spinner className={cn(className, style.spinner, { [style.sm]: size === "sm" })} />
  )
}
