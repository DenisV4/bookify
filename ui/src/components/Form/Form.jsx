/* eslint-disable react/no-children-prop */
import cn from "classnames"

import { LoadSpinner } from "../LoadSpinner/LoadSpinner"

import style from "./Form.module.css"

export const Form = ({ children, className, submitComponent, isFetching, error, size = "lg", ...props }) => {
  return (
    <form
      className={cn(className, style.form, {
        [style.md]: size === "md",
      })}
      onSubmit={(e) => {
        e.preventDefault()
        e.stopPropagation()
      }}
      {...props}
    >
      {isFetching && <LoadSpinner />}
      {error && <div className={style.error}>Failed attempt: {error}</div>}
      {children}
      <div className={style.submit}>
        {submitComponent}
      </div>
    </form>
  )
}
