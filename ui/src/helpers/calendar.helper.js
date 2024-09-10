import moment from "moment"
import { getNumberRange } from "./helpers"

export const DATE_FORMAT = "YYYY-MM-DD"

const today = new Date()

export const months = [
  "January", 
  "February", 
  "March", 
  "April", 
  "May", 
  "June", 
  "July",
  "August", 
  "September", 
  "October", 
  "November", 
  "December",
]

export const years = getNumberRange(moment(today).year() - 10, moment(today).year() + 10)
