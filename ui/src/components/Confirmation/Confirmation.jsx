import { Button } from "../buttons/Button/Button"
import { Modal } from "../Modal/Modal"
import ConfirmIcon from "./confirm.svg?react"

import style from "./Confirmation.module.css"

export const Confirmation = ({ options, isOpen, onConfirm, onClose }) => {
  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <div className={style.wrapper}>
        <ConfirmIcon className={style.icon} />
        {<div className={style.message}>{options?.message ? options.message : "Are you sure?"}</div>}
        <div className={style.buttons}>
          <Button className={style.button} size="sm" onClick={onConfirm}>
            {options?.action ? options.action : "Confirm"}
          </Button>
          <Button className={style.button} size="sm" onClick={onClose}>
            Cancel
          </Button>
        </div>
      </div>
    </Modal>
  )
}
