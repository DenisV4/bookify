import { useRef, useState } from "react"
import { CSSTransition } from "react-transition-group"

import cn from "classnames"

import { Button } from "../buttons/Button/Button"
import { HotelFilter } from "./HotelFilter"
import { RoomFilter } from "./RoomFilter"
import { Input } from "../controls/Input/Input"

import ResetIcon from "./reset.svg?react"
import FilterIcon from "./filter.svg?react"

import style from "./Filter.module.css"

const PLACE_HOLDER = "Search..."

export const Filter = ({ className, target, filters, setFilters, resetFilters, hasFilters, ...props }) => {
  const filterRef = useRef(null)
  const [isOpen, setIsOpen] = useState(hasFilters())

  return (
    <div className={style.wrapper}>
      <Button
        className={cn(style.button, { [style.active]: hasFilters() })}
        appearance="secondary"
        mode="icon"
        onClick={() => setIsOpen(!isOpen)}
      >
        <FilterIcon />
      </Button>
      <CSSTransition
        classNames={{
          enter: style.enter,
          enterActive: style.enterActive,
          enterDone: style.enterDone,
          exit: style.exit,
          exitActive: style.exitActive,
          exitDone: style.exitDone,
        }}
        appear={true}
        nodeRef={filterRef}
        in={isOpen}
        unmountOnExit={true}
        timeout={300}
      >
        <div ref={filterRef} className={cn(className, style.filter)} {...props}>
          <div className={style.top}>
            <span className={style.caption}>Filter by:</span>
            <Button appearance="secondary" mode="icon" onClick={() => resetFilters()}>
              <ResetIcon />
            </Button>
          </div>
          <div className={style.filters}>
            <Input
              id="id-filter"
              label="Id"
              type="number"
              debounce={500}
              placeholder={PLACE_HOLDER}
              min={1}
              value={filters.id}
              onChange={(value) => {
                const id = value ? Number(value) : NaN
                setFilters({ id })
              }}
            />
            <Input
              id="name-filter"
              label="Name"
              type="text"
              debounce={500}
              placeholder={PLACE_HOLDER}
              value={filters.name}
              onChange={(value) => {
                const name = value ? value : ""
                setFilters({ name })
              }}
            />
            {target === "hotels" && <HotelFilter filters={filters} setFilters={setFilters} placeholder={PLACE_HOLDER} />}
            {target === "rooms" && <RoomFilter filters={filters} setFilters={setFilters} placeholder={PLACE_HOLDER} />}
          </div>
        </div>
      </CSSTransition>
    </div>
  )
}
