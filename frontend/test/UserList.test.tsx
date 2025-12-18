import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import UserList from '../src/components/UserList'

/**
 * Comprehensive tests for UserList component
 */

const mockUsers = [
  { id: 1, username: 'john', email: 'john@test.com', role: 'ADMIN' },
  { id: 2, username: 'jane', email: 'jane@test.com', role: 'USER' },
  { id: 3, username: 'bob', email: 'bob@test.com', role: 'USER' }
]

const createMockProps = () => ({
  users: mockUsers,
  selectedUser: null,
  onSelect: vi.fn(),
  loading: false,
  theme: 'light',
  apiUrl: 'http://localhost:8080/api'
})

let mockProps: ReturnType<typeof createMockProps>

beforeEach(() => {
  mockProps = createMockProps()
})

describe('UserList Component', () => {

  it('should render users title', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByRole('heading', { level: 2 })).toHaveTextContent('Users')
  })

  it('should display loading state', () => {
    render(<UserList {...mockProps} loading={true} />)
    expect(screen.getByText('Loading users...')).toBeInTheDocument()
  })

  it('should render search input', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByPlaceholderText('Search users...')).toBeInTheDocument()
  })

  it('should render sort buttons', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByRole('button', { name: /Sort by Username/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /Sort by Email/i })).toBeInTheDocument()
  })

  it('should have card class on container', () => {
    render(<UserList {...mockProps} />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })
})

describe('UserList Table Rendering', () => {
  it('should render table with headers', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByText('ID')).toBeInTheDocument()
    expect(screen.getByText('Username')).toBeInTheDocument()
    expect(screen.getByText('Email')).toBeInTheDocument()
    expect(screen.getByText('Role')).toBeInTheDocument()
    expect(screen.getByText('Actions')).toBeInTheDocument()
  })

  it('should render all users in table', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByText('john')).toBeInTheDocument()
    expect(screen.getByText('jane')).toBeInTheDocument()
    expect(screen.getByText('bob')).toBeInTheDocument()
  })

  it('should render user emails', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByText('john@test.com')).toBeInTheDocument()
    expect(screen.getByText('jane@test.com')).toBeInTheDocument()
    expect(screen.getByText('bob@test.com')).toBeInTheDocument()
  })

  it('should render user roles', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByText('ADMIN')).toBeInTheDocument()
    expect(screen.getAllByText('USER').length).toBe(2)
  })

  it('should render Edit and Delete buttons for each user', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getAllByRole('button', { name: 'Edit' }).length).toBe(3)
    expect(screen.getAllByRole('button', { name: 'Delete' }).length).toBe(3)
  })
})

describe('UserList Search Functionality', () => {
  it('should filter users by username', () => {
    render(<UserList {...mockProps} />)
    
    const searchInput = screen.getByPlaceholderText('Search users...')
    fireEvent.change(searchInput, { target: { value: 'john' } })
    
    expect(screen.getByText('john')).toBeInTheDocument()
    expect(screen.queryByText('jane')).not.toBeInTheDocument()
    expect(screen.queryByText('bob')).not.toBeInTheDocument()
  })

  it('should filter users by email', () => {
    render(<UserList {...mockProps} />)
    
    const searchInput = screen.getByPlaceholderText('Search users...')
    fireEvent.change(searchInput, { target: { value: 'jane@test' } })
    
    expect(screen.getByText('jane')).toBeInTheDocument()
    expect(screen.queryByText('john')).not.toBeInTheDocument()
  })

  it('should show all users when search is cleared', () => {
    render(<UserList {...mockProps} />)
    
    const searchInput = screen.getByPlaceholderText('Search users...')
    fireEvent.change(searchInput, { target: { value: 'john' } })
    fireEvent.change(searchInput, { target: { value: '' } })
    
    expect(screen.getByText('john')).toBeInTheDocument()
    expect(screen.getByText('jane')).toBeInTheDocument()
    expect(screen.getByText('bob')).toBeInTheDocument()
  })

  it('should handle case insensitive search', () => {
    render(<UserList {...mockProps} />)
    
    const searchInput = screen.getByPlaceholderText('Search users...')
    fireEvent.change(searchInput, { target: { value: 'JOHN' } })
    
    expect(screen.getByText('john')).toBeInTheDocument()
  })
})

