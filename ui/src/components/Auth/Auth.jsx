import { useState } from "react"

import { Title } from "../Title/Title"
import { LoginForm } from "./LoginForm"
import { RegistrationForm } from "./RegistrationForm"

import style from "./Auth.module.css"

export const Auth = () => {
  const [isChecked, setIsChecked] = useState(false)

  return (
    <div className={style.wrapper}>
      <div className={style.auth}>
        <Title className={style.title} type="h2" size="sm">
          <span>Log In </span><span>Sign Up</span>
        </Title>
        <input
          id="log-reg"
          className={style.checkbox}
          type="checkbox"
          checked={isChecked}
          name="log-reg"
          onChange={() => setIsChecked(!isChecked)}
        />
        <label htmlFor="log-reg"></label>
        <div className={style.card3d}>
          <div className={style.cardWrapper}>
            <div className={style.front}>
              <LoginForm isActive={!isChecked} />
            </div>
            <div className={style.back}>
              <RegistrationForm isActive={isChecked} onSuccess={() => setIsChecked(false)} />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
