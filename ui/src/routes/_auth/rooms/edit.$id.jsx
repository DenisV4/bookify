import { createFileRoute } from '@tanstack/react-router'

import { Upsert } from "../../../components//Upsert/Upsert"

export const Route = createFileRoute('/_auth/rooms/edit/$id')({
  loader: ({ context: { queryClient, notFoundHandler, roomQueryOptions }, params }) => {
    return notFoundHandler.execute(
      () => queryClient.ensureQueryData(roomQueryOptions.getById(params.id)),
      "__root"
    )
  },
  component: () => <Upsert resource={"rooms"} />,
})
