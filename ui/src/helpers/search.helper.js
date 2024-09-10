import moment from "moment";
import { DATE_FORMAT } from "./calendar.helper";

export const DEFAULT_PAGE_NUMBER = 0;
export const DEFAULT_PAGE_SIZE = 10;

export const removeEmptyParams = (search) => {
  const newSearch = { ...search }

  Object.keys(newSearch).forEach((key) => {
    const value = newSearch[key]
    if (value === "" || value === undefined || typeof value === "number" && isNaN(value)) {
      delete newSearch[key]
    }
  })

  if (search.pageNumber === DEFAULT_PAGE_NUMBER) {
    delete newSearch.pageNumber
  }

  if (search.pageSize === DEFAULT_PAGE_SIZE) {
    delete newSearch.pageSize
  }

  return newSearch
}

export const defaultSearch = {
  hotels: {
    pageSize: 12,
  },
  rooms: {
    pageSize: 12,
    checkInDate: moment().add(1, "day").format(DATE_FORMAT),
    checkOutDate: moment().add(7, "day").format(DATE_FORMAT),
  }
}
