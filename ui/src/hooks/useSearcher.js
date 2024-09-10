/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useMemo, useState } from "react"
import { useQueryClient } from "@tanstack/react-query"

import { useQueryOptions } from "./useQueryOptions"
import { formDefaultValues } from "../helpers/form.helper"

export const useSearcher = (searchParams, debounce) => {
  const queryClient = useQueryClient()

  const [searchQuery, setSearchQuery] = useState("")
  const [isFetching, setIsFetching] = useState(false)
  const [data, setData] = useState([])
  const [error, setError] = useState(null)

  const queryOptions = useQueryOptions(searchParams?.resource)
  const request = useMemo(() => formDefaultValues.filter, [])

  useEffect(() => {
    const load = async () => {
      try {
        request[searchParams.field] = searchQuery
        const response = await queryClient.ensureQueryData(queryOptions.filter(request))
        setData(response.data[searchParams.resource])
      } catch (error) {
        setError(error)
      } finally {
        setIsFetching(false)
      }
    }

    searchQuery && setIsFetching(true)
    const timeout = setTimeout(() => searchQuery && load(), debounce)

    return () => clearTimeout(timeout)
  }, [searchQuery])

  return { data, error, isFetching, searchQuery, setSearchQuery }
}
