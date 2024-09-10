import { useCallback, useRef, useState } from "react"
import { CSSTransition } from "react-transition-group"
import { useRouter } from "@tanstack/react-router"

import { getCurrentUser } from "../../security/store"
import { useOutsideClick } from "../../hooks/useOutsideClick"
import { useKeyDown } from "../../hooks/useKeyDown"
import { useAuth } from "../../hooks/useAuth"

import { roleToDisplay } from "../../helpers/helpers"

import { Button } from "../buttons/Button/Button"
import UserIcon from "./user-circle.svg?react"

import style from "./UserInfo.module.css"

export const UserInfo = () => {
  const infoRef = useRef(null)
  const [isOpen, setIsOpen] = useState(false)
  const { logout } = useAuth()

  const router = useRouter()

  const handleLogout = useCallback(async () => {
    await logout()
    setIsOpen(false)
    await router.navigate({ to: "/" })
  }, [logout, router])

  useOutsideClick(infoRef, () => {
    if (isOpen) {
      setTimeout(() => setIsOpen(false), 300)
    }
  })

  useKeyDown("Escape", () => setIsOpen(false))

  return (
    <>
      {getCurrentUser() && (
        <div className={style.profile}>
          <span className={style.name}>{getCurrentUser().name}</span>
          <Button mode="icon" appearance="white" size="lg" onClick={() => setIsOpen(!isOpen)}>
            <UserIcon />
          </Button>
          <CSSTransition
            classNames={{ enterDone: style.enterDone }}
            nodeRef={infoRef}
            in={isOpen}
            timeout={0}
          >
            <div className={style.info} ref={infoRef} >
              <div className={style.wrapper}>
                <span className={style.label}>Name:</span>
                <span>{getCurrentUser().name}</span>
                <span className={style.label}>Email:</span>
                <span>{getCurrentUser().email}</span>
                <span className={style.label}>Roles:</span>
                <span>{getCurrentUser().roles.map(role => roleToDisplay(role)).join(", ")}</span>
              </div>
              <div className={style.bottom}>
                <Button
                  className={style.button}
                  size="sm"
                  onClick={() => {
                    setIsOpen(false)
                    router.navigate({
                      to: "/users/profile/edit/$id",
                      params: { id: getCurrentUser().id },
                      search: { redirect: location.pathname + location.search },
                    })
                  }}
                >
                  Edit profile
                </Button>
                <Button
                  className={style.button}
                  size="sm"
                  appearance="secondary"
                  onClick={handleLogout}
                >
                  Logout
                </Button>
              </div>
            </div>
          </CSSTransition>
        </div>
      )}
    </>
  )
}
