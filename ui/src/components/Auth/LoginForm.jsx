import { useState, useEffect } from "react"
import { useForm } from "@tanstack/react-form"
import { useRouter, useSearch } from "@tanstack/react-router"
import { zodValidator } from "@tanstack/zod-form-adapter"
import { AxiosError } from "axios"

import { useAuth } from "../../hooks/useAuth"
import { formDefaultValues } from "../../helpers/form.helper"
import { validationSchemas } from "../../helpers/validation.helper"
import { switchTabindex } from "../../helpers/helpers"

import { Form } from "../Form/Form"
import { Input } from "../controls/Input/Input"
import { CheckBox } from "../controls/CheckBox/CheckBox"
import { Submit } from "../Form/Submit"

import style from "./Auth.module.css"

export const LoginForm = ({ isActive }) => {
  const [loginError, setLoginError] = useState(null)
  const [requestError, setRequestError] = useState(null)

  const { login, isFetching } = useAuth()

  const search = useSearch({ strict: false, })
  const router = useRouter()

  useEffect(() => {
    if (requestError !== null) {
      throw requestError
    }
  }, [requestError])

  const form = useForm({
    defaultValues: formDefaultValues.login,
    validatorAdapter: zodValidator,
    onSubmit: async ({ value }) => {
      try {
        await login(value.name, value.password, value.remember)
        await router.navigate({ to: search.redirect || "/hotels" })
      } catch (error) {
        if (error instanceof AxiosError && error.response?.status === 401) {
          setLoginError(error.response.data.errorMessage)
        } else {
          setRequestError(error)
        }
      } finally {
        form.reset()
      }
    },
  })

  return (
    <Form
      className={style.form}
      submitComponent={
        <Submit
          className={style.button}
          form={form}
          message={"Login"}
          focusable={isActive}
        />
      }
      isFetching={isFetching}
      error={loginError}>
      <form.Field
        name="name"
        validators={{ onChange: validationSchemas.loginSchema.shape.name }}
      >
        {(field) => (
          <Input
            id="log-name"
            label="Name *"
            type="text"
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
            tabIndex={switchTabindex(isActive)}
          />
        )}
      </form.Field>
      <form.Field
        name="password"
        validators={{ onChange: validationSchemas.loginSchema.shape.name }}
      >
        {(field) => (
          <Input
            id="log-password"
            label="Password *"
            type="password"
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
            tabIndex={switchTabindex(isActive)}
          />
        )}
      </form.Field>
      <form.Field
        name="remember"
      >
        {(field) => (
          <CheckBox
            id="log-remember"
            label="Remember me:"
            checked={field.state.value}
            onChange={(value) => field.handleChange(value)}
            tabIndex={switchTabindex(isActive)}
          />
        )}
      </form.Field>
    </Form>
  )
}
