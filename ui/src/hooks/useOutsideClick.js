import { useEffect } from "react"

export const useOutsideClick = (ref , callback) => {

  const handleClick = (e) => {
    if (ref.current && !ref.current.contains(e.target)) {
      callback()
    }
  }

  useEffect(() => {
    document.addEventListener("mousedown", handleClick, false)
    return () => {
      document.removeEventListener("mousedown", handleClick, false)
    }
  })
}
