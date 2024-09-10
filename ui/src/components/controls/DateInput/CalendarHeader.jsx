import moment from "moment/moment"

import { months, years } from "../../../helpers/calendar.helper"

import { NavButton } from "../../buttons/NavButton/NavButton"
import { SelectControl } from "../SelectControl/SelectControl"

import style from "./DateInput.module.css"

export const CalendarHeader = ({
  date,
  changeYear,
  changeMonth,
  decreaseMonth,
  increaseMonth,
  prevMonthButtonDisabled,
  nextMonthButtonDisabled,
}) => {
  return (
    <div className={style.header}>
      <NavButton
        className={style.button}
        target={"prev"}
        disabled={prevMonthButtonDisabled}
        onClick={decreaseMonth}
      />
      <SelectControl
        id="month"
        width={108}
        value={months[moment(date).month()]}
        onChange={(value) => changeMonth(months.indexOf(value))}
        options={months.map((month) => ({ value: month, label: month }))}
      />
      <SelectControl
        id="year"
        value={moment(date).year()}
        onChange={(value) => changeYear(value)}
        options={years.map((year) => ({ value: year, label: year }))}
      />
      <NavButton
        className={style.button}
        target={"next"}
        disabled={nextMonthButtonDisabled}
        onClick={increaseMonth}
      />
    </div>
  )
}
