import { useRef } from "react"
import cn from "classnames"
import DatePicker, { registerLocale } from "react-datepicker"
import { enGB } from "date-fns/locale"

import { CalendarHeader } from "./CalendarHeader"
import CalendarIcon from "./calendar.svg?react"
import { ForwardedInput } from "../Input/Input"

import "react-datepicker/dist/react-datepicker.css"
import "./calendar.css"
import style from "./DateInput.module.css"

registerLocale("enGB", enGB)

export const DateInput = ({ className, id, value, label, ...props }) => {
  const inputRef = useRef(null)

  return (
    <div
      className={cn(className, style.date)}>
      <DatePicker
        locale={"enGB"}
        dateFormat="dd.MM.yyyy"
        id={id}
        minDate={props.min}
        maxDate={props.max}
        selected={value}
        placeholderText="dd.MM.yyyy"
        showIcon
        toggleCalendarOnIconClick
        icon={<CalendarIcon />}
        customInput={
          <ForwardedInput
            ref={inputRef}
            className={"date-input"}
            id={id}
            label={label}
            errors={props.errors}
            type="text"
          />
        }
        renderCustomHeader={({
          date,
          changeYear,
          changeMonth,
          decreaseMonth,
          increaseMonth,
          prevMonthButtonDisabled,
          nextMonthButtonDisabled,
        }) => (
          <CalendarHeader
            date={date}
            changeYear={changeYear}
            changeMonth={changeMonth}
            decreaseMonth={decreaseMonth}
            increaseMonth={increaseMonth}
            prevMonthButtonDisabled={prevMonthButtonDisabled}
            nextMonthButtonDisabled={nextMonthButtonDisabled}
          />
        )}
        // showPopperArrow={false}
        popperPlacement="bottom-start"
        wrapperClassName={style.wrapper}
        calendarIconClassName={style.icon}
        popperClassName={style.popper}
        calendarClassName={style.calendar}
        dayClassName={() => style.day}
        {...props}
      />
    </div>
  )
}
