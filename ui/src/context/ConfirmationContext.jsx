import { createContext } from "react"
import { useState, useRef } from "react"

import { Confirmation } from "../components/Confirmation/Confirmation"

export const ConfirmationContext = createContext()

export const ConfirmationContextProvider = ({ children }) => {
  const [options, setOptions] = useState(null)
  const [isOpen, setIsOpen] = useState(false)
  const promiseRef = useRef(null)

  const openConfirmation = (options) => {
    setOptions(options)
    setIsOpen(true)
    return new Promise((resolve, reject) => {
      promiseRef.current = { resolve, reject }
    })
  }

  const handleClose = () => {
    if (promiseRef.current) {
      promiseRef.current.reject()
    }
    setIsOpen(false)
  }

  const handleConfirm = () => {
    if (promiseRef.current) {
      promiseRef.current.resolve()
    }
    setIsOpen(false)
  }

  return (
    <>
      <ConfirmationContext.Provider value={openConfirmation}>
        {children}
        <Confirmation
          options={options}
          isOpen={isOpen}
          onConfirm={handleConfirm}
          onClose={handleClose}
        />
      </ConfirmationContext.Provider>
    </>
  )
}
