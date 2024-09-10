import { Button } from "../Button/Button"
import DeleteIcon from "./delete.svg?react"

export const DeleteButton = ({ className, appearance = "secondary", ...props }) => {
  return (
    <Button className={className} mode="icon" appearance={appearance} {...props}>
      <DeleteIcon />
    </Button>
  )
}
