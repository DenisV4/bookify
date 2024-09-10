import { createFileRoute } from '@tanstack/react-router'

import { Booking } from "../../../components/Booking/Booking"

export const Route = createFileRoute('/_auth/bookings/create')({
  loaderDeps: ({ search }) => ({ ...search }),
  loader: async ({ context: { queryClient, notFoundHandler, roomQueryOptions }, deps }) => {
    return notFoundHandler.execute(
      () => queryClient.ensureQueryData(roomQueryOptions.getById(deps.roomId)),
      "/__root"
    )
  },
  component: () => <Booking />,
})
