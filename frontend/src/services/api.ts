import axios from 'axios'

/**
 * API service with missing return types
 * 
 * MNT-08: Functions without explicit return types
 */

// SEC: Hardcoded API URL
const API_URL = 'http://localhost:8080/api/v1'

// SEC: Hardcoded API key
const API_KEY = 'sk_live_sonarshowcase_api_key_12345'

// MNT: Console spam
console.log('API module loaded')
console.log('API URL:', API_URL)
console.log('API KEY:', API_KEY)

// MNT-08: No return type annotation
export const fetchUsers = async () => {
  console.log('Fetching users...')
  try {
    const response = await axios.get(`${API_URL}/users`, {
      headers: {
        'X-API-Key': API_KEY // SEC: Sending hardcoded key
      }
    })
    console.log('Users response:', response)
    return response
  } catch (error) {
    console.log('Error fetching users:', error)
    throw error
  }
}

// MNT-08: No return type
export const fetchOrders = async () => {
  console.log('Fetching orders...')
  return axios.get(`${API_URL}/orders`)
}

// MNT-08: No return type, also uses any
export const createUser = async (userData: any) => {
  console.log('Creating user:', userData)
  // SEC: Logging potentially sensitive user data
  console.log('Password:', userData.password)
  
  return axios.post(`${API_URL}/users`, userData)
}

// MNT: No return type
export const updateUser = async (id: any, userData: any) => {
  console.log('Updating user:', id, userData)
  return axios.put(`${API_URL}/users/${id}`, userData)
}

// MNT: No return type
export const deleteUser = async (id: any) => {
  console.log('Deleting user:', id)
  return axios.delete(`${API_URL}/users/${id}`)
}

// MNT: Function with any parameter and no return type
export const searchUsers = async (query: any) => {
  console.log('Searching users:', query)
  // SEC: Passing unsanitized query
  return axios.get(`${API_URL}/users/search?q=${query}`)
}

// MNT: No return type
export const processPayment = async (paymentData: any) => {
  // SEC: Logging payment data
  console.log('Processing payment:', paymentData)
  console.log('Card number:', paymentData.cardNumber)
  console.log('CVV:', paymentData.cvv)
  
  return axios.post(`${API_URL}/payments`, paymentData)
}

// MNT: Generic fetch function without types
export const fetchData = async (endpoint: any, options?: any) => {
  console.log('Fetching:', endpoint)
  return axios.get(`${API_URL}/${endpoint}`, options)
}

// MNT: Generic post without types
export const postData = async (endpoint: any, data: any) => {
  console.log('Posting to:', endpoint, data)
  return axios.post(`${API_URL}/${endpoint}`, data)
}

// MNT: Unused export
export const unusedApiFunction = () => {
  console.log('This is never used')
}

// MNT: Default export also without type
export default {
  fetchUsers,
  fetchOrders,
  createUser,
  updateUser,
  deleteUser,
  searchUsers,
  processPayment,
  fetchData,
  postData,
}

