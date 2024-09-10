import { createFileRoute, useLoaderData } from '@tanstack/react-router'

import SwaggerUI from "swagger-ui-react"
import "swagger-ui-react/swagger-ui.css"

export const Route = createFileRoute('/_auth/api')({
  loader: async ({ context: { queryClient, apiDocQueryOptions } }) => await queryClient.ensureQueryData(apiDocQueryOptions.get()),
  component: Api,
})

function Api() {
  const { data } = useLoaderData({ from: "/_auth/api" })

  return <SwaggerUI spec={data} />
}
