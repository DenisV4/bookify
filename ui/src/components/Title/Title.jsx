import cn from "classnames"

import style from "./Title.module.css"

export const Title = ({ className, children, type = "h1", size = "lg" }) => {
  const setStyle = (size) => {
    return cn(className, style.title, {
      [style.lg]: size === "lg",
      [style.md]: size === "md",
      [style.sm]: size === "sm"
    }
    )
  }

  return (
    <>
      {type === "h1" &&
        <h1 className={setStyle(size)}>
          {children}
        </h1>
      }
      {type === "h2" &&
        <h2 className={setStyle(size)}>
          {children}
        </h2>
      }
      {type === "h3" &&
        <h3 className={setStyle(size)}>
          {children}
        </h3>
      }
      {type === "h4" &&
        <h3 className={setStyle(size)}>
          {children}
        </h3>
      }
    </>
  )
}
