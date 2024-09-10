import cn from "classnames"

import { Copyright } from "../../Copyright/Copyright"

import style from "./Footer.module.css"

export const Footer = ({ className }) => {
  return (
    <footer  className={cn(className, style.footer)}>
      <Copyright />
    </footer>
  )
}
