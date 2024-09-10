import { Button } from "../Button/Button"
import UpIcon from "./up.svg?react"

export const UpButton = ({ className, appearance = "secondary", ...props }) => {
  return (
    <Button className={className} mode="icon" appearance={appearance} {...props}>
      <UpIcon />
    </Button>
  )
}
