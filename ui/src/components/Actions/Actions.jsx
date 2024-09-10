import { DeleteButton } from "../buttons/DeleteButton/DeleteButton"
import { EditButton } from "../buttons/EditButton/EditButton"

import style from './Actions.module.css'

export const Actions = ({ onEdit, onDelete, ...props }) => {
  return (
    <div className={style.actions}>
      <EditButton disabled={props.disabled} onClick={onEdit} {...props} />
      <DeleteButton disabled={props.disabled} onClick={onDelete} {...props} />
    </div>
  )
}
