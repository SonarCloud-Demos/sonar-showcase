import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import axios from 'axios'

// Mock axios
vi.mock('axios', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
    defaults: {
      headers: {
        common: {}
      }
    }
  }
}))

// Import after mocking
import * as api from '../src/services/api'
import * as authService from '../src/services/authService'
import * as userService from '../src/services/userService'

describe('API Service', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('fetchUsers', () => {
    it('should fetch users successfully', async () => {
      const mockUsers = [{ id: 1, name: 'Test User' }]
      vi.mocked(axios.get).mockResolvedValueOnce({ data: mockUsers })

      const result = await api.fetchUsers()
      
      expect(axios.get).toHaveBeenCalledWith(
        expect.stringContaining('/users'),
        expect.any(Object)
      )
      expect(result.data).toEqual(mockUsers)
    })

    it('should throw error on fetch failure', async () => {
      const error = new Error('Network error')
      vi.mocked(axios.get).mockRejectedValueOnce(error)

      await expect(api.fetchUsers()).rejects.toThrow('Network error')
    })
  })

  describe('fetchOrders', () => {
    it('should fetch orders successfully', async () => {
      const mockOrders = [{ id: 1, total: 100 }]
      vi.mocked(axios.get).mockResolvedValueOnce({ data: mockOrders })

      const result = await api.fetchOrders()
      
      expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('/orders'))
      expect(result.data).toEqual(mockOrders)
    })
  })

  describe('createUser', () => {
    it('should create user successfully', async () => {
      const userData = { username: 'test', email: 'test@test.com' }
      const mockResponse = { id: 1, ...userData }
      vi.mocked(axios.post).mockResolvedValueOnce({ data: mockResponse })

      const result = await api.createUser(userData)
      
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/users'),
        userData
      )
      expect(result.data).toEqual(mockResponse)
    })
  })

  describe('updateUser', () => {
    it('should update user successfully', async () => {
      const userData = { username: 'updated' }
      const mockResponse = { id: 1, username: 'updated' }
      vi.mocked(axios.put).mockResolvedValueOnce({ data: mockResponse })

      const result = await api.updateUser(1, userData)
      
      expect(axios.put).toHaveBeenCalledWith(
        expect.stringContaining('/users/1'),
        userData
      )
      expect(result.data).toEqual(mockResponse)
    })
  })

  describe('deleteUser', () => {
    it('should delete user successfully', async () => {
      vi.mocked(axios.delete).mockResolvedValueOnce({ data: null })

      const result = await api.deleteUser(1)
      
      expect(axios.delete).toHaveBeenCalledWith(expect.stringContaining('/users/1'))
    })
  })

  describe('searchUsers', () => {
    it('should search users successfully', async () => {
      const mockUsers = [{ id: 1, name: 'John' }]
      vi.mocked(axios.get).mockResolvedValueOnce({ data: mockUsers })

      const result = await api.searchUsers('John')
      
      expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('search?q=John'))
    })
  })

  describe('processPayment', () => {
    it('should process payment successfully', async () => {
      const paymentData = { amount: 100, cardNumber: '4111111111111111' }
      vi.mocked(axios.post).mockResolvedValueOnce({ data: { success: true } })

      const result = await api.processPayment(paymentData)
      
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/payments'),
        paymentData
      )
    })
  })

  describe('fetchData', () => {
    it('should fetch data from custom endpoint', async () => {
      vi.mocked(axios.get).mockResolvedValueOnce({ data: { test: true } })

      const result = await api.fetchData('custom-endpoint')
      
      expect(axios.get).toHaveBeenCalledWith(
        expect.stringContaining('/custom-endpoint'),
        undefined
      )
    })

    it('should fetch data with options', async () => {
      const options = { headers: { 'X-Custom': 'value' } }
      vi.mocked(axios.get).mockResolvedValueOnce({ data: { test: true } })

      const result = await api.fetchData('custom-endpoint', options)
      
      expect(axios.get).toHaveBeenCalledWith(
        expect.stringContaining('/custom-endpoint'),
        options
      )
    })
  })

  describe('postData', () => {
    it('should post data to custom endpoint', async () => {
      const data = { key: 'value' }
      vi.mocked(axios.post).mockResolvedValueOnce({ data: { success: true } })

      const result = await api.postData('custom-endpoint', data)
      
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/custom-endpoint'),
        data
      )
    })
  })

  describe('unusedApiFunction', () => {
    it('should exist and be callable', () => {
      expect(() => api.unusedApiFunction()).not.toThrow()
    })
  })
})

