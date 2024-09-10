import React, { Suspense } from "react"

import { Outlet, createRootRouteWithContext } from "@tanstack/react-router"
import { ReactQueryDevtools } from "@tanstack/react-query-devtools"

import { NotFound } from "../components//Error/NotFound"

const TanStackRouterDevtools = import.meta.env.PROD
  ? () => null
  : React.lazy(() => import("@tanstack/router-devtools")
    .then((res) => ({
      default: res.TanStackRouterDevtools,
    }))
  )

export const Route = createRootRouteWithContext()({
  component: () => {
    return (
      <>
        <Outlet />
        <ReactQueryDevtools initialIsOpen={false} buttonPosition="bottom-right" />
        <Suspense>
          <TanStackRouterDevtools initialIsOpen={false} position="bottom-left" />
        </Suspense>
      </>
    )
  },
  notFoundComponent: () => <NotFound type="simple" />,
})
