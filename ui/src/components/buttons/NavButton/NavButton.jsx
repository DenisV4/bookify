import FirstIcon from "./nav-first.svg?react"
import LastIcon from "./nav-last.svg?react"
import PrevIcon from "./nav-prev.svg?react"
import NextIcon from "./nav-next.svg?react"

import { Button } from "../Button/Button"

export const NavButton = ({ target, ...props }) => {
  return (
    <>
      <Button
        mode="icon"
        appearance="secondary"
        {...props}
      >
        {target === "first" && <FirstIcon />}
        {target === "last" && <LastIcon />}
        {target === "prev" && <PrevIcon />}
        {target === "next" && <NextIcon />}
      </Button>
    </>
  )
}
