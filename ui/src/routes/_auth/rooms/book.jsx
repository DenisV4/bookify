import { createFileRoute } from '@tanstack/react-router'

import { validationSchemas } from "../../../helpers/validation.helper"

import { List } from "../../../components/List/List"

export const Route = createFileRoute('/_auth/rooms/book')({
  validateSearch: validationSchemas.filterSchema,
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, roomQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(roomQueryOptions.filter(deps))
  },
  component: () => <List resource={"rooms/book"} />
})
