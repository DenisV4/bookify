/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react"

import style from "./CheckBox.module.css"

export const CheckBox = ({ id, tabIndex, label, checked, onChange, ...props }) => {
  const [isChecked, setIsChecked] = useState(checked)
  
  useEffect(() => setIsChecked(checked), [checked])

  useEffect(() => onChange(isChecked), [isChecked])

  return (
    <div className={style.field}>
      {label && <span className={style.label}>{label}</span>}
      <input
        id={id}
        className={style.checkbox}
        type={"checkbox"}
        tabIndex={tabIndex}
        checked={isChecked}
        onChange={(e) => setIsChecked(e.target.checked)}
        {...props}
      />
      <label htmlFor={id}></label>
    </div>
  )
}