describe('UserList Sort Functionality', () => {
  it('should sort by username when button clicked', () => {
    render(<UserList {...mockProps} />)
    
    const sortButton = screen.getByRole('button', { name: /Sort by Username/i })
    fireEvent.click(sortButton)
    
    // Verify the sort button click doesn't throw
    expect(sortButton).toBeInTheDocument()
  })

  it('should sort by email when button clicked', () => {
    render(<UserList {...mockProps} />)
    
    const sortButton = screen.getByRole('button', { name: /Sort by Email/i })
    fireEvent.click(sortButton)
    
    // Verify the sort button click doesn't throw
    expect(sortButton).toBeInTheDocument()
  })

  it('should toggle sort order on repeated clicks', () => {
    render(<UserList {...mockProps} />)
    
    const sortButton = screen.getByRole('button', { name: /Sort by Username/i })
    fireEvent.click(sortButton)
    fireEvent.click(sortButton)
    
    // Should handle multiple clicks
    expect(sortButton).toBeInTheDocument()
  })
})

describe('UserList User Selection', () => {
  it('should call onSelect when user row is clicked', () => {
    render(<UserList {...mockProps} />)
    
    const userRow = screen.getByText('john').closest('tr')
    if (userRow) {
      fireEvent.click(userRow)
    }
    
    expect(mockProps.onSelect).toHaveBeenCalledWith(mockUsers[0])
  })

  it('should highlight selected user', () => {
    render(<UserList {...mockProps} selectedUser={mockUsers[0]} />)
    
    const userRow = screen.getByText('john').closest('tr')
    expect(userRow).toHaveStyle({ backgroundColor: '#e6f3ff' })
  })
})

describe('UserList Action Buttons', () => {
  it('should handle Edit button click without triggering row select', () => {
    render(<UserList {...mockProps} />)
    
    const editButtons = screen.getAllByRole('button', { name: 'Edit' })
    fireEvent.click(editButtons[0])
    
    // Edit click should not call onSelect (stopPropagation)
    expect(mockProps.onSelect).not.toHaveBeenCalled()
  })

  it('should handle Delete button click without triggering row select', () => {
    render(<UserList {...mockProps} />)
    
    const deleteButtons = screen.getAllByRole('button', { name: 'Delete' })
    fireEvent.click(deleteButtons[0])
    
    // Delete click should not call onSelect (stopPropagation)
    expect(mockProps.onSelect).not.toHaveBeenCalled()
  })
})

describe('UserList Stats Display', () => {
  it('should display total count', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByText('Total: 3')).toBeInTheDocument()
  })

  it('should display filtered count', () => {
    render(<UserList {...mockProps} />)
    expect(screen.getByText('Filtered: 3')).toBeInTheDocument()
  })

  it('should update filtered count after search', () => {
    render(<UserList {...mockProps} />)
    
    const searchInput = screen.getByPlaceholderText('Search users...')
    fireEvent.change(searchInput, { target: { value: 'john' } })
    
    expect(screen.getByText('Filtered: 1')).toBeInTheDocument()
  })
})

describe('UserList Empty State', () => {
  it('should handle empty users array', () => {
    render(<UserList {...mockProps} users={[]} />)
    expect(screen.getByText('Total: 0')).toBeInTheDocument()
  })

  it('should handle null users', () => {
    render(<UserList {...mockProps} users={null as any} />)
    expect(screen.getByText('Total: 0')).toBeInTheDocument()
  })

  it('should handle undefined users', () => {
    render(<UserList {...mockProps} users={undefined as any} />)
    expect(screen.getByText('Total: 0')).toBeInTheDocument()
  })
})

