import { useEffect } from "react"

import { useRefreshToken } from "./useRefreshToken"
import { privateHttpClient } from "../api/http"
import { getAuth, hasAuth } from "../security/store"

export const usePrivateHttpClient = () => {
    const refresh = useRefreshToken()

    useEffect(() => {
        const requestInterceptor = privateHttpClient.interceptors.request.use((config) => {
            const accessToken = hasAuth() ? getAuth().accessToken : null
            if (!config.headers.Authorization && accessToken) {
                config.headers.Authorization = `Bearer ${accessToken}`
            }

            return config
        }, (error) => Promise.reject(error))

        const responseInterceptor = privateHttpClient.interceptors.response.use((config) => config,
            async (error) => {
                const originalRequest = error?.config
                if (error.response?.status === 401 && error.config && !error.config._isRetry) {
                    originalRequest._isRetry = true

                    const newAccessToken = await refresh();
                    if (newAccessToken) {
                        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`

                        return privateHttpClient(originalRequest)
                    }
                }

                return Promise.reject(error)
            }
        )

        return () => {
            privateHttpClient.interceptors.request.eject(requestInterceptor)
            privateHttpClient.interceptors.response.eject(responseInterceptor)
        }
    }, [refresh])

    return { privateHttpClient }
}
