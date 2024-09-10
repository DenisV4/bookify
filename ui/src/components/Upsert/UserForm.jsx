import { useMemo, useState } from "react"
import { useQueryClient } from "@tanstack/react-query"

import { useAuth } from "../../hooks/useAuth"
import { useQueryOptions } from "../../hooks/useQueryOptions"
import { useThrowError } from "../../hooks/useThrowError"

import { validationSchemas } from "../../helpers/validation.helper"
import { displayToRoles, rolesToDisplay } from "../../helpers/helpers"
import { userRoleOptions } from "../../helpers/form.helper"

import { Form } from "../Form/Form"
import { Input } from "../controls/Input/Input"
import { Submit } from "../Form/Submit"
import { SelectControl } from "../controls/SelectControl/SelectControl"

import style from "./Upsert.module.css"

export const UserForm = ({ form, isFetching, data, ...props }) => {
  const queryClient = useQueryClient()
  const [requestError, setRequestError] = useState(null)

  const { validateName, validateEmail } = useAuth()
  const { validate } = useQueryOptions("users")

  const labelSuffix = useMemo(() => !data ? "*" : "", [data])
  const validationSchema = useMemo(() => !data
    ? validationSchemas.userSchema
    : validationSchemas.userSchema.partial(), [data])

  const formErrorMap = form.useStore((state) => state.errorMap)

  useThrowError(requestError)

  return (
    <Form
      className={style.form}
      submitComponent={
        <Submit
          className={style.button}
          form={form}
          message={data ? "Edit" : "Create"}
          size="sm"
        />
      }
      isFetching={isFetching}
      error={formErrorMap.onSubmit}
      size="md"
    >
      <form.Field
        name="name"
        validators={{
          onChangeAsyncDebounceMs: 500,
          onChangeAsync: async ({ value }) => {
            if (value !== undefined) {
              return await validateName(value)
            }
            return undefined
          },
          onChange: validationSchema.shape.name,
        }}>
        {(field) => (
          <Input
            id="user-name"
            label={`Name ${labelSuffix}`}
            type="text"
            placeholder={!data ? "" : data.name}
            value={field.state.value}
            isValidating={field.state.value !== undefined ? field.getMeta().isValidating : false}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
      <form.Field
        name="email"
        validators={{
          onChangeAsyncDebounceMs: 500,
          onChangeAsync: async ({ value }) => {
            if (value !== undefined) {
              return await validateEmail(value)
            }
            return undefined
          },
          onChange: validationSchema.shape.email,
        }}>
        {(field) => (
          <Input
            id="user-email"
            label={`Email ${labelSuffix}`}
            type="text"
            placeholder={!data ? "" : data.email}
            value={field.state.value}
            isValidating={field.state.value !== undefined ? field.getMeta().isValidating : false}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
      <form.Field
        name="password"
        validators={{
          onChange: validationSchema.shape.password,
        }}>
        {(field) => (
          <Input
            id="user-password"
            label={`Password ${labelSuffix}`}
            type="password"
            placeholder={!data ? "" : data.password}
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
      {!props.isProfile && (
        <form.Field
          name="roles"
          validators={{
            onChangeAsyncDebounceMs: 500,
            onChangeAsync: async ({ value }) => {
              if (data && value !== undefined) {
                try {
                  const response = await queryClient.fetchQuery(
                    validate(data.id, { roles: displayToRoles(value).join(",") })
                  )
                  return response.data ? undefined : "There will be no admin left after execution"
                } catch (error) {
                  setRequestError(error)
                }
              }
              return undefined
            },
            onChange: validationSchema.shape.roles,
          }}>
          {(field) => (
            <SelectControl
              label={`Roles ${labelSuffix}`}
              placeholder={!data ? "" : rolesToDisplay(data.roles).join(" | ")}
              value={field.state.value}
              errors={field.state.meta.errors}
              isValidating={
                data
                  ? field.state.value !== undefined
                    ? field.getMeta().isValidating
                    : false
                  : false
              }
              onChange={(value) => { field.handleChange(value) }}
              options={userRoleOptions}
              isMulti
            />
          )}
        </form.Field>
      )}
    </Form >
  )
}
