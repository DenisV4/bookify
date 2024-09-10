import { useMemo } from "react"

import { validationSchemas } from "../../helpers/validation.helper"

import { Form } from "../Form/Form"
import { Input } from "../controls/Input/Input"
import { Submit } from "../Form/Submit"

import style from "./Upsert.module.css"

export const HotelForm = ({ form, isFetching, data }) => {
  const labelSuffix = useMemo(() => !data ? "*" : "", [data])
  const validationSchema = useMemo(() => !data
    ? validationSchemas.hotelSchema
    : validationSchemas.hotelSchema.partial(), [data]
  )

  const formErrorMap = form.useStore((state) => state.errorMap)

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
          onChange: validationSchema.shape.name,
        }}
      >
        {(field) => (
          <Input
            id="hotel-name"
            label={`Name ${labelSuffix}`}
            type="text"
            placeholder={!data ? "" : data.name}
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
      <form.Field
        name="title"
        validators={{
          onChange: validationSchema.shape.title,
        }}
      >
        {(field) => (
          <Input
            id="hotel-title"
            label={`Title ${labelSuffix}`}
            type="text"
            placeholder={!data ? "" : data.title}
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
      <form.Field
        name="city"
        validators={{
          onChange: validationSchema.shape.city,
        }}
      >
        {(field) => (
          <Input
            id="hotel-city"
            label={`City ${labelSuffix}`}
            type="text"
            placeholder={!data ? "" : data.city}
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
      <form.Field
        name="address"
        validators={{
          onChange: validationSchema.shape.address,
        }}
      >
        {(field) => (
          <Input
            id="hotel-address"
            label={`Address ${labelSuffix}`}
            type="text"
            placeholder={!data ? "" : data.address}
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
      <form.Field
        name="distance"
        validators={{
          onChange: validationSchema.shape.distance,
        }}
      >
        {(field) => (
          <Input
            id="hotel-distance"
            label={`Distance ${labelSuffix}`}
            type="number"
            min={0.1}
            step={0.1}
            placeholder={!data ? "" : data.distance}
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
    </Form>
  )
}
