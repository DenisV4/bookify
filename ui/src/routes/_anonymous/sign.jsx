import { createFileRoute } from "@tanstack/react-router"

import { Auth } from "../../components/Auth/Auth"

export const Route = createFileRoute("/_anonymous/sign")({
  component: Auth,
})
