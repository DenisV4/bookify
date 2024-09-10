import { Button } from "../Button/Button"
import EditIcon from "./edit.svg?react"

export const EditButton = ({ className, appearance = "secondary", ...props }) => {
  return (
    <Button className={className} mode="icon" appearance={appearance} {...props}>
      <EditIcon />
    </Button>
  )
}
