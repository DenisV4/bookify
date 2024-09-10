/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from "react"
import { getRouteApi } from "@tanstack/react-router"
import { useInView } from "react-intersection-observer"

import cn from "classnames"

import { useFilters } from "../../hooks/useFilters"
import { camelTextToFlat } from "../../helpers/helpers"
import { defaultSearch } from "../../helpers/search.helper"

import { Title } from "../Title/Title"
import { ListFilter } from "./ListFilter"
import { Card } from "../cards/Card/Card"
import { LoadSpinner } from "../LoadSpinner/LoadSpinner"

import style from "./List.module.css"

export const List = ({ className, resource }) => {
  const routeApi = getRouteApi(`/_auth/${resource}`)

  const { ref, inView } = useInView({ threshold: 0.1 })

  const { filters, setFilters, resetFilters } = useFilters(routeApi)
  const { data } = routeApi.useLoaderData()

  const target = resource.split("/")[0]
  const defaultFilters = defaultSearch[target]

  const hasMore = filters.pageSize < data.totalElements

  useEffect(() => {
    if (inView && hasMore) {
      setFilters({ pageSize: filters.pageSize + defaultFilters.pageSize / 2 })
    }
  }, [inView])

  return (
    <div className={cn(className, style.wrapper)}>
      <div className={style.top}>
        <Title className={style.title} type="h3" size="sm">
          {camelTextToFlat(target)}
        </Title>
      </div>
      <ListFilter
        target={target}
        filters={filters}
        setFilters={setFilters}
        resetFilters={() => {
          resetFilters(defaultFilters)
        }}
      />
      {data[target].length > 0 ? (
        <>
          <ul className={style.list}>
            {data[target].map((item) => (
              <li key={item.id}>
                <Card target={target} item={item} filters={filters} />
              </li>
            ))}
          </ul>
          <div ref={ref} className={style.bottom}>
            {(inView && hasMore) && (
              <>
                <span>Fetching...</span>
                <LoadSpinner className={style.spinner} size="sm" />
              </>
            )}
          </div>
        </>
      ) : (
        <div className={style.message}>No data found</div>
      )}
    </div>
  )
}
