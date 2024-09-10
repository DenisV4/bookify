import { useRef, useState } from "react"
import cn from "classnames"
import { CSSTransition } from "react-transition-group"

import { hasRoles } from "../../../security/store"
import { useOutsideClick } from "../../../hooks/useOutsideClick"
import { useKeyDown } from "../../../hooks/useKeyDown"
import { useMediaQuery } from "../../../hooks/useMediaQuery"
import { menuContent } from "../../../helpers/menu.helper"

import { HyperLink } from "../../HyperLink/HyperLink"

import style from "./Menu.module.css"


const MenuItem = ({ item }) => {
  const ref = useRef(null)
  const matchMedia = useMediaQuery("(width <= 1024px)")

  const [isOpen, setIsOpen] = useState(false)

  useOutsideClick(ref, () => {
    if (isOpen) {
      setTimeout(() => setIsOpen(false), 300)
    }
  })

  useKeyDown("Escape", () => setIsOpen(false))

  return (
    <>
      <button
        className={style.button}
        onClick={() => setIsOpen(!isOpen)}
        disabled={!matchMedia || !item.subItems}
      >
        <div className={style.icon}>{item.icon}</div>
        <div>{item.title}</div>
      </button>
      <CSSTransition
        classNames={{ enterDone: style.enterDone }}
        nodeRef={ref}
        in={isOpen}
        timeout={0}
      >
        <ul ref={ref} className={cn("list-reset", style.submenu)}>
          {item.subItems && item.subItems.map((subItem) => (
            <li key={subItem.label}>
              <HyperLink
                to={subItem.path}
                search={subItem.search ?? subItem.search}
                activeOptions={{ exact: true }}
                disabled={["/bookings"].includes(subItem.path) && !hasRoles(["ADMIN"])}
                onClick={() => setIsOpen(false)}
              >
                <div className={style.icon}>{subItem.icon}</div>
                {subItem.label}
              </HyperLink>
            </li>
          ))}
        </ul>
      </CSSTransition>
    </>
  )
}

export const Menu = ({ className }) => {
  return (
    <menu className={cn("list-reset", className, style.menu)}>
      {menuContent && menuContent.map((item) => (
        <li key={item.title} className={style.item}>
          <MenuItem item={item} />
        </li>
      ))}
    </menu>
  )
}
