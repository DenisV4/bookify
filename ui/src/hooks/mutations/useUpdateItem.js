import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useRouter } from "@tanstack/react-router"

import { usePrivateHttpClient } from "../usePrivateHttpClient"

export const useUpdateItem = (resource) => {
  const queryClient = useQueryClient()
  const router = useRouter()
  const { privateHttpClient } = usePrivateHttpClient()

  const key = resource.split("/")[0]

  return useMutation({
    mutationFn: ({ id, body }) => privateHttpClient.put(`/${resource}/${id}`, body),
    onSettled: async () => {
      await queryClient.invalidateQueries({ queryKey: [key], refetchType: "all" })
      router.invalidate()
    },
    throwOnError: true,
  })
}
