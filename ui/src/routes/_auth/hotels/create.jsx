import { createFileRoute } from '@tanstack/react-router'

import { Upsert } from "../../../components//Upsert/Upsert"

export const Route = createFileRoute('/_auth/hotels/create')({
  component: () => <Upsert resource={"hotels"} />,
})
