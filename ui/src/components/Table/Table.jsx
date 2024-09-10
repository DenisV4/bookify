/* eslint-disable react-hooks/exhaustive-deps */
import { useMemo, useState } from "react"
import { flexRender, getCoreRowModel, getSortedRowModel, useReactTable } from "@tanstack/react-table"
import { getRouteApi, useLocation } from "@tanstack/react-router"

import cn from "classnames"

import { Tooltip } from "react-tooltip"

import { hasRoles } from "../../security/store"
import { useFilters } from "../../hooks/useFilters"
import { useRemoveItem } from "../../hooks/mutations/useRemoveItem"
import { useConfirmation } from "../../hooks/useConfirmation"

import { camelTextToFlat, objectToHtml, switchTabindex } from "../../helpers/helpers"
import { columnTemplates } from "../../helpers/table.helper"
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from "../../helpers/search.helper"

import { Title } from "../Title/Title"
import { Button } from "../buttons/Button/Button"
import { Pagination } from "../Pagination/Pagination"
import { Filter } from "../Filter/Filter"
import { Actions } from "../Actions/Actions"

import style from "./Table.module.css"

export const Table = ({ resource }) => {
  const routeApi = getRouteApi(`/_auth/${resource.includes("/") ? resource : resource + "/"}`)
  const location = useLocation()
  const navigate = routeApi.useNavigate()

  const removeItem = useRemoveItem(resource)
  const confirm = useConfirmation()
  const { data } = routeApi.useLoaderData()

  const { filters, setFilters, resetFilters, hasFilters } = useFilters(routeApi)
  const [sorting, setSorting] = useState(
    resource.split("/")[0] !== "statistics"
      ? [{ id: "id" }]
      : [{ id: "timestamp", desc: true }]
  )

  const paginationState = {
    pageIndex: filters.pageNumber ?? DEFAULT_PAGE_NUMBER,
    pageSize: filters.pageSize ?? DEFAULT_PAGE_SIZE
  }
  const target = useMemo(() => resource.split("/")[1] ? resource.split("/")[1] : resource, [])
  const hasActions = useMemo(() => ["hotels", "rooms", "users"].includes(target), [])

  const columnTemplate = useMemo(() => columnTemplates[
    target === "bookings" && resource.split("/")[0] === "statistics"
      ? "bookingStat"
      : target
  ], [])
  const columns = hasActions ? [
    ...columnTemplate,
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => (
        <Actions
          onEdit={() => navigate({
            to: `/${resource}/edit/$id`,
            params: { id: row.original.id },
            search: { redirect: location.href },
          })}
          onDelete={() => confirm({
            message: `Delete the ${resource.slice(0, -1)} with id = ${row.original.id}?`,
            action: "Delete"
          })
            .then(() => removeItem.mutate(row.original.id))
          }
          disabled={!hasRoles(["ADMIN"])}
        />
      ),
      size: 68
    }
  ] : columnTemplate

  const table = useReactTable({
    columns,
    data: data[target],
    state: {
      sorting: sorting,
      pagination: paginationState,
    },
    manualPagination: true,
    manualFiltering: true,
    rowCount: data.totalElements,
    pageCount: data.totalPages,
    onSortingChange: setSorting,
    onPaginationChange: (pagination) => {
      setFilters(typeof pagination === "function" ? pagination(paginationState) : pagination)
    },
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
  })

  return (
    <div className={style.wrapper}>
      <div className={style.top}>
        {resource.split("/")[0] !== "statistics" && (
          <Title className={style.title} type="h3" size="sm">
            {camelTextToFlat(target)}
          </Title>)}
        {hasActions && (
          <Button
            onClick={() => navigate({
              to: `/${resource}/create`,
              search: { redirect: location.href },
            })}
            disabled={!hasRoles(["ADMIN"])}
          >
            + Create
          </Button>
        )}
      </div>
      {["hotels", "rooms"].includes(target) && (
        <Filter
          className={style.filter}
          target={target}
          filters={filters}
          setFilters={setFilters}
          resetFilters={resetFilters}
          hasFilters={hasFilters}
        />
      )}
      <table className={style.table}>
        <thead>
          {table.getHeaderGroups().map(headerGroup => (
            <tr key={headerGroup.id}>
              {headerGroup.headers.map(header => (
                <th
                  key={header.id}
                  className={cn({ [style.sort]: header.column.getCanSort() })}
                  style={{
                    width: ["actions", "id", "timestamp"].includes(header.id)
                      ? `${header.getSize()}px`
                      : `${header.getSize()}%`
                  }}
                >
                  <button
                    className={style.header}
                    onClick={header.column.getToggleSortingHandler()}
                    tabIndex={header.column.getCanSort() ? 0 : -1}
                    disabled={!header.column.getCanSort()}
                  >
                    <span>
                      {flexRender(header.column.columnDef.header, header.getContext())}
                    </span>
                    {{ asc: '▲', desc: '▼' }[header.column.getIsSorted() ?? null]}
                  </button>
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody>
          {table.getRowModel().rows.map((row) => (
            <tr key={row.id}>
              {row.getVisibleCells().map((cell, index) => (
                <td key={cell.id}>
                  <span
                    className={cn(style.cell, {
                      [style.ellipsis]: cell.column.getCanSort(),
                      [style.tooltip]: index === 0
                    })}
                    data-tooltip-id={index === 0 ? `row-tooltip` : null}
                    data-tooltip-html={index === 0 ? objectToHtml(row.original, style.popup) : null}
                    tabIndex={switchTabindex(index === 0)}
                  >
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </span>
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      {
        data?.totalPages > 0 ? (
          <Pagination className={style.pagination} table={table} />
        ) : (
          <div className={style.message}>No data found</div>
        )
      }
      <Tooltip
        id={`row-tooltip`}
        className={"tooltip"}
        place="top-start"
        openEvents={{ focus: true, click: true }}
        closeEvents={{ blur: true, mouseleave: true }}
        globalCloseEvents={{ escape: true, clickOutsideAnchor: true }}
      />
    </div >
  )
}
