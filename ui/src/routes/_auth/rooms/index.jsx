import { createFileRoute } from '@tanstack/react-router'

import { validationSchemas } from "../../../helpers/validation.helper"

import { Table } from "../../../components/Table/Table"

export const Route = createFileRoute("/_auth/rooms/")({
  validateSearch: validationSchemas.filterSchema,
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, roomQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(roomQueryOptions.filter(deps))
  },
  component: () => <Table resource={"rooms"} />,
})
