import { useRouter } from "@tanstack/react-router"

import { Layout } from "../layout/Layout"
import { Title } from "../Title/Title"
import { Button } from "../buttons/Button/Button"

import style from "./Error.module.css"

export const NotFound = ({ type = "default" }) => {
  const router = useRouter()

  return (
    <>
      <Layout type="simple" header="short">
        <div className={style.error}>
          <Title type="h2">404 Page not found</Title>
          <div className={style.buttons}>
            {type === "default" && <Button appearance="secondary" size="sm" onClick={() => router.history.back()}>Back</Button>}
            <Button appearance="secondary" size="sm" onClick={() => router.navigate({ to: "/" })}>Start over</Button>
          </div>
        </div>
      </Layout>
    </>
  )
}