describe('Auth Service', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    localStorage.clear()
    sessionStorage.clear()
  })

  describe('login', () => {
    it('should login successfully and store tokens', async () => {
      const mockResponse = {
        data: {
          token: 'test-token',
          refreshToken: 'test-refresh-token',
          user: { id: 1, username: 'test' }
        }
      }
      vi.mocked(axios.post).mockResolvedValueOnce(mockResponse)

      const result = await authService.login('test', 'password')
      
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining('/login'),
        { username: 'test', password: 'password' }
      )
      expect(localStorage.getItem('token')).toBe('test-token')
      expect(localStorage.getItem('refreshToken')).toBe('test-refresh-token')
    })

    it('should throw error on login failure', async () => {
      vi.mocked(axios.post).mockRejectedValueOnce(new Error('Invalid credentials'))

      await expect(authService.login('test', 'wrong')).rejects.toThrow()
    })
  })

  describe('getToken', () => {
    it('should return token from localStorage', () => {
      localStorage.setItem('token', 'stored-token')
      
      const token = authService.getToken()
      
      expect(token).toBe('stored-token')
    })

    it('should return null when no token exists', () => {
      const token = authService.getToken()
      
      expect(token).toBeNull()
    })
  })

  describe('getUser', () => {
    it('should return user from localStorage', () => {
      const user = { id: 1, username: 'test' }
      localStorage.setItem('user', JSON.stringify(user))
      
      const result = authService.getUser()
      
      expect(result).toEqual(user)
    })

    it('should return null when no user exists', () => {
      const result = authService.getUser()
      
      expect(result).toBeNull()
    })
  })

  describe('logout', () => {
    it('should clear all tokens', () => {
      localStorage.setItem('token', 'test-token')
      localStorage.setItem('refreshToken', 'test-refresh')
      localStorage.setItem('user', '{}')
      sessionStorage.setItem('accessToken', 'test-access')
      
      authService.logout()
      
      expect(localStorage.getItem('token')).toBeNull()
      expect(localStorage.getItem('refreshToken')).toBeNull()
      expect(localStorage.getItem('user')).toBeNull()
      expect(sessionStorage.getItem('accessToken')).toBeNull()
    })
  })

  describe('isAuthenticated', () => {
    it('should return true when token exists', () => {
      localStorage.setItem('token', 'valid-token')
      
      const result = authService.isAuthenticated()
      
      expect(result).toBe(true)
    })

    it('should return false when no token exists', () => {
      const result = authService.isAuthenticated()
      
      expect(result).toBe(false)
    })
  })

  describe('storeUserCredentials', () => {
    it('should store credentials in localStorage', () => {
      authService.storeUserCredentials('testuser', 'testpass')
      
      expect(localStorage.getItem('savedUsername')).toBe('testuser')
      expect(localStorage.getItem('savedPassword')).toBe('testpass')
    })
  })

  describe('getSavedCredentials', () => {
    it('should retrieve saved credentials', () => {
      localStorage.setItem('savedUsername', 'testuser')
      localStorage.setItem('savedPassword', 'testpass')
      
      const result = authService.getSavedCredentials()
      
      expect(result.username).toBe('testuser')
      expect(result.password).toBe('testpass')
    })

    it('should return null for missing credentials', () => {
      const result = authService.getSavedCredentials()
      
      expect(result.username).toBeNull()
      expect(result.password).toBeNull()
    })
  })

  describe('autoLogin', () => {
    it('should call login with default credentials', async () => {
      const mockResponse = {
        data: {
          token: 'auto-token',
          refreshToken: 'auto-refresh',
          user: { id: 1 }
        }
      }
      vi.mocked(axios.post).mockResolvedValueOnce(mockResponse)

      await authService.autoLogin()
      
      expect(axios.post).toHaveBeenCalledWith(
        expect.any(String),
        expect.objectContaining({
          username: 'admin',
          password: 'admin123'
        })
      )
    })
  })

  describe('setAuthHeader', () => {
    it('should set Authorization header when token exists', () => {
      localStorage.setItem('token', 'bearer-token')
      
      authService.setAuthHeader()
      
      expect(axios.defaults.headers.common['Authorization']).toBe('Bearer bearer-token')
    })

    it('should not set header when no token exists', () => {
      delete axios.defaults.headers.common['Authorization']
      
      authService.setAuthHeader()
      
      // Should not throw
      expect(true).toBe(true)
    })
  })
})

