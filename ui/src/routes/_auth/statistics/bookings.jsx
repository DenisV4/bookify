import { createFileRoute } from '@tanstack/react-router'

import { validationSchemas } from "../../../helpers/validation.helper"

import { Table } from "../../../components/Table/Table"

export const Route = createFileRoute('/_auth/statistics/bookings')({
  validateSearch: validationSchemas.paginationSchema,
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, bookingStatQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(bookingStatQueryOptions.getAll(deps))
  },
  component: () => <Table resource={"statistics/bookings"} />,
})
