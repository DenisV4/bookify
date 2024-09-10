/* eslint-disable react/no-children-prop */
import { switchTabindex } from "../../helpers/helpers"

import { Button } from "../buttons/Button/Button"

export const Submit = ({ className, form, message, focusable = true, ...props }) => {
  return (
    <form.Subscribe
      selector={(state) => [state.canSubmit, state.isValidating,]}
      children={([canSubmit, isValidating]) => (
        <Button
          className={className}
          appearance="primary"
          disabled={!canSubmit || isValidating}
          onClick={form.handleSubmit}
          tabIndex={switchTabindex(focusable)}
          {...props}
        >
          {message}
        </Button>
      )}
    />
  )
}