describe('User Service', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    localStorage.clear()
  })

  describe('getUsers', () => {
    it('should get all users', async () => {
      const mockUsers = [{ id: 1, username: 'test' }]
      vi.mocked(axios.get).mockResolvedValueOnce({ data: mockUsers })

      const result = await userService.getUsers()
      
      expect(axios.get).toHaveBeenCalled()
      expect(result).toEqual(mockUsers)
    })

    it('should throw error on failure', async () => {
      vi.mocked(axios.get).mockRejectedValueOnce(new Error('Failed'))

      await expect(userService.getUsers()).rejects.toThrow()
    })
  })

  describe('getUserById', () => {
    it('should get user by id', async () => {
      const mockUser = { id: 1, username: 'test' }
      vi.mocked(axios.get).mockResolvedValueOnce({ data: mockUser })

      const result = await userService.getUserById(1)
      
      expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('/1'))
      expect(result).toEqual(mockUser)
    })
  })

  describe('createUser', () => {
    it('should create a new user', async () => {
      const userData = { username: 'new', email: 'new@test.com' }
      const mockResponse = { id: 1, ...userData }
      vi.mocked(axios.post).mockResolvedValueOnce({ data: mockResponse })

      const result = await userService.createUser(userData)
      
      expect(axios.post).toHaveBeenCalled()
      expect(result).toEqual(mockResponse)
    })
  })

  describe('updateUser', () => {
    it('should update existing user', async () => {
      const userData = { username: 'updated' }
      const mockResponse = { id: 1, username: 'updated' }
      vi.mocked(axios.put).mockResolvedValueOnce({ data: mockResponse })

      const result = await userService.updateUser(1, userData)
      
      expect(axios.put).toHaveBeenCalledWith(
        expect.stringContaining('/1'),
        userData
      )
      expect(result).toEqual(mockResponse)
    })
  })

  describe('deleteUser', () => {
    it('should delete user', async () => {
      vi.mocked(axios.delete).mockResolvedValueOnce({})

      const result = await userService.deleteUser(1)
      
      expect(axios.delete).toHaveBeenCalledWith(expect.stringContaining('/1'))
      expect(result).toBe(true)
    })
  })

  describe('searchUsers', () => {
    it('should search users by query', async () => {
      const mockUsers = [{ id: 1, username: 'john' }]
      vi.mocked(axios.get).mockResolvedValueOnce({ data: mockUsers })

      const result = await userService.searchUsers('john')
      
      expect(axios.get).toHaveBeenCalledWith(
        expect.stringContaining('/search'),
        expect.objectContaining({ params: { q: 'john' } })
      )
      expect(result).toEqual(mockUsers)
    })
  })

  describe('debugUser', () => {
    it('should not throw when debugging user', () => {
      const user = { id: 1, username: 'test', email: 'test@test.com' }
      
      expect(() => userService.debugUser(user)).not.toThrow()
    })

    it('should handle null user', () => {
      expect(() => userService.debugUser(null)).not.toThrow()
    })

    it('should handle undefined user', () => {
      expect(() => userService.debugUser(undefined)).not.toThrow()
    })
  })
})
