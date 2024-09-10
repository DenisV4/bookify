import { useCallback, useEffect, useRef } from "react"

import { useScrollY } from "../../hooks/useScrollY"

import { UpButton } from "../buttons/UpButton/UpButton"

import style from "./Up.module.css"

export const Up = ({ position = {} }) => {
  const ref = useRef(null)
  const y = useScrollY()

  useEffect(() => {
    if (ref.current) {
      ref.current.style.opacity = 2 * y / document.body.scrollHeight
    }
  }, [y, ref])

  const scrollToTop = useCallback(() => {
    window.scrollTo({ top: 0, behavior: "smooth" })
  }, [])

  return (
    <span
      ref={ref}
      className={style.up}
      style={{ opacity: 0, ...position }}
    >
      <UpButton onClick={scrollToTop} />
    </span >
  )
}
