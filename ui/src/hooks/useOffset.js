/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";

export const useOffset = (ref) => {
  const [offset, setOffset] = useState({
    top: null,
    bottom: null,
    left: null,
    right: null,
  });

  const setValues = () => {
    if (ref.current) {
      setOffset({
        top: ref.current.offsetTop,
        bottom: window.innerHeight - ref.current.offsetTop + ref.current.offsetHeight,
        left: ref.current.offsetLeft,
        right: window.innerWidth - ref.current.offsetLeft - ref.current.offsetWidth,
      })
    }
  }

  useEffect(() => {
    setValues()
  }, [ref])

  useEffect(() => {
    const handleResize = () => setValues()
    window.addEventListener("resize", handleResize)

    return () => window.removeEventListener("resize", handleResize);
  })

  return offset;
}
