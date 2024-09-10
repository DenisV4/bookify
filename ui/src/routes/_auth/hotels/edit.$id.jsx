import { createFileRoute } from '@tanstack/react-router'

import { Upsert } from "../../../components/Upsert/Upsert"

export const Route = createFileRoute('/_auth/hotels/edit/$id')({
  loader: ({ context: { queryClient, notFoundHandler, hotelQueryOptions }, params }) => {
    return notFoundHandler.execute(
      () => queryClient.ensureQueryData(hotelQueryOptions.getById(params.id)),
      "__root"
    )
  },
  component: () => <Upsert resource={"hotels"} />,
})
