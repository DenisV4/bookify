import { hasRoles } from "../../security/store"

import { DownloadLink } from "../DownloadLink/DownloadLink"
import { HyperLink } from "../HyperLink/HyperLink"
import { Title } from "../Title/Title"

import style from "./Statistics.module.css"

export const Statistics = ({ children }) => {
  return (
    <div className={style.wrapper}>
      <div className={style.header}>
        <div className={style.top}>
          <Title className={style.title} type="h2" size="sm">Statistics</Title>
          <DownloadLink
            resource={"statistics"}
            size="md"
            disabled={!hasRoles(["ADMIN"])}
          >
            Download CSV
          </DownloadLink>
        </div>
        <div className={style.links}>
          <HyperLink
            className={style.link}
            activeClassName={style.active}
            to="/statistics/logins"
          >
            Logins
          </HyperLink>
          <HyperLink
            className={style.link}
            activeClassName={style.active}
            to="/statistics/registrations"
          >
            Registrations
          </HyperLink>
          <HyperLink
            className={style.link}
            activeClassName={style.active}
            to="/statistics/bookings"
          >
            Bookings
          </HyperLink>
        </div>
      </div>
      {children}
    </div >
  )
}
