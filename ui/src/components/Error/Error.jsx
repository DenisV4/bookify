import { useRouter } from "@tanstack/react-router"

import { Title } from "../Title/Title"
import { Button } from "../buttons/Button/Button"

import { useCallback } from "react"
import { Layout } from "../layout/Layout"

import style from "./Error.module.css"

export const Error = ({ code, reset }) => {
  const router = useRouter()

  const reload = useCallback(() => {
    reset()
    router.invalidate()
  }, [reset, router])

  const handleCode = () => {
    switch (code) {
      case "ERR_BAD_REQUEST":
        return { title: "400 Bad request", message: "Something went wrong. Please try again later." }

      case "ERR_BAD_RESPONSE":
        return { title: "500 Internal server error", message: "Please try again later." }

      case "ERR_NETWORK":
        return { title: "Network error", message: "Remote server is not available. Please try again later." }

      default:
        return { title: "Unexpected error", message: "Please try again later." }
    }
  }

  return (
    <Layout type="simple">
      <div className={style.error}>
        <Title type="h2">{handleCode().title}</Title>
        <p className={style.message}>{handleCode().message}</p>
        <div className={style.buttons}>
          {code && <Button appearance="secondary" size="sm" onClick={reload}>Back</Button>}
          <Button appearance="secondary" size="sm" onClick={() => router.navigate({ to: "/" })}>Start over</Button>
        </div>
      </div>
    </Layout>
  )
}
