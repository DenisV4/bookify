import { createFileRoute } from '@tanstack/react-router'

import { validationSchemas } from "../../../helpers/validation.helper"

import { List } from "../../../components/List/List"

export const Route = createFileRoute('/_auth/hotels/rate')({
  validateSearch: validationSchemas.filterSchema,
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, hotelQueryOptions }, deps }) => {
    return await queryClient.ensureQueryData(hotelQueryOptions.filter(deps))
  },
  component: () => <List resource={"hotels/rate"} />,
})
