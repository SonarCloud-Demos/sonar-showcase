import axios from 'axios'
import { getToken } from './authService'

/**
 * User service with console.log spam
 * 
 * MNT-09: Debug console.log statements left in production code
 */

const API_URL = 'http://localhost:8080/api/v1/users'

// MNT-09: Console spam starts here
console.log('UserService module loaded')
console.log('API URL:', API_URL)

/**
 * MNT-09: Excessive console.log statements
 */
export const getUsers = async () => {
  console.log('=== getUsers called ===')
  console.log('Timestamp:', new Date().toISOString())
  console.log('Token:', getToken())
  
  try {
    console.log('Making request to:', API_URL)
    const response = await axios.get(API_URL)
    
    console.log('Response status:', response.status)
    console.log('Response headers:', response.headers)
    console.log('Response data:', response.data)
    console.log('Number of users:', response.data?.length)
    
    return response.data
  } catch (error: any) {
    console.log('Error in getUsers:', error)
    console.log('Error message:', error.message)
    console.log('Error stack:', error.stack)
    throw error
  }
}

export const getUserById = async (id: any) => {
  console.log('=== getUserById called ===')
  console.log('User ID:', id)
  console.log('ID type:', typeof id)
  
  try {
    console.log('Fetching user with ID:', id)
    const response = await axios.get(`${API_URL}/${id}`)
    
    console.log('User found:', response.data)
    console.log('User name:', response.data?.username)
    console.log('User email:', response.data?.email)
    
    return response.data
  } catch (error: any) {
    console.log('Error fetching user:', id)
    console.log('Error details:', error)
    throw error
  }
}

export const createUser = async (userData: any) => {
  console.log('=== createUser called ===')
  console.log('User data:', userData)
  console.log('Username:', userData.username)
  console.log('Email:', userData.email)
  console.log('Password:', userData.password) // SEC: Logging password!
  console.log('Role:', userData.role)
  
  try {
    console.log('Sending POST request to:', API_URL)
    const response = await axios.post(API_URL, userData)
    
    console.log('User created successfully')
    console.log('Created user:', response.data)
    console.log('New user ID:', response.data?.id)
    
    return response.data
  } catch (error: any) {
    console.log('Error creating user')
    console.log('Error:', error.message)
    console.log('Request data was:', userData)
    throw error
  }
}

export const updateUser = async (id: any, userData: any) => {
  console.log('=== updateUser called ===')
  console.log('User ID:', id)
  console.log('Update data:', userData)
  
  try {
    console.log('Sending PUT request')
    const response = await axios.put(`${API_URL}/${id}`, userData)
    
    console.log('User updated')
    console.log('Updated user:', response.data)
    
    return response.data
  } catch (error: any) {
    console.log('Update failed for user:', id)
    console.log('Error:', error)
    throw error
  }
}

export const deleteUser = async (id: any) => {
  console.log('=== deleteUser called ===')
  console.log('Deleting user:', id)
  
  try {
    console.log('Sending DELETE request')
    await axios.delete(`${API_URL}/${id}`)
    
    console.log('User deleted successfully')
    console.log('Deleted user ID:', id)
    
    return true
  } catch (error: any) {
    console.log('Delete failed')
    console.log('Error:', error)
    throw error
  }
}

export const searchUsers = async (query: any) => {
  console.log('=== searchUsers called ===')
  console.log('Search query:', query)
  console.log('Query type:', typeof query)
  console.log('Query length:', query?.length)
  
  try {
    console.log('Making search request')
    const response = await axios.get(`${API_URL}/search`, { params: { q: query } })
    
    console.log('Search results:', response.data)
    console.log('Found users:', response.data?.length)
    
    return response.data
  } catch (error: any) {
    console.log('Search failed')
    console.log('Query was:', query)
    console.log('Error:', error)
    throw error
  }
}

// MNT: Debug helper function
export const debugUser = (user: any) => {
  console.log('=== DEBUG USER ===')
  console.log('ID:', user?.id)
  console.log('Username:', user?.username)
  console.log('Email:', user?.email)
  console.log('Password:', user?.password) // SEC: Logging password
  console.log('Role:', user?.role)
  console.log('Created:', user?.createdAt)
  console.log('Full object:', JSON.stringify(user, null, 2))
  console.log('==================')
}

export default {
  getUsers,
  getUserById,
  createUser,
  updateUser,
  deleteUser,
  searchUsers,
  debugUser,
}

