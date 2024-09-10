import "./styles/normalize.css"
import "./styles/index.css"

// @ts-ignore
import React from "react"
// @ts-ignore
import ReactDOM from "react-dom/client"

import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import { RouterProvider, createRouter } from "@tanstack/react-router"
import { routeTree } from "./routeTree.gen"
import { AxiosError } from "axios"
import { disableReactDevTools } from '@fvilers/disable-react-devtools';

import { ConfirmationContextProvider } from "./context/ConfirmationContext"

import { useRefreshToken } from "./hooks/useRefreshToken"
import { useNotFoundHandler } from "./hooks/useNotFoundHandler"
import { useQueryOptions } from "./hooks/useQueryOptions"

import { LoadSpinner } from "./components/LoadSpinner/LoadSpinner"

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: (failureCount, error) => {
        if (error instanceof AxiosError && error.code === "ERR_BAD_REQUEST") {
          return false
        }
        return failureCount < 3
      },
    },
  },
})

const router = createRouter({
  routeTree,
  defaultPendingComponent: LoadSpinner,
  defaultPreloadStaleTime: 0,
  notFoundMode: "root",
  context: {
    queryClient,
  },
})

declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router
  }
}

const App = () => {
  const refreshToken = useRefreshToken()
  const notFoundHandler = useNotFoundHandler()

  const apiDocQueryOptions = useQueryOptions("api-docs")
  const hotelQueryOptions = useQueryOptions("hotels")
  const roomQueryOptions = useQueryOptions("rooms")
  const userQueryOptions = useQueryOptions("users")
  const bookingQueryOptions = useQueryOptions("bookings")
  const loginStatQueryOptions = useQueryOptions("statistics/logins")
  const registrationStatQueryOptions = useQueryOptions("statistics/registrations")
  const bookingStatQueryOptions = useQueryOptions("statistics/bookings")

  return (
    <RouterProvider
      router={router}
      context={{
        refreshToken,
        notFoundHandler,
        apiDocQueryOptions,
        hotelQueryOptions,
        roomQueryOptions,
        userQueryOptions,
        bookingQueryOptions,
        loginStatQueryOptions,
        registrationStatQueryOptions,
        bookingStatQueryOptions,
      }}
    />
  )
}



const rootElement = document.getElementById('root')!

// @ts-ignore
if (import.meta.env.PROD) {
  disableReactDevTools()
}

if (!rootElement.innerHTML) {
  const root = ReactDOM.createRoot(rootElement)
  root.render(
    <React.StrictMode>
      <QueryClientProvider client={queryClient}>
        <ConfirmationContextProvider>
          <App />
        </ConfirmationContextProvider>
      </QueryClientProvider>
    </React.StrictMode>
  )
}
