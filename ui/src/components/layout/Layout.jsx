import { useRef } from "react"
import cn from "classnames"

import { useOffset } from "../../hooks/useOffset"

import { Header } from "./Header/Header"
import { SideBar } from "./SideBar/SideBar"
import { Footer } from "./Footer/Footer"
import { Up } from "../Up/Up"

import style from "./Layout.module.css"

export const Layout = ({ children, type = "default", header = "default" }) => {
  const ref = useRef(null)
  const { right } = useOffset(ref)

  return (
    <div ref={ref} className={cn(style.layout, { [style.simple]: type === "simple" })}>
      <Header className={style.header} type={header} />
      {type === "default" && <SideBar className={style.sidebar} />}
      <main className={style.main}>
        {type === "default" && (
          <section className={style.section}>
            {children}
          </section>
        )}
        {type === "simple" && children}
      </main>
      <Footer className={style.footer} />
      <Up position={{ right }} />
    </div>
  )
}
