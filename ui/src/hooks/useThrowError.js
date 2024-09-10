import { useEffect } from "react"

export const useThrowError = (error) => {
  return useEffect(() => {
    if (error) {
      throw error      
    }
  }, [error])
}
