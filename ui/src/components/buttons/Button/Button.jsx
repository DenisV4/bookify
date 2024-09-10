import cn from "classnames";

import style from './Button.module.css'

export const Button = ({
  children,
  className,
  mode = "standard",
  appearance = "primary",
  size = "md",
  disabled,
  ...props
}) => {
  return (
    <button className={cn(style.btn, className,
      {
        [style.standard]: mode === "standard",
        [style.icon]: mode === "icon",
        [style.primary]: appearance === "primary",
        [style.secondary]: appearance === "secondary",
        [style.white]: appearance === "white",
        [style.large]: size === "lg",
        [style.small]: size === "sm",
        [style.disabled]: disabled
      })}
      disabled={disabled}
      {...props}
    >
      <div className={style.wrapper}>{children}</div>
    </button>
  )
}
