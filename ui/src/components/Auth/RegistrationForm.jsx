import { useState, useEffect } from "react"
import { useForm } from "@tanstack/react-form"
import { zodValidator } from "@tanstack/zod-form-adapter"

import { useAuth } from "../../hooks/useAuth"
import { formDefaultValues } from "../../helpers/form.helper"
import { validationSchemas } from "../../helpers/validation.helper"
import { switchTabindex } from "../../helpers/helpers"

import { Form } from "../Form/Form"
import { Input } from "../controls/Input/Input"
import { Submit } from "../Form/Submit"

import style from "./Auth.module.css"

export const RegistrationForm = ({ isActive, onSuccess }) => {
  const [requestError, setRequestError] = useState(null)
  const { register, validateName, validateEmail, isFetching } = useAuth()

  useEffect(() => {
    if (requestError !== null) {
      throw requestError
    }
  }, [requestError])

  const form = useForm({
    defaultValues: formDefaultValues.registration,
    validatorAdapter: zodValidator,
    onSubmit: async ({ value }) => {
      try {
        await register(value.name, value.email, value.password)
        onSuccess()
      } catch (error) {
        setRequestError(error)
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
          message={"Sign"}
          form={form}
          focusable={isActive}
        />
      }
      isFetching={isFetching}
    >
      <form.Field
        name="name"
        validators={{
          onChangeAsyncDebounceMs: 500,
          onChangeAsync: async ({ value }) => {
            try {
              return await validateName(value)
            } catch (error) {
              setRequestError(error)
            }
          },
          onChange: validationSchemas.registrationSchema.shape.name
        }}
      >
        {(field) => (
          <Input
            id="reg-name"
            label="Name *"
            type="text"
            value={field.state.value}
            errors={field.state.meta.errors}
            isValidating={field.getMeta().isValidating}
            onChange={(value) => field.handleChange(value)}
            tabIndex={switchTabindex(isActive)}
          />
        )}
      </form.Field>
      <form.Field
        name="email"
        validators={{
          onChangeAsyncDebounceMs: 500,
          onChangeAsync: async ({ value }) => {
            try {
              return await validateEmail(value)
            } catch (error) {
              setRequestError(error)
            }
          },
          onChange: validationSchemas.registrationSchema.shape.email
        }}
      >
        {(field) => (
          <Input
            id="log-email"
            label="Email *"
            type="email"
            value={field.state.value}
            errors={field.state.meta.errors}
            isValidating={field.getMeta().isValidating}
            onChange={(value) => field.handleChange(value)}
            tabIndex={switchTabindex(isActive)}
          />
        )}
      </form.Field>
      <form.Field
        name="password"
        validators={{ onChange: validationSchemas.registrationSchema.shape.password }}
      >
        {(field) => (
          <Input
            id="reg-password"
            label="Password *"
            type="password"
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
            tabIndex={switchTabindex(isActive)}
          />
        )}
      </form.Field>
    </Form>
  )
}
