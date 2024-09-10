import { createFileRoute } from "@tanstack/react-router"

import { validationSchemas } from "../../../helpers/validation.helper"

import { Table } from "../../../components/Table/Table"

export const Route = createFileRoute("/_auth/hotels/")({
  validateSearch: validationSchemas.filterSchema,
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, hotelQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(hotelQueryOptions.filter(deps))
  },
  component: () => <Table resource={"hotels"} />,
})
