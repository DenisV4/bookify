import { createFileRoute } from '@tanstack/react-router'

import { validationSchemas } from "../../../helpers/validation.helper"

import { Table } from "../../../components/Table/Table"

export const Route = createFileRoute('/_auth/statistics/registrations')({
  validateSearch: validationSchemas.paginationSchema,
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, registrationStatQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(registrationStatQueryOptions.getAll(deps))
  },
  component: () => <Table resource={"statistics/registrations"} />,
})
