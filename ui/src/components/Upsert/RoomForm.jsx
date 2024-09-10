import { useMemo } from "react"

import { validationSchemas } from "../../helpers/validation.helper"

import { Form } from "../Form/Form"
import { Input, Textarea } from "../controls/Input/Input"
import { SelectControl } from "../controls/SelectControl/SelectControl"
import { Submit } from "../Form/Submit"

import style from "./Upsert.module.css"

export const RoomForm = ({ form, isFetching, data }) => {
  const labelSuffix = useMemo(() => !data ? "*" : "", [data])
  const validationSchema = useMemo(() => !data
    ? validationSchemas.roomSchema
    : validationSchemas.roomSchema.partial(), [data])

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
            id="room-name"
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
        name="description"
        validators={{
          onChange: validationSchema.shape.description,
        }}
      >
        {(field) => (
          <Textarea
            id="room-description"
            label={`Description ${labelSuffix}`}
            placeholder={!data ? "" : data.description}
            value={field.state.value}
            errors={field.state.meta.errors}
            onChange={(value) => field.handleChange(value)}
          />
        )}
      </form.Field>
      <div className={style.group}>
        <form.Field
          name="number"
          validators={{
            onChange: validationSchema.shape.number,
          }}
        >
          {(field) => (
            <Input
              id="room-number"
              label={`Number ${labelSuffix}`}
              type="text"
              placeholder={!data ? "" : data.number}
              value={field.state.value}
              errors={field.state.meta.errors}
              onChange={(value) => field.handleChange(value)}
            />
          )}
        </form.Field>
        <form.Field
          name="price"
          validators={{
            onChange: validationSchema.shape.price,
          }}
        >
          {(field) => (
            <Input
              id="room-price"
              label={`Price ${labelSuffix}`}
              type="number"
              min={0.1}
              step={0.1}
              placeholder={!data ? "" : data.price}
              value={field.state.value}
              errors={field.state.meta.errors}
              onChange={(value) => field.handleChange(value)}
            />
          )}
        </form.Field>
        <form.Field
          name="guestsNumber"
          validators={{
            onChange: validationSchema.shape.guestsNumber,
          }}
        >
          {(field) => (
            <Input
              id="room-guests-number"
              label={`Guests number ${labelSuffix}`}
              type="number"
              min={1}
              placeholder={!data ? "" : data.guestsNumber}
              value={field.state.value}
              errors={field.state.meta.errors}
              onChange={(value) => field.handleChange(value)}
            />
          )}
        </form.Field>
        <form.Field
          name="hotelId"
          validators={{
            onChange: validationSchema.shape.hotelId,
          }}
        >
          {(field) => (
            <SelectControl
              id="room-hotel-id"
              label={`Hotel id ${labelSuffix}`}
              valueType="number"
              placeholder={!data ? "" : data.hotelId}
              value={field.state.value}
              errors={field.state.meta.errors}
              onChange={(value) => field.handleChange(value)}
              searchParams={{ resource: "hotels", field: "id" }}
            />
          )}
        </form.Field>
      </div>
    </Form>
  )
}
