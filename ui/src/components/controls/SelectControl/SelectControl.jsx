/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react"
import cn from "classnames"

import { CustomScroll } from "react-custom-scroll"
import Select, { components } from "react-select"

import { useSearcher } from "../../../hooks/useSearcher"
import { useThrowError } from "../../../hooks/useThrowError"

import { CrossButton } from "../../buttons/CrossButton/CrossButton"
import { LoadSpinner } from "../../LoadSpinner/LoadSpinner"
import ArrowIcon from "./arrow.svg?react"

import style from "./SelectControl.module.css"

const DropdownIndicator = (props) => {
  return (
    <components.DropdownIndicator {...props}>
      <ArrowIcon />
    </components.DropdownIndicator>
  )
}

const Menu = (props) => {
  return (
    <components.Menu {...props}>
      <CustomScroll>
        {props.children}
      </CustomScroll>
    </components.Menu>
  )
}

const MultiValueRemove = (props) => {
  return (
    <components.MultiValueRemove {...props}>
      <CrossButton />
    </components.MultiValueRemove>
  )
}

export const SelectControl = ({
  className,
  label,
  width,
  value: initialValue,
  onChange,
  debounce = 500,
  options,
  isValidating,
  errors,
  ...props
}) => {
  const [value, setValue] = useState(initialValue)
  const [menuIsOpen, setMenuIsOpen] = useState(false)

  const { data, error, isFetching, searchQuery, setSearchQuery } = useSearcher(props?.searchParams, debounce)

  useEffect(() => setValue(initialValue), [initialValue])
  useEffect(() => onChange(value), [value])

  useThrowError(error)

  return (
    <div
      className={cn(className, style.field, {
        [style.default]: !props.searchParams,
        [style.searchable]: !!props.searchParams
      })}>
      {label && <span className={style.label}>{label}</span>}
      <Select
        classNames={{
          container: () => style.container,
          control: (state) => state.isFocused ? cn(style.select, style.focused) : style.select,
          indicatorsContainer: () => menuIsOpen ? cn(style.indicator, style.active) : style.indicator,
          indicatorSeparator: () => style.hidden,
          clearIndicator: () => style.hidden,
          loadingIndicator: () => style.hidden,
          placeholder: () => style.shaded,
          valueContainer: () => style.values,
          singleValue: () => menuIsOpen ? style.shaded : "",
          multiValue: () => style.value,
          multiValueRemove: () => style.remove,
          menu: () => style.menu,
          menuList: () => style.list,
          option: (state) => state.isFocused ? cn(style.option, style.focused) : style.option,
          noOptionsMessage: () => style.shaded,
          loadingMessage: () => style.shaded,
        }}
        styles={{
          control: (baseStyles) => (
            { ...baseStyles, width: width ? width : "100%" }
          )
        }}
        controlShouldRenderValue
        unstyled
        isClearable={!!props.searchParams}
        isSearchable={!!props.searchParams}
        hideSelectedOptions
        backspaceRemovesValue
        // blurInputOnSelect
        captureMenuScroll={false}
        menuPlacement="auto"
        minMenuHeight={160}
        maxMenuHeight={200}
        components={{
          Menu: Menu,
          DropdownIndicator: DropdownIndicator,
          MultiValueRemove: MultiValueRemove
        }}
        placeholder={props.placeholder ? props.placeholder : null}
        value={
          value !== undefined
            ? Array.isArray(value)
              ? value.map((item) => ({ label: item, value: item }))
              : { label: value, value }
            : null
        }
        inputValue={searchQuery}
        isFetching={isFetching}
        options={props.searchParams ? data?.map((item) => ({ label: item.id, value: item.id })) : options}
        onMenuOpen={() => setMenuIsOpen(true)}
        onMenuClose={() => setMenuIsOpen(false)}
        onInputChange={(value) => {
          if (props.valueType && props.valueType === "number") {
            return setSearchQuery(!isNaN(Number(value)) ? value > 0 ? value.trim() : "" : "")
          }
          setSearchQuery(value)
        }}
        onChange={(option) => {
          setValue(Array.isArray(option)
            ? option.length > 0
              ? option.map((item) => item?.value)
              : undefined
            : option?.value
              ? option.value
              : undefined)
        }}
        {...props}
      />
      {props.searchParams && value !== undefined && (
        <CrossButton className={style.clear} size="sm" onClick={() => setValue(undefined)} />
      )}
      {isValidating && <LoadSpinner className={style.spinner} size="sm" />}
      {errors && <span className={style.error}>{errors}</span>}
    </div>
  )
}
