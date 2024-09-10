import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useRouter } from "@tanstack/react-router"

import { usePrivateHttpClient } from "../usePrivateHttpClient"

export const useCreateItem = (resource) => {
  const queryClient = useQueryClient()
  const router = useRouter()
  const { privateHttpClient } = usePrivateHttpClient()

  return useMutation({
    mutationFn: (insertRequest) => privateHttpClient.post(`/${resource}`, insertRequest),
    onSettled: async () => {
      await queryClient.invalidateQueries({ queryKey: [resource], refetchType: "all" })
      router.invalidate()
    },
    throwOnError: true,
  })
}
