import { useCallback, useState } from "react"
import { useQueryClient } from "@tanstack/react-query"

import { useQueryOptions } from "../../hooks/useQueryOptions"
import { useThrowError } from "../../hooks/useThrowError"

import { Button } from "../buttons/Button/Button"
import { LoadSpinner } from "../LoadSpinner/LoadSpinner"

export const DownloadLink = ({ children, className, resource, filename, ...props }) => {
  const queryClient = useQueryClient()
  const queryOptions = useQueryOptions(resource)

  const [isFetching, setIsFetching] = useState(false)
  const [error, setError] = useState(false)

  const download = useCallback(async () => {
    setIsFetching(true)
    try {
      const response = await queryClient.fetchQuery(queryOptions.download())
      const url = window.URL.createObjectURL(response.data)
      const link = document.createElement("a")

      link.href = url
      link.download = filename
        ? filename
        : response.headers["content-disposition"].split("filename=")[1].replace(/"/g, "")

      document.body.appendChild(link)
      link.click()

      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    } catch (error) {
      setError(error)
    } finally {
      setIsFetching(false)
    }
  }, [queryClient, queryOptions, filename])

  useThrowError(error)

  return (
    <div style={{ position: "relative" }}>
      <Button
        className={className}
        onClick={download}
        disabled={props.disabled || isFetching}
        {...props}
      >
        {children}
      </Button>
      {isFetching && <LoadSpinner size="sm" />}
    </div>
  )
}
