import { useEffect } from "react"

import cn from "classnames"

import { NavButton } from "../buttons/NavButton/NavButton"
import { Input } from "../controls/Input/Input"
import { SelectControl } from "../controls/SelectControl/SelectControl"

import style from "./Pagination.module.css"

export const Pagination = ({ className, table }) => {
  useEffect(() => {
    if (table.getState().pagination.pageIndex + 1 > table.getPageCount()) {
      table.setPageIndex(table.getPageCount())
    }
  }, [table])

  return (
    <div className={cn(className, style.pagination)}>
      <span className={style.info}>
        {`Page ${table.getState().pagination.pageIndex + 1} of ${table.getPageCount()}`}
      </span>
      <div className={style.navigation}>
        <NavButton
          className={style.button}
          target={"first"}
          disabled={!table.getCanPreviousPage()}
          onClick={() => table.setPageIndex(0)}
        />
        <NavButton
          className={style.button}
          target={"prev"}
          disabled={!table.getCanPreviousPage()}
          onClick={() => table.previousPage()}
        />
        <Input
          className={style.input}
          id={"page-number"}
          label={"Go to page:"}
          type="number"
          min={1}
          max={table.getPageCount()}
          value={table.getState().pagination.pageIndex + 1}
          onChange={(value) => {
            const pageIndex = value ? Number(value) - 1 : 0
            table.setPageIndex(pageIndex)
          }}
        />
        <NavButton
          className={style.button}
          target={"next"}
          disabled={!table.getCanNextPage()}
          onClick={() => table.nextPage()}
        />
        <NavButton
          className={style.button}
          target={"last"}
          disabled={!table.getCanNextPage()}
          onClick={() => table.setPageIndex(table.getPageCount() - 1)}
        />
      </div>
      <SelectControl
        id={"page-size"}
        label="Show:"
        width={68}
        value={table.getState().pagination.pageSize}
        onChange={(value) => table.setPageSize(Number(value))}
        options={[
          { value: 10, label: 10 },
          { value: 20, label: 20 },
          { value: 30, label: 30 },
          { value: 40, label: 40 },
          { value: 50, label: 50 },
          { value: 100, label: 100 },
        ]}
      />
    </div >
  )
}
