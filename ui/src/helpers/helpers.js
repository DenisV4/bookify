export const switchTabindex = (bool) => {
  return bool ? 0 : -1
}

export const camelTextToFlat = (camelText) => {
  const camelCase = camelText.replace(/([a-z])([A-Z])/g, '$1 $2').split(" ")

  let flatText = ""

  camelCase.forEach((word, index) => {
    if (index === 0) {
      flatText = flatText + word.charAt(0).toUpperCase() + word.slice(1) + " "
    } else {
      flatText = flatText + word.charAt(0).toLowerCase() + word.slice(1) + " "
    }
  })

  return flatText
}

export const objectToHtml = (obj, className) => {
  return `
          <div class="${className}">
            ${Object.keys(obj)
              .map(key => `<div><strong>${camelTextToFlat(key)}:</strong></div><div>${obj[key]}</div>`)
              .join("")}
          </div>
        `
}

export const roleToDisplay = (role) => {
  if (!role || role === undefined) {
    return role
  }

  const lowCase = role.replace("ROLE_", "").toLowerCase()
  return lowCase.charAt(0).toUpperCase() + lowCase.slice(1)
}

export const displayToRole = (role) => {
  if (!role || role === undefined) {
    return role
  }

  return `ROLE_${role.toUpperCase()}`
}

export const rolesToDisplay = (roles) => {
  if (!roles || roles === undefined) {
    return roles
  }
  return roles.map(role => roleToDisplay(role))
}

export const displayToRoles = (roles) => {
  if (!roles || roles === undefined) {
    return roles
  }
  return roles.map(role => displayToRole(role))
}

export const priceToDisplay = (price) => {
  return price.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1 ')
}

export const getNumberRange = (start, end) => {
  return Array(end - start + 1).fill().map((_, idx) => start + idx)
}

export const getDecimalPlace = (number) => {
  return number.toString().split(".")[1]?.length ?? 0
}

export const roundNumber = (number, decimals) => {
  return Math.round(number * Math.pow(10, decimals)) / Math.pow(10, decimals)
}

export const getRandomInt = (min, max) => {
  return Math.floor(Math.random() * (max - min + 1)) + min
}
