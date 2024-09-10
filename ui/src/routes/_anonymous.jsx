import { createFileRoute, Outlet, redirect } from '@tanstack/react-router'
import { z } from "zod"

import { hasAuth, isStored } from "../security/store"

import { Error } from "../components/Error/Error"
import { Layout} from "../components/layout/Layout"

export const Route = createFileRoute('/_anonymous')({
  validateSearch: z.object({
    redirect: z.string().optional().catch(''),
  }),
  beforeLoad: ({ search }) => {
    if (isStored() || hasAuth()) {
      throw redirect({ to: search.redirect || "/hotels" })
    }
  },
  errorComponent: ({ error, reset }) => <Error code={error.code} reset={reset} />,
  component: LayoutA,
})

function LayoutA() {
  return (
    <Layout type="simple">
      <Outlet />
    </Layout>
  )
}
