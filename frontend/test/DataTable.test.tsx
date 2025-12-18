import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import DataTable from '../src/components/DataTable'

/**
 * Comprehensive tests for DataTable component
 */

const mockUsers = [
  { id: 1, username: 'john', name: 'John', value: 10, val: 10 },
  { id: 2, username: 'jane', name: 'Jane', value: 20, val: 20 },
  { id: 3, username: 'bob', name: 'Bob', value: 30, val: 30 },
  { id: 4, username: 'alice', name: 'Alice', value: 40, val: 40 },
  { id: 5, username: 'charlie', name: 'Charlie', value: 50, val: 50 }
]

const mockProps = {
  users: mockUsers,
  loading: false,
  pageSize: 3
}

describe('DataTable Component', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render data table title', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByRole('heading', { level: 2 })).toHaveTextContent('Data Table')
  })

  it('should display loading state', () => {
    render(<DataTable {...mockProps} loading={true} />)
    expect(screen.getByText('Loading...')).toBeInTheDocument()
  })

  it('should have card class on container', () => {
    render(<DataTable {...mockProps} />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should render search input', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByPlaceholderText('Search...')).toBeInTheDocument()
  })
})

describe('DataTable Table Rendering', () => {
  it('should render table', () => {
    render(<DataTable {...mockProps} />)
    expect(document.querySelector('table')).toBeInTheDocument()
  })

  it('should render table headers', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText('#')).toBeInTheDocument()
    expect(screen.getByText('Data')).toBeInTheDocument()
    expect(screen.getByText('Val')).toBeInTheDocument()
    expect(screen.getByText('Act')).toBeInTheDocument()
  })

  it('should render first page of users', () => {
    render(<DataTable {...mockProps} />)
    // With pageSize 3, first 3 users should be visible
    expect(screen.getByText('john')).toBeInTheDocument()
    expect(screen.getByText('jane')).toBeInTheDocument()
    expect(screen.getByText('bob')).toBeInTheDocument()
  })

  it('should not render users beyond page size', () => {
    render(<DataTable {...mockProps} />)
    // alice and charlie should not be on first page
    expect(screen.queryByText('alice')).not.toBeInTheDocument()
    expect(screen.queryByText('charlie')).not.toBeInTheDocument()
  })

  it('should render Do buttons for each row', () => {
    render(<DataTable {...mockProps} />)
    const doButtons = screen.getAllByRole('button', { name: 'Do' })
    expect(doButtons.length).toBe(3) // pageSize
  })
})

describe('DataTable Pagination', () => {
  it('should render pagination controls', () => {
    render(<DataTable {...mockProps} />)
    expect(document.querySelector('.pagination')).toBeInTheDocument()
  })

  it('should render Prev button', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByRole('button', { name: 'Prev' })).toBeInTheDocument()
  })

  it('should render Next button', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByRole('button', { name: 'Next' })).toBeInTheDocument()
  })

  it('should show page number', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText('Pg 1')).toBeInTheDocument()
  })

  it('should disable Prev button on first page', () => {
    render(<DataTable {...mockProps} />)
    const prevButton = screen.getByRole('button', { name: 'Prev' })
    expect(prevButton).toBeDisabled()
  })

  it('should go to next page when Next clicked', () => {
    render(<DataTable {...mockProps} />)
    
    const nextButton = screen.getByRole('button', { name: 'Next' })
    fireEvent.click(nextButton)
    
    expect(screen.getByText('Pg 2')).toBeInTheDocument()
    expect(screen.getByText('alice')).toBeInTheDocument()
  })

  it('should go back when Prev clicked', () => {
    render(<DataTable {...mockProps} />)
    
    const nextButton = screen.getByRole('button', { name: 'Next' })
    fireEvent.click(nextButton)
    
    const prevButton = screen.getByRole('button', { name: 'Prev' })
    fireEvent.click(prevButton)
    
    expect(screen.getByText('Pg 1')).toBeInTheDocument()
  })

  it('should enable Prev button after going to page 2', () => {
    render(<DataTable {...mockProps} />)
    
    const nextButton = screen.getByRole('button', { name: 'Next' })
    fireEvent.click(nextButton)
    
    const prevButton = screen.getByRole('button', { name: 'Prev' })
    expect(prevButton).not.toBeDisabled()
  })
})

describe('DataTable Row Interactions', () => {
  it('should handle row click', () => {
    render(<DataTable {...mockProps} />)
    
    const row = screen.getByText('john').closest('tr')
    if (row) {
      expect(() => fireEvent.click(row)).not.toThrow()
    }
  })

  it('should handle Do button click', () => {
    render(<DataTable {...mockProps} />)
    
    const doButtons = screen.getAllByRole('button', { name: 'Do' })
    expect(() => fireEvent.click(doButtons[0])).not.toThrow()
  })
})

describe('DataTable Search', () => {
  it('should update search input value', () => {
    render(<DataTable {...mockProps} />)
    
    const searchInput = screen.getByPlaceholderText('Search...') as HTMLInputElement
    fireEvent.change(searchInput, { target: { value: 'test' } })
    
    expect(searchInput.value).toBe('test')
  })
})

describe('DataTable Debug Info', () => {
  it('should display x value', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText(/x: 0/)).toBeInTheDocument()
  })

  it('should display y value', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText(/y: 0/)).toBeInTheDocument()
  })

  it('should display flag value', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText(/flag:/)).toBeInTheDocument()
  })

  it('should display num value', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText(/num: 0/)).toBeInTheDocument()
  })

  it('should display ttl value', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText(/ttl:/)).toBeInTheDocument()
  })
})

describe('DataTable Empty State', () => {
  it('should handle empty users array', () => {
    render(<DataTable users={[]} loading={false} pageSize={10} />)
    expect(document.querySelector('table')).toBeInTheDocument()
  })

  it('should handle null users', () => {
    render(<DataTable users={null as any} loading={false} pageSize={10} />)
    expect(document.querySelector('table')).toBeInTheDocument()
  })

  it('should handle undefined users', () => {
    render(<DataTable users={undefined as any} loading={false} pageSize={10} />)
    expect(document.querySelector('table')).toBeInTheDocument()
  })
})

describe('DataTable Values Display', () => {
  it('should display user values', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText('10')).toBeInTheDocument()
    expect(screen.getByText('20')).toBeInTheDocument()
    expect(screen.getByText('30')).toBeInTheDocument()
  })

  it('should display row numbers', () => {
    render(<DataTable {...mockProps} />)
    expect(screen.getByText('1')).toBeInTheDocument()
    expect(screen.getByText('2')).toBeInTheDocument()
    expect(screen.getByText('3')).toBeInTheDocument()
  })
})

