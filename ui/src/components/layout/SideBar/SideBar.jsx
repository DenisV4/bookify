import cn from "classnames"

import { Menu } from "../Menu/Menu"

import style from "./SideBar.module.css"

export const SideBar = ({ className }) => {
  return (
    <div className={cn(className, style.sidebar)}>
      <Menu />
    </div>
  )
}
