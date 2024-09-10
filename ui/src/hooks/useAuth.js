import { useState } from "react"

import { httpClient } from "../api/http"
import { removeAuth, setAuth, setStored } from "../security/store"

export const useAuth = () => {
  const PATH = "/auth"

  const [isFetching, setIsFetching] = useState(false)

  const login = async (name, password, remember) => {
    try {
      setIsFetching(true)
      const response = await httpClient.post(`${PATH}/login`, { name, password }, { withCredentials: true })

      setAuth(response.data)
      setStored(remember)
    } catch (error) {
      return Promise.reject(error)
    } finally {
      setIsFetching(false)
    }
  }

  const logout = async () => {
    try {
      setIsFetching(true)
      await httpClient.post(`${PATH}/logout`, { withCredentials: true })

      removeAuth()
    } catch (error) {
      return Promise.reject(error)
    } finally {
      setIsFetching(false)
    }
  }

  const register = async (name, email, password) => {
    try {
      setIsFetching(true)
      await httpClient.post(`${PATH}/register`, { name, email, password })
    } catch (error) {
      return Promise.reject(error)
    } finally {
      setIsFetching(false)
    }
  }

  const validateName = async (name) => {
    const response = await httpClient.get(`${PATH}/validate-name?name=${name}`)
    return response.data ? undefined : "User with this name already exists"
  }

  const validateEmail = async (email) => {
    const response = await httpClient.get(`${PATH}/validate-email?email=${email}`)
    return response.data ? undefined : "User with this email already exists"
  }

  return { login, register, logout, validateName, validateEmail, isFetching }
}
