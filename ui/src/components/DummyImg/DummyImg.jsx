import { useCallback, useMemo, useState } from "react"
import cn from "classnames"

import { camelTextToFlat, getRandomInt } from "../../helpers/helpers"
import { LoadSpinner } from "../LoadSpinner/LoadSpinner"

import style from "./DummyImg.module.css"

export const DummyImg = ({ resource, className }) => {
  const [isFetching, setIsFetching] = useState(true)

  const backgroundColor = useMemo(() => Math.floor(Math.random() * 16777215).toString(16), [])
  const imageNumber = useMemo(() => getRandomInt(1, 5), [])

  const onImageLoaded = useCallback(() => setIsFetching(false), [])

  return (
    <div className={cn(className, style.image)}>
      <div className={style.wrapper}>
        <div className={cn(style.dummy, { [style.loading]: isFetching })}>
          <img
            className={style.background}
            src={`https://placehold.co/600x400/${backgroundColor}/${backgroundColor}`}
            aria-hidden
            onLoad={onImageLoaded}
          />
          <img
            className={style.picture}
            src={`/${resource}/${imageNumber}.svg`}
            alt={`${camelTextToFlat(resource.slice(0, -1))}} image`}
          />
        </div>
      </div>
      {isFetching && <LoadSpinner size="sm" />}
    </div>
  )
}
