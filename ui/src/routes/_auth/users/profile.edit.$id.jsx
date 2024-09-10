import { createFileRoute } from '@tanstack/react-router'

import { Upsert } from "../../../components//Upsert/Upsert"

export const Route = createFileRoute('/_auth/users/profile/edit/$id')({
  loader: ({ context: { queryClient, notFoundHandler, userQueryOptions }, params }) => {
    return notFoundHandler.execute(
      () => queryClient.ensureQueryData(userQueryOptions.getById(params.id)),
      "__root"
    )
  },
  component: () => <Upsert resource={"users/profile"} />
})
