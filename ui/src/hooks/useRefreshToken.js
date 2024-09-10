
import { privateHttpClient } from "../api/http"
import { setAuth, removeAuth } from "../security/store"

export const useRefreshToken = () => {

  const refresh = async () => {

    try {
      const response = await privateHttpClient.post("/auth/refresh", { withCredentials: true })

      setAuth(response.data)
      return response.data.accessToken
    } catch (error) {
      removeAuth()
      return Promise.reject(error)
    }
  }

  return refresh
}
