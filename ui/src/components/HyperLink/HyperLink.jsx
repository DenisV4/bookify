import { Link } from "@tanstack/react-router"
import cn from "classnames"

import style from "./HyperLink.module.css"

export const HyperLink = ({ children, className, activeClassName = null,  ...props }) => {
  const disabledProps = props.disabled ? {tabIndex: -1, disabled: true} : {}
  
  return (
    <Link
      className={cn(className, style.link, {
        [style.disabled]: props.disabled
      })}
      activeProps={{ 
        className: cn(style.link, style.active, {
          [activeClassName]: activeClassName}
        ), 
        tabIndex: -1 }}
      {...disabledProps}
      {...props}
    >
      {children}
    </Link>
  )
}
