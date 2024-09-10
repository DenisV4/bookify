import { createFileRoute } from '@tanstack/react-router'

import { validationSchemas } from "../../../helpers/validation.helper"

import { Table } from "../../../components/Table/Table"

export const Route = createFileRoute('/_auth/statistics/logins')({
  validateSearch: validationSchemas.paginationSchema,
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, loginStatQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(loginStatQueryOptions.getAll(deps))
  },
  component: () => <Table resource={"statistics/logins"} />,
})
