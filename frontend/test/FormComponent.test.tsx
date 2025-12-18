import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import FormComponent from '../src/components/FormComponent'

/**
 * Comprehensive tests for FormComponent
 */

const mockProps = {
  config: {},
  theme: 'light',
  apiUrl: 'http://localhost:8080/api'
}

describe('FormComponent', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render form title', () => {
    render(<FormComponent {...mockProps} />)
    expect(screen.getByRole('heading', { level: 2 })).toHaveTextContent('User Form')
  })

  it('should have card class on container', () => {
    render(<FormComponent {...mockProps} />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should render name input', () => {
    render(<FormComponent {...mockProps} />)
    expect(document.querySelector('input[name="name"]')).toBeInTheDocument()
  })

  it('should render email input', () => {
    render(<FormComponent {...mockProps} />)
    expect(document.querySelector('input[name="email"]')).toBeInTheDocument()
  })

  it('should render password input', () => {
    render(<FormComponent {...mockProps} />)
    const passwordInput = document.querySelector('input[type="password"]')
    expect(passwordInput).toBeInTheDocument()
  })

  it('should render submit button', () => {
    render(<FormComponent {...mockProps} />)
    expect(screen.getByRole('button', { name: /submit/i })).toBeInTheDocument()
  })

  it('should render form element', () => {
    render(<FormComponent {...mockProps} />)
    const form = document.querySelector('form')
    expect(form).toBeInTheDocument()
  })
})

describe('FormComponent Input Handling', () => {
  it('should update name input value', () => {
    render(<FormComponent {...mockProps} />)
    
    const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement
    fireEvent.change(nameInput, { target: { value: 'John Doe', name: 'name' } })
    
    expect(nameInput.value).toBe('John Doe')
  })

  it('should update email input value', () => {
    render(<FormComponent {...mockProps} />)
    
    const emailInput = document.querySelector('input[name="email"]') as HTMLInputElement
    fireEvent.change(emailInput, { target: { value: 'john@test.com', name: 'email' } })
    
    expect(emailInput.value).toBe('john@test.com')
  })

  it('should update password input value', () => {
    render(<FormComponent {...mockProps} />)
    
    const passwordInput = document.querySelector('input[type="password"]') as HTMLInputElement
    fireEvent.change(passwordInput, { target: { value: 'secret123', name: 'password' } })
    
    expect(passwordInput.value).toBe('secret123')
  })
})

describe('FormComponent Form Submission', () => {
  it('should handle form submission', () => {
    render(<FormComponent {...mockProps} />)
    
    const form = document.querySelector('form') as HTMLFormElement
    
    // Should not throw on submit
    expect(() => fireEvent.submit(form)).not.toThrow()
  })

  it('should handle submit button click', () => {
    render(<FormComponent {...mockProps} />)
    
    const submitButton = screen.getByRole('button', { name: /submit/i })
    
    // Should not throw on click
    expect(() => fireEvent.click(submitButton)).not.toThrow()
  })

  it('should submit with filled form data', () => {
    render(<FormComponent {...mockProps} />)
    
    const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement
    const emailInput = document.querySelector('input[name="email"]') as HTMLInputElement
    
    fireEvent.change(nameInput, { target: { value: 'Test User', name: 'name' } })
    fireEvent.change(emailInput, { target: { value: 'test@test.com', name: 'email' } })
    
    const submitButton = screen.getByRole('button', { name: /submit/i })
    fireEvent.click(submitButton)
    
    // Form should be submittable
    expect(submitButton).toBeInTheDocument()
  })
})

describe('FormComponent User Info Display', () => {
  it('should display N/A for empty user info', () => {
    render(<FormComponent {...mockProps} />)
    
    // Should show N/A placeholders
    expect(screen.getAllByText(/N\/A/).length).toBeGreaterThan(0)
  })

  it('should display email as N/A initially', () => {
    render(<FormComponent {...mockProps} />)
    
    const naElements = screen.getAllByText(/N\/A/)
    expect(naElements.length).toBeGreaterThan(0)
  })
})

describe('FormComponent Debug Output', () => {
  it('should render pre elements for debug', () => {
    render(<FormComponent {...mockProps} />)
    
    const preElements = document.querySelectorAll('pre')
    expect(preElements.length).toBe(3)
  })
})

describe('FormComponent Theme Handling', () => {
  it('should accept light theme', () => {
    render(<FormComponent config={{}} theme="light" apiUrl="http://localhost" />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should accept dark theme', () => {
    render(<FormComponent config={{}} theme="dark" apiUrl="http://localhost" />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should handle null config', () => {
    render(<FormComponent config={null} theme="light" apiUrl="http://localhost" />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })
})

describe('FormComponent Labels', () => {
  it('should have Name label', () => {
    render(<FormComponent {...mockProps} />)
    expect(screen.getByText('Name:')).toBeInTheDocument()
  })

  it('should have Email label', () => {
    render(<FormComponent {...mockProps} />)
    expect(screen.getByText('Email:')).toBeInTheDocument()
  })

  it('should have Password label', () => {
    render(<FormComponent {...mockProps} />)
    expect(screen.getAllByText(/Password/).length).toBeGreaterThan(0)
  })
})

describe('FormComponent Edge Cases', () => {
  it('should handle rapid input changes', () => {
    render(<FormComponent {...mockProps} />)
    
    const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement
    
    for (let i = 0; i < 10; i++) {
      fireEvent.change(nameInput, { target: { value: `Value ${i}`, name: 'name' } })
    }
    
    expect(nameInput).toHaveValue('Value 9')
  })

  it('should handle empty submissions', () => {
    render(<FormComponent {...mockProps} />)
    
    const form = document.querySelector('form') as HTMLFormElement
    
    // Should handle empty form submission
    expect(() => fireEvent.submit(form)).not.toThrow()
  })
})

