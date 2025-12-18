import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import CommentDisplay from '../src/components/CommentDisplay'

/**
 * Comprehensive tests for CommentDisplay component
 */

const mockProps = {
  theme: 'light',
  config: {}
}

describe('CommentDisplay Component', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render comments title', () => {
    render(<CommentDisplay {...mockProps} />)
    expect(screen.getByRole('heading', { level: 2 })).toHaveTextContent('Comments')
  })

  it('should have card class on container', () => {
    render(<CommentDisplay {...mockProps} />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should render comments list', () => {
    render(<CommentDisplay {...mockProps} />)
    expect(document.querySelector('.comments-list')).toBeInTheDocument()
  })

  it('should render add comment section', () => {
    render(<CommentDisplay {...mockProps} />)
    expect(screen.getByText('Add Comment (supports HTML!)')).toBeInTheDocument()
  })

  it('should render textarea for new comment', () => {
    render(<CommentDisplay {...mockProps} />)
    expect(screen.getByPlaceholderText('Enter your comment (HTML allowed)...')).toBeInTheDocument()
  })

  it('should render Post Comment button', () => {
    render(<CommentDisplay {...mockProps} />)
    expect(screen.getByRole('button', { name: 'Post Comment' })).toBeInTheDocument()
  })

  it('should render preview section', () => {
    render(<CommentDisplay {...mockProps} />)
    expect(screen.getByText('Preview:')).toBeInTheDocument()
  })
})

describe('CommentDisplay Initial Comments', () => {
  it('should render initial comments after mount', async () => {
    render(<CommentDisplay {...mockProps} />)
    
    // Wait for useEffect to run
    expect(await screen.findByText('User1')).toBeInTheDocument()
    expect(screen.getByText('Attacker')).toBeInTheDocument()
    expect(screen.getByText('Hacker')).toBeInTheDocument()
  })

  it('should render comment authors', async () => {
    render(<CommentDisplay {...mockProps} />)
    
    // Wait for initial comments to render
    await screen.findByText('User1')
    
    const strongElements = document.querySelectorAll('strong')
    expect(strongElements.length).toBeGreaterThan(0)
  })
})

describe('CommentDisplay Input Handling', () => {
  it('should update textarea value on change', () => {
    render(<CommentDisplay {...mockProps} />)
    
    const textarea = screen.getByPlaceholderText('Enter your comment (HTML allowed)...') as HTMLTextAreaElement
    fireEvent.change(textarea, { target: { value: 'Test comment' } })
    
    expect(textarea.value).toBe('Test comment')
  })

  it('should clear textarea after posting comment', () => {
    render(<CommentDisplay {...mockProps} />)
    
    const textarea = screen.getByPlaceholderText('Enter your comment (HTML allowed)...') as HTMLTextAreaElement
    fireEvent.change(textarea, { target: { value: 'Test comment' } })
    
    const postButton = screen.getByRole('button', { name: 'Post Comment' })
    fireEvent.click(postButton)
    
    expect(textarea.value).toBe('')
  })

  it('should add new comment to list when posted', async () => {
    render(<CommentDisplay {...mockProps} />)
    
    // Wait for initial comments
    await screen.findByText('User1')
    
    const textarea = screen.getByPlaceholderText('Enter your comment (HTML allowed)...')
    fireEvent.change(textarea, { target: { value: 'My new comment' } })
    
    const postButton = screen.getByRole('button', { name: 'Post Comment' })
    fireEvent.click(postButton)
    
    expect(await screen.findByText('CurrentUser')).toBeInTheDocument()
  })
})

describe('CommentDisplay Preview', () => {
  it('should show preview of comment being typed', () => {
    render(<CommentDisplay {...mockProps} />)
    
    const textarea = screen.getByPlaceholderText('Enter your comment (HTML allowed)...')
    fireEvent.change(textarea, { target: { value: 'Preview this!' } })
    
    // The preview section should exist
    expect(screen.getByText('Preview:')).toBeInTheDocument()
  })
})

describe('CommentDisplay Theme Handling', () => {
  it('should accept light theme', () => {
    render(<CommentDisplay theme="light" config={{}} />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should accept dark theme', () => {
    render(<CommentDisplay theme="dark" config={{}} />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should handle null theme', () => {
    render(<CommentDisplay theme={null} config={{}} />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })
})

describe('CommentDisplay Button Interactions', () => {
  it('should handle post button click with empty comment', () => {
    render(<CommentDisplay {...mockProps} />)
    
    const postButton = screen.getByRole('button', { name: 'Post Comment' })
    
    // Should not throw when clicking with empty comment
    expect(() => fireEvent.click(postButton)).not.toThrow()
  })

  it('should handle multiple post clicks', async () => {
    render(<CommentDisplay {...mockProps} />)
    
    const textarea = screen.getByPlaceholderText('Enter your comment (HTML allowed)...')
    const postButton = screen.getByRole('button', { name: 'Post Comment' })
    
    fireEvent.change(textarea, { target: { value: 'Comment 1' } })
    fireEvent.click(postButton)
    
    fireEvent.change(textarea, { target: { value: 'Comment 2' } })
    fireEvent.click(postButton)
    
    // Should handle multiple submissions
    expect(textarea).toHaveValue('')
  })
})

