import axios from 'axios'

/**
 * Auth service with insecure token storage
 * 
 * SEC-10: Storing JWT in localStorage (vulnerable to XSS)
 */

const API_URL = 'http://localhost:8080/api/v1/auth'

// SEC: Hardcoded credentials
const DEFAULT_USERNAME = 'admin'
const DEFAULT_PASSWORD = 'admin123'

// MNT: Console spam
console.log('AuthService loaded')

/**
 * SEC-10: Storing JWT in localStorage
 * localStorage is accessible via JavaScript, making it vulnerable to XSS attacks
 * Should use httpOnly cookies instead
 */
export const login = async (username: string, password: string) => {
  console.log('Logging in user:', username)
  console.log('Password:', password) // SEC: Logging password!
  
  try {
    const response = await axios.post(`${API_URL}/login`, {
      username,
      password
    })
    
    const { token, refreshToken, user } = response.data
    
    // SEC-10: Storing tokens in localStorage - XSS vulnerable
    localStorage.setItem('token', token)
    localStorage.setItem('refreshToken', refreshToken)
    localStorage.setItem('user', JSON.stringify(user))
    
    // SEC: Also storing in sessionStorage (slightly better but still XSS vulnerable)
    sessionStorage.setItem('accessToken', token)
    
    console.log('Stored token:', token) // SEC: Logging token
    console.log('User data:', user) // SEC: Logging user data
    
    return response.data
  } catch (error) {
    console.log('Login error:', error)
    throw error
  }
}

/**
 * SEC: Token retrieval from localStorage
 */
export const getToken = () => {
  const token = localStorage.getItem('token')
  console.log('Retrieved token:', token) // SEC: Logging token
  return token
}

/**
 * SEC: More localStorage operations
 */
export const getUser = () => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    const user = JSON.parse(userStr)
    console.log('Retrieved user:', user)
    return user
  }
  return null
}

/**
 * Logout - clears localStorage
 */
export const logout = () => {
  console.log('Logging out user')
  
  // Clearing tokens
  localStorage.removeItem('token')
  localStorage.removeItem('refreshToken')
  localStorage.removeItem('user')
  sessionStorage.removeItem('accessToken')
  
  console.log('Tokens cleared')
}

/**
 * SEC: Check if user is authenticated (reading from localStorage)
 */
export const isAuthenticated = () => {
  const token = localStorage.getItem('token')
  
  if (!token) {
    return false
  }
  
  // SEC: No proper token validation
  // Just checking if it exists, not if it's valid or expired
  console.log('User is authenticated')
  return true
}

/**
 * SEC: Storing sensitive data in localStorage
 */
export const storeUserCredentials = (username: string, password: string) => {
  // SEC: NEVER store passwords in localStorage!
  localStorage.setItem('savedUsername', username)
  localStorage.setItem('savedPassword', password) // SEC: Critical vulnerability!
  console.log('Credentials stored')
}

/**
 * SEC: Retrieving stored credentials
 */
export const getSavedCredentials = () => {
  const username = localStorage.getItem('savedUsername')
  const password = localStorage.getItem('savedPassword')
  console.log('Retrieved credentials:', username, password) // SEC: Logging credentials
  return { username, password }
}

/**
 * SEC: Auto-login with default credentials
 */
export const autoLogin = async () => {
  console.log('Auto-login with default credentials')
  // SEC: Using hardcoded credentials
  return login(DEFAULT_USERNAME, DEFAULT_PASSWORD)
}

/**
 * Set auth header for axios
 */
export const setAuthHeader = () => {
  const token = localStorage.getItem('token')
  if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    console.log('Auth header set:', axios.defaults.headers.common['Authorization'])
  }
}

export default {
  login,
  logout,
  getToken,
  getUser,
  isAuthenticated,
  storeUserCredentials,
  getSavedCredentials,
  autoLogin,
  setAuthHeader,
}

