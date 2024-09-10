import { notFound } from "@tanstack/react-router"
import { AxiosError } from "axios"

export const useNotFoundHandler = () => {

  const execute = async (queryFunc, notFoundBoundaryRoute) => {
    try {
      return await queryFunc()
    } catch (error) {
      if (error instanceof AxiosError && error.code === "ERR_BAD_REQUEST") {
        throw notFound({ routeId: notFoundBoundaryRoute })
      }
      return Promise.reject(error)
    }
  }

  return { execute }
}
