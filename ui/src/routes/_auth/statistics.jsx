import { createFileRoute, Outlet } from '@tanstack/react-router'

import { Statistics } from "../../components/Statistics/Statistics"

export const Route = createFileRoute('/_auth/statistics')({
  component: StatisticsWrapper,
})

function StatisticsWrapper() {
  return (
    <Statistics>
      <Outlet />
    </Statistics>
  )
}
