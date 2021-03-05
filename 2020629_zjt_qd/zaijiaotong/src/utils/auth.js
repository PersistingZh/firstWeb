// import Cookies from 'js-cookie'

const TokenKey = 'gbfw_token'
const RefTokenKey = 'gbfw_ref_token'

export function getToken() {
  return window.localStorage.getItem(TokenKey)
}

export function setToken(token) {
  return window.localStorage.setItem(TokenKey, token)
}

export function removeToken() {
  return window.localStorage.removeItem(TokenKey)
}

export function getRefToken() {
  return window.localStorage.getItem(RefTokenKey)
}

export function setRefToken(token) {
  return window.localStorage.setItem(RefTokenKey, token)
}

export function removeRefToken() {
  return window.localStorage.removeItem(RefTokenKey)
}

// export function getToken() {
//   return Cookies.get(TokenKey)
// }

// export function setToken(token) {
//   return Cookies.set(TokenKey, token)
// }

// export function removeToken() {
//   return Cookies.remove(TokenKey)
// }

// export function getRefToken() {
//   return Cookies.get(RefTokenKey)
// }

// export function setRefToken(token) {
//   return Cookies.set(RefTokenKey, token)
// }

// export function removeRefToken() {
//   return Cookies.remove(RefTokenKey)
// }
