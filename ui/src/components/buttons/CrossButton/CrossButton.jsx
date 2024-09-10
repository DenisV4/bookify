import { Button } from "../Button/Button"
import CrossIcon from "./cross.svg?react"

export const CrossButton = ({ className, appearance = "secondary", ...props }) => {
  return (
    <Button className={className} mode="icon" appearance={appearance} {...props}>
      <CrossIcon />
    </Button>
  )
}
