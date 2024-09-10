import { Navigate, Outlet, createFileRoute, redirect, useLocation, useRouter } from '@tanstack/react-router'
import { useEffect } from "react"
import { AxiosError } from "axios"

import { hasAuth, isStored, KEY, removeAuth } from "../security/store"

import { Layout } from "../components/layout/Layout"
import { Error } from "../components/Error/Error"

export const Route = createFileRoute('/_auth')({
  beforeLoad: ({ context: { refreshToken }, location }) => {
    if (!hasAuth()) {
      if (!isStored()) {
        throw redirect({
          to: "/sign", search: { redirect: location.href, }
        })
      }

      refreshToken()
    }
  },
  errorComponent: ({ error, reset }) => {
    if (error instanceof AxiosError && error.response?.status === 403) {
      return <RefreshTokenError reset={reset} />
    }

    return <Error code={error.code} reset={reset} />
  },
  component: LayoutB,
})

function RefreshTokenError({ reset }) {
  const router = useRouter()
  const location = useLocation()

  useEffect(() => {
    reset()
    router.invalidate()
  })

  return <Navigate to="/sign" search={{ redirect: location.href, }} />
}

function LayoutB() {
  const router = useRouter()

  const handleStorageChange = (e) => {
    if (e.key === KEY && e.newValue === null && e.oldValue === "true") {
      removeAuth()
      router.invalidate()
    }
  }

  useEffect(() => {
    window.addEventListener("storage", handleStorageChange)
    return () => window.removeEventListener("storage", handleStorageChange)
  })

  return (
    <Layout>
      <Outlet />
    </Layout>
  )
}
