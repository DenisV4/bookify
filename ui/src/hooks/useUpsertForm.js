import { useForm } from "@tanstack/react-form"
import { getRouteApi, useParams, useRouter } from "@tanstack/react-router"
import { useEffect, useState } from "react"
import { zodValidator } from "@tanstack/zod-form-adapter"

import { useCreateItem } from "./mutations/useCreateItem"
import { useUpdateItem } from "./mutations/useUpdateItem"
import { useThrowError } from "./useThrowError"

import { formDefaultValues } from "../helpers/form.helper"
import { validationSchemas } from "../helpers/validation.helper"
import { displayToRoles } from "../helpers/helpers"

export const useUpsertForm = (resource, search) => {
  const params = useParams({ strict: false })
  const router = useRouter()
  const routeApi = getRouteApi(`/_auth/${resource}/${params?.id ? "edit/$id" : "create"}`)

  const [isFetching, setIsFetching] = useState(false)

  const data = routeApi.useLoaderData()

  const target = resource.split("/")[0]
  const resourceHasFilter = ["hotels", "rooms"].includes(target)

  const createItem = useCreateItem(target)
  const updateItem = useUpdateItem(target)

  const mutation = data ? updateItem : createItem

  useEffect(() => {
    const mutationData = mutation.data?.data

    if (mutationData) {
      setIsFetching(false)
      router.navigate({
        to: resourceHasFilter ? `/${resource}`: search.redirect || `/${resource}`,
        search: resourceHasFilter ? { id: mutationData.id } : {}
      })
    }
  }, [router, mutation, resource, search, resourceHasFilter])

  useThrowError(mutation.error)

  const form = useForm({
    defaultValues: formDefaultValues[target],
    validatorAdapter: zodValidator,
    validators: {
      onSubmit: validationSchemas[`${target.slice(0, -1)}Schema`].partial().refine(
        (value) => {
          return Object.keys(value).filter((key) => value[key] !== undefined).length > 0
        },
        {
          message: "One of the fields is required",
          path: [],
        }
      ),
    },
    onSubmit: async ({ value }) => {
      const formData = JSON.parse(JSON.stringify(value))
      if (formData.roles) {
        formData.roles = displayToRoles(formData.roles)
      }
      const args = data ? { id: data.data?.id, body: { ...formData } } : { ...formData }
      setIsFetching(true)
      mutation.mutate(args)
      // form.reset()
    },
  })

  return { form, isFetching, data: data?.data }
}
