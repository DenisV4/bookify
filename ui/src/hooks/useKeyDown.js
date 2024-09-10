import { useEffect } from "react"

export const useKeyDown = (keyName, callback) => {
  const onKeyDown = (e) => {
    if (e.key === keyName) {
      callback()
    }
  }

  useEffect(() => {
    document.addEventListener("keydown", onKeyDown, false)

    return () => {
      document.removeEventListener("keydown", onKeyDown, false)
    }
  })
}
