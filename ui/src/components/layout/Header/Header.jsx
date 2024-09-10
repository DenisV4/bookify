import cn from "classnames"
import { useRouter } from "@tanstack/react-router"

import { getCurrentUser } from "../../../security/store"

import { Logo } from "../../Logo/Logo"
import { Title } from "../../Title/Title"
import { Button } from "../../buttons/Button/Button"
import { UserInfo } from "../../UserInfo/UserInfo"

import style from "./Header.module.css"
import { useCallback } from "react"

export const Header = ({ className, type = "default" }) => {
  const router = useRouter()

  const handleLoginClick = useCallback(() => {
    router.navigate({ to: "/sign" })
  }, [router])
  
  return (
    <header className={cn(className, style.header, { [style.short]: type === "short" })}>
      <div className={style.logo}>
        <Logo size="lg" />
        <span>Bookify</span>
      </div>
      <Title size="md">Content Management System</Title>
      <div>
        {type === "default" && (
          getCurrentUser() ? (
            <UserInfo />
          ) : (
            <Button className={style.btn} appearance="white" onClick={handleLoginClick}>
              Login
            </Button>
          )
        )}
      </div>
    </header>
  )
}
