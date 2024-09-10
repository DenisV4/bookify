import { create } from "zustand";
import { jwtDecode } from "jwt-decode"

export const KEY = "bookify.auth"

export const authStore = create(() => ({
  auth: null,
}))

export const getAuth = () => authStore.getState().auth

export const getCurrentUser = () => {
  const accessToken = authStore.getState().auth?.accessToken
  return accessToken ? JSON.parse(jwtDecode(accessToken).sub) : null
}

export const setAuth = (value) => authStore.setState({auth: value})

export const setStored = (value) => localStorage.setItem(KEY, value)

export const removeAuth = () => {
  authStore.setState({ auth: null })
  localStorage.removeItem(KEY)
}

export const isStored = () => localStorage.getItem(KEY) === "true"

export const hasAuth = () => !!authStore.getState().auth

export const hasRoles = (value) => {
  const accessToken = authStore.getState().auth?.accessToken
  const roles = accessToken ? JSON.parse(jwtDecode(accessToken).sub).roles : null
  
  if (!roles) {
    return false
  }
  const intersection = value.filter((role) => roles.includes(`ROLE_${role}`))

  return intersection.length === value.length
}
