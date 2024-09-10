import { queryOptions } from "@tanstack/react-query"
import { usePrivateHttpClient } from "./usePrivateHttpClient"

export const useQueryOptions = (resource) => {
  const { privateHttpClient } = usePrivateHttpClient()

  const get = () => queryOptions({
    queryKey: [resource],
    queryFn: () => privateHttpClient.get(`/${resource}`),
  })

  const getAll = (pagination) => queryOptions({
    queryKey: [resource, pagination],
    queryFn: () => privateHttpClient.get(`/${resource}`, { params: pagination }),
  })

  const getById = (id) => queryOptions({
    queryKey: [resource, id],
    queryFn: () => privateHttpClient.get(`/${resource}/${id}`),
  })

  const filter = (filter) => queryOptions({
    queryKey: [resource, filter],
    queryFn: () => privateHttpClient.get(`/${resource}/filter`, { params: filter }),
  })

  const validate = (id, request) => queryOptions({
    queryKey: [`${resource}-validate`, id],
    queryFn: async () => privateHttpClient.get(`/${resource}/validate/${id}`, { params: request })
  })

  const download = () => queryOptions({
    queryKey: [`${resource}-download`],
    queryFn: () => privateHttpClient.get(`/${resource}/download`, { responseType: "blob" })
  })

  return { get, getAll, getById, filter, validate, download }
}
