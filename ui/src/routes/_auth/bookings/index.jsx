import { createFileRoute, redirect } from "@tanstack/react-router"

import { hasRoles } from "../../../security/store"
import { validationSchemas } from "../../../helpers/validation.helper"

import { Table } from "../../../components/Table/Table"

export const Route = createFileRoute("/_auth/bookings/")({
  validateSearch: validationSchemas.paginationSchema,
  beforeLoad: () => {
    if (!hasRoles(["ADMIN"])) {
      throw redirect({ to: "/hotels" })
    }
  },
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, bookingQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(bookingQueryOptions.getAll(deps))
  },
  component: () => <Table resource={"bookings"} />,
})
