import { useMemo } from "react"
import { Logo } from "../Logo/Logo"

import style from "./Copyright.module.css"

export const Copyright = () => {
  const now = useMemo(() => new Date(), [])

  return (
    <div className={style.copyright}>
      <Logo size="sm" />
      <span>
        <span className={style.name}>Bookify</span> Reservation Service &copy; {now.getFullYear()}
      </span>
    </div>
  )
}
