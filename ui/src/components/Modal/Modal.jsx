import { createPortal } from "react-dom"
import { useEffect, useRef } from "react"
import { CSSTransition } from "react-transition-group"
import { CustomScroll } from "react-custom-scroll"
import FocusTrap from "focus-trap-react"

import { useOutsideClick } from "../../hooks/useOutsideClick.js"
import { useKeyDown } from "../../hooks/useKeyDown.js"
import { useScrollLock } from "../../hooks/useScrollLock.js"
import { CrossButton } from "../buttons/CrossButton/CrossButton"

import style from "./Modal.module.css"

export const Modal = ({ children, isOpen, onClose }) => {
  const overlayRef = useRef(null)
  const contentRef = useRef(null)

  const { lockScroll, unlockScroll } = useScrollLock()

  useOutsideClick(contentRef, () => { onClose() })

  useKeyDown("Escape", () => { onClose() })

  useEffect(() => {
    if (isOpen) {
      lockScroll()
    } else {
      unlockScroll()
    }
  }, [isOpen, lockScroll, unlockScroll])

  return createPortal(
    <CSSTransition
      classNames={{
        enter: style.enter,
        enterActive: style.enterActive,
        exit: style.exit,
        exitActive: style.exitActive,
      }}
      nodeRef={overlayRef}
      in={isOpen}
      timeout={300}
      unmountOnExit={true}
    >
      <div className={style.overlay} ref={overlayRef}>
        <div className={style.modal}>
          <FocusTrap focusTrapOptions={{ allowOutsideClick: true }}>
            <div className={style.content} ref={contentRef}>
              <CustomScroll>
                <div className={style.wrapper} tabIndex={0}>
                  {children}
                </div>
              </CustomScroll>
              <CrossButton className={style.close} onClick={onClose} />
            </div>
          </FocusTrap>
        </div>
      </div>
    </CSSTransition>,
    document.getElementById("modals")
  )
}
