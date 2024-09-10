import cn from "classnames"

import logo from "./logo.svg"

import style from "./Logo.module.css"

export const Logo = ({ className, size }) => {
  return (
    <a className={cn(className, style.logo, {
      [style.sm]: size == "sm", 
      [style.lg]: size == "lg" 
    })}>
      <img src={logo} alt="Logo" />
    </a>
  )
}
