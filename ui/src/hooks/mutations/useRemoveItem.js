import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useRouter } from "@tanstack/react-router"

import { usePrivateHttpClient } from "../usePrivateHttpClient"

export const useRemoveItem = (resource) => {
  const queryClient = useQueryClient()
  const router = useRouter()
  const { privateHttpClient } = usePrivateHttpClient()

  return useMutation({
    mutationFn: (id) => privateHttpClient.delete(`/${resource}/${id}`),
    onSettled: async () => {
      await queryClient.invalidateQueries({ queryKey: [resource], refetchType: "all" })
      queryClient.removeQueries({ queryKey: [resource] })
      router.invalidate()
    },
    throwOnError: true,
  })
}
