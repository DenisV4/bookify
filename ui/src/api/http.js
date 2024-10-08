import axios from "axios";

export const httpClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
})

export const privateHttpClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
})
