import { createFileRoute } from '@tanstack/react-router'

import { validationSchemas } from "../../../helpers/validation.helper"

import { Table } from "../../../components/Table/Table"

export const Route = createFileRoute('/_auth/users/')({
  validateSearch: validationSchemas.paginationSchema,
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, userQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(userQueryOptions.getAll(deps))
  },
  component: () => <Table resource={"users"} />,
})
