import { removeEmptyParams } from "../helpers/search.helper"

export const useFilters = (routeApi) => {
  const navigate = routeApi.useNavigate()
  const filters = routeApi.useSearch()

  const setFilters = (filters) => {
    const pageNumber = filters.pageIndex
    delete filters.pageIndex

    const search = pageNumber != undefined ? { pageNumber: pageNumber, ...filters } : filters

    navigate({ search: (prev) => removeEmptyParams({ ...prev, ...search }) })
  }

  const resetFilters = (search = {}) => navigate({ search })

  const hasFilters = () => Object.keys(filters).filter((key) => !["pageNumber", "pageSize"].includes(key)).length > 0

  return { filters, setFilters, resetFilters, hasFilters }
}
