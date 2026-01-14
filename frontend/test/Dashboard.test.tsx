import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import Dashboard from '../src/components/Dashboard'

/**
 * Comprehensive tests for Dashboard component
 */

// Mock the API module
vi.mock('../services/api', () => ({
  fetchUsers: vi.fn(() => Promise.resolve({ data: [] })),
  fetchOrders: vi.fn(() => Promise.resolve({ data: [] }))
}))

const mockProps = {
  users: [],
  setUsers: vi.fn(),
  selectedUser: null,
  setSelectedUser: vi.fn(),
  loading: false,
  setLoading: vi.fn(),
  error: null,
  setError: vi.fn(),
  config: {},
  setConfig: vi.fn(),
  theme: 'light',
  setTheme: vi.fn(),
  notifications: [],
  setNotifications: vi.fn(),
  apiUrl: 'http://localhost:8080/api',
  appName: 'SonarShowcase',
  version: '1.2.0',
  maxRetries: 3,
  timeout: 5000,
  pageSize: 10,
  onHandleStuff: vi.fn(),
  onDoThing: vi.fn(),
}

describe('Dashboard Component', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render dashboard title', () => {
    render(<Dashboard {...mockProps} />)
    expect(screen.getByRole('heading', { level: 2 })).toHaveTextContent('Dashboard')
  })

  it('should display loading state', () => {
    render(<Dashboard {...mockProps} loading={true} />)
    expect(screen.getByText('Loading...')).toBeInTheDocument()
  })

  it('should display error state', () => {
    render(<Dashboard {...mockProps} error="Something went wrong" />)
    expect(screen.getByText(/Error: Something went wrong/i)).toBeInTheDocument()
  })

  it('should display total users count', () => {
    const users = [
      { id: 1, username: 'user1', email: 'user1@test.com' },
      { id: 2, username: 'user2', email: 'user2@test.com' }
    ]
    render(<Dashboard {...mockProps} users={users} />)
    expect(screen.getByText('Total Users: 2')).toBeInTheDocument()
  })

  it('should display zero users when array is empty', () => {
    render(<Dashboard {...mockProps} users={[]} />)
    expect(screen.getByText('Total Users: 0')).toBeInTheDocument()
  })

  it('should display API URL', () => {
    render(<Dashboard {...mockProps} />)
    expect(screen.getByText(/API URL:/i)).toBeInTheDocument()
  })

  it('should render refresh button', () => {
    render(<Dashboard {...mockProps} />)
    expect(screen.getByRole('button', { name: /Refresh Data/i })).toBeInTheDocument()
  })

  it('should have card class on container', () => {
    render(<Dashboard {...mockProps} />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should have stats section', () => {
    render(<Dashboard {...mockProps} />)
    expect(document.querySelector('.stats')).toBeInTheDocument()
  })
})

describe('Dashboard Component with Users', () => {
  const usersData = [
    { id: 1, username: 'john', email: 'john@test.com' },
    { id: 2, username: 'jane', email: 'jane@test.com' },
    { id: 3, username: 'bob', email: 'bob@test.com' }
  ]

  it('should render user list', () => {
    render(<Dashboard {...mockProps} users={usersData} />)
    expect(screen.getByText('john - john@test.com')).toBeInTheDocument()
    expect(screen.getByText('jane - jane@test.com')).toBeInTheDocument()
    expect(screen.getByText('bob - bob@test.com')).toBeInTheDocument()
  })

  it('should display correct user count', () => {
    render(<Dashboard {...mockProps} users={usersData} />)
    expect(screen.getByText('Total Users: 3')).toBeInTheDocument()
  })

  it('should call onHandleStuff when user is clicked', () => {
    render(<Dashboard {...mockProps} users={usersData} />)
    
    const userItem = screen.getByText('john - john@test.com')
    fireEvent.click(userItem)
    
    expect(mockProps.onHandleStuff).toHaveBeenCalledWith(usersData[0])
  })

  it('should render users in a list', () => {
    render(<Dashboard {...mockProps} users={usersData} />)
    
    const listItems = document.querySelectorAll('li')
    expect(listItems.length).toBe(3)
  })
})

describe('Dashboard Component Button Interactions', () => {
  it('should handle refresh button click', () => {
    render(<Dashboard {...mockProps} />)
    
    const refreshButton = screen.getByRole('button', { name: /Refresh Data/i })
    fireEvent.click(refreshButton)
    
    // The button should be clickable without throwing
    expect(refreshButton).toBeInTheDocument()
  })
})

describe('Dashboard Component Error States', () => {
  it('should not render main content when loading', () => {
    render(<Dashboard {...mockProps} loading={true} />)
    expect(screen.queryByRole('heading', { level: 2 })).not.toBeInTheDocument()
  })

  it('should not render main content when error', () => {
    render(<Dashboard {...mockProps} error="Error occurred" />)
    expect(screen.queryByRole('button', { name: /Refresh Data/i })).not.toBeInTheDocument()
  })

  it('should display error message correctly', () => {
    const errorMessage = 'Network request failed'
    render(<Dashboard {...mockProps} error={errorMessage} />)
    expect(screen.getByText(`Error: ${errorMessage}`)).toBeInTheDocument()
  })
})

describe('Dashboard Component Null Safety', () => {
  it('should handle null users gracefully', () => {
    render(<Dashboard {...mockProps} users={null as any} />)
    expect(screen.getByText('Total Users: 0')).toBeInTheDocument()
  })

  it('should handle undefined users gracefully', () => {
    render(<Dashboard {...mockProps} users={undefined as any} />)
    expect(screen.getByText('Total Users: 0')).toBeInTheDocument()
  })
})
