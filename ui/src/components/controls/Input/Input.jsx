/* eslint-disable react/display-name */
/* eslint-disable react-hooks/exhaustive-deps */
import { forwardRef, useEffect, useState } from "react"
import TextAutosize from "react-textarea-autosize"

import cn from "classnames"

import { getDecimalPlace, roundNumber } from "../../../helpers/helpers"

import { LoadSpinner } from "../../LoadSpinner/LoadSpinner"

import Eye from "./eye.svg?react"
import EyeOff from "./eye-off.svg?react"
import UpIcon from "./up.svg?react"
import DownIcon from "./down.svg?react"

import style from "./Input.module.css"

export const Wrapper = ({ children, className, id, label, errors, isValidating }) => {
  return (
    <div
      className={cn(className, style.field)}>
      {label && <label className={style.label} htmlFor={id}>{label}</label>}
      {children}
      {errors && <span className={style.error}>{errors}</span>}
      {isValidating && <LoadSpinner className={style.spinner} size="sm" />}
    </div>
  )
}

export const Input = ({
  className,
  id,
  type,
  label,
  value: initialValue,
  onChange,
  debounce = 0,
  errors,
  isValidating,
  tabIndex = 0,
  ...props
}) => {

  const [passwordType, setPasswordType] = useState("password")
  const [value, setValue] = useState(initialValue)

  useEffect(() => {
    setValue(initialValue)
  }, [initialValue])

  useEffect(() => {
    const timeout = setTimeout(() => {
      onChange(value)
    }, debounce)

    return () => clearTimeout(timeout)
  }, [value])

  const handleEyeToggle = () => {
    if (passwordType === "password") {
      setPasswordType("text")
    } else {
      setPasswordType("password")
    }
  }

  const increaseValue = () => {
    setValue((prev) => {
      const step = props?.step ?? 1
      const max = props?.max ?? null
      if (prev === undefined || Number.isNaN(prev)) {
        return step
      }
      const next = max ? Math.min(prev + step, max) : prev + step
      return roundNumber(next, getDecimalPlace(step))
    })
  }

  const decreaseValue = () => {
    setValue((prev) => {
      const step = props?.step ?? 1
      const min = props?.min ?? null
      if (prev === undefined || Number.isNaN(prev)) {
        return step
      }
      const next = min ? Math.max(prev - step, min) : prev - step

      return roundNumber(next, getDecimalPlace(step))
    })
  }

  return (
    <Wrapper
      className={className}
      id={id}
      label={label}
      errors={errors}
      isValidating={isValidating}
    >
      <input
        id={id}
        className={style.input}
        type={type === "password" ? passwordType : type}
        tabIndex={tabIndex}
        spellCheck="false"
        value={value ?? ""}
        onChange={(e) => {
          if (e.target.value === undefined || e.target.value.trimStart() === "") {
            return setValue(undefined)
          }
          if (type === "number") {
            return setValue(roundNumber(e.target.value, getDecimalPlace(props?.step ?? 1)))
          }
          setValue(e.target.value)
        }}
        {...props}
      />
      {type === "password" && (
        <div className={style.right}>
          <button
            className={style.eye}
            type="button"
            tabIndex={tabIndex}
            onClick={handleEyeToggle}>
            {passwordType === "password" ? <EyeOff /> : <Eye />}
          </button>
        </div>
      )}
      {(type === "number") && (
        <div className={style.right}>
          <div className={style.spin}>
            <button
              className={style.button}
              type="button"
              tabIndex={-1}
              onClick={increaseValue}
            >
              <UpIcon />
            </button>
            <button
              className={style.button}
              type="button"
              tabIndex={-1}
              onClick={decreaseValue}
            >
              <DownIcon />
            </button>
          </div>
        </div>
      )}
    </Wrapper >
  )
}

export const ForwardedInput = forwardRef(({ className, id, tabIndex = 0, label, errors, isValidating, ...props }, ref) => {
  return (
    <Wrapper className={className} id={id} label={label} errors={errors} isValidating={isValidating}>
      <input
        ref={ref}
        id={id}
        className={style.input}
        tabIndex={tabIndex}
        spellCheck="false"
        {...props}
      />
    </Wrapper>
  )
})

export const Textarea = ({
  className,
  id,
  value: initialValue,
  onChange,
  debounce = 0,
  tabIndex = 0,
  label,
  errors,
  isValidating,
  ...props
}) => {
  const [value, setValue] = useState(initialValue)

  useEffect(() => {
    setValue(initialValue)
  }, [initialValue])

  useEffect(() => {
    const timeout = setTimeout(() => {
      onChange(value)
    }, debounce)

    return () => clearTimeout(timeout)
  }, [value])

  return (
    <Wrapper className={className} id={id} label={label} errors={errors} isValidating={isValidating}>
      <TextAutosize
        id={id}
        className={cn(style.input, style.textarea)}
        tabIndex={tabIndex}
        spellCheck="false"
        value={value ?? ""}
        onChange={(e) => {
          if (e.target.value === undefined || e.target.value.trimStart() === "") {
            return setValue(undefined)
          }
          setValue(e.target.value)
        }}
        {...props}
      />
    </Wrapper>
  )
}
