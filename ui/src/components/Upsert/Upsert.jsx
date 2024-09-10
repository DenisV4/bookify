import { useMemo } from "react"
import { useSearch } from "@tanstack/react-router"

import { useUpsertForm } from "../../hooks/useUpsertForm"
import { camelTextToFlat } from "../../helpers/helpers"

import { Title } from "../Title/Title"
import { HyperLink } from "../HyperLink/HyperLink"
import { HotelForm } from "./HotelForm"
import { RoomForm } from "./RoomForm"
import { UserForm } from "./UserForm"

import style from "./Upsert.module.css"

export const Upsert = ({ resource }) => {
  const search = useSearch({ strict: false })

  const { form, isFetching, data } = useUpsertForm(resource, search)
  const itemName = useMemo(() => camelTextToFlat(resource.slice(0, -1)), [resource])

  return (
    <div className={style.wrapper} >
      <div className={style.top}>
        <HyperLink className={style.link} to={search.redirect || `/${resource}`} activeProps={{}}>
          {resource === "users/profile" ? "Back" : "Back to list"}
        </HyperLink>
        <Title className={style.title} type="h3" size="sm">
          {data
            ? resource === "users/profile"
              ? "Edit profile"
              : `Edit ${itemName} #${data.id}`
            : `Create new ${itemName}`}
        </Title>
      </div>
      <div className={style.content}>
        <div></div>
        {resource === "hotels" && <HotelForm form={form} isFetching={isFetching} data={data} />}
        {resource === "rooms" && <RoomForm form={form} isFetching={isFetching} data={data} />}
        {resource === "users" && <UserForm form={form} isFetching={isFetching} data={data} />}
        {resource === "users/profile" && <UserForm form={form} isFetching={isFetching} data={data} isProfile />}
        <div></div>
      </div>
    </div>
  )
}
