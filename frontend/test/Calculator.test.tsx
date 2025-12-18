import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import Calculator from '../src/components/Calculator'

/**
 * Comprehensive tests for Calculator component
 */

describe('Calculator Component', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render calculator title', () => {
    render(<Calculator />)
    expect(screen.getByRole('heading', { level: 2 })).toHaveTextContent('Calculator')
  })

  it('should render two input fields', () => {
    render(<Calculator />)
    expect(screen.getByPlaceholderText('First number')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Second number')).toBeInTheDocument()
  })

  it('should render operation buttons', () => {
    render(<Calculator />)
    expect(screen.getByRole('button', { name: '+' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: '-' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: '×' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: '÷' })).toBeInTheDocument()
  })

  it('should have card class on container', () => {
    render(<Calculator />)
    expect(document.querySelector('.card')).toBeInTheDocument()
  })

  it('should have calculator-inputs class', () => {
    render(<Calculator />)
    expect(document.querySelector('.calculator-inputs')).toBeInTheDocument()
  })

  it('should have calculator-buttons class', () => {
    render(<Calculator />)
    expect(document.querySelector('.calculator-buttons')).toBeInTheDocument()
  })
})

describe('Calculator Input Handling', () => {
  it('should update first number input', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number') as HTMLInputElement
    fireEvent.change(input1, { target: { value: '10' } })
    
    expect(input1.value).toBe('10')
  })

  it('should update second number input', () => {
    render(<Calculator />)
    
    const input2 = screen.getByPlaceholderText('Second number') as HTMLInputElement
    fireEvent.change(input2, { target: { value: '5' } })
    
    expect(input2.value).toBe('5')
  })

  it('should accept decimal numbers', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number') as HTMLInputElement
    fireEvent.change(input1, { target: { value: '10.5' } })
    
    expect(input1.value).toBe('10.5')
  })

  it('should accept negative numbers', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number') as HTMLInputElement
    fireEvent.change(input1, { target: { value: '-10' } })
    
    expect(input1.value).toBe('-10')
  })
})

describe('Calculator Addition', () => {
  it('should add two positive numbers', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '10' } })
    fireEvent.change(input2, { target: { value: '5' } })
    
    const addButton = screen.getByRole('button', { name: '+' })
    fireEvent.click(addButton)
    
    expect(screen.getByText(/Result: 15/i)).toBeInTheDocument()
  })

  it('should add decimal numbers', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '10.5' } })
    fireEvent.change(input2, { target: { value: '5.5' } })
    
    const addButton = screen.getByRole('button', { name: '+' })
    fireEvent.click(addButton)
    
    expect(screen.getByText(/Result: 16/i)).toBeInTheDocument()
  })
})

describe('Calculator Subtraction', () => {
  it('should subtract two numbers', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '10' } })
    fireEvent.change(input2, { target: { value: '3' } })
    
    const subButton = screen.getByRole('button', { name: '-' })
    fireEvent.click(subButton)
    
    expect(screen.getByText(/Result: 7/i)).toBeInTheDocument()
  })

  it('should handle negative result', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '5' } })
    fireEvent.change(input2, { target: { value: '10' } })
    
    const subButton = screen.getByRole('button', { name: '-' })
    fireEvent.click(subButton)
    
    expect(screen.getByText(/Result: -5/i)).toBeInTheDocument()
  })
})

describe('Calculator Multiplication', () => {
  it('should multiply two numbers', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '6' } })
    fireEvent.change(input2, { target: { value: '7' } })
    
    const mulButton = screen.getByRole('button', { name: '×' })
    fireEvent.click(mulButton)
    
    expect(screen.getByText(/Result: 42/i)).toBeInTheDocument()
  })

  it('should multiply by zero', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '10' } })
    fireEvent.change(input2, { target: { value: '0' } })
    
    const mulButton = screen.getByRole('button', { name: '×' })
    fireEvent.click(mulButton)
    
    expect(screen.getByText(/Result: 0/i)).toBeInTheDocument()
  })
})

describe('Calculator Division', () => {
  it('should divide two numbers', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '20' } })
    fireEvent.change(input2, { target: { value: '4' } })
    
    const divButton = screen.getByRole('button', { name: '÷' })
    fireEvent.click(divButton)
    
    expect(screen.getByText(/Result: 5/i)).toBeInTheDocument()
  })

  it('should handle division by zero', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '10' } })
    fireEvent.change(input2, { target: { value: '0' } })
    
    const divButton = screen.getByRole('button', { name: '÷' })
    fireEvent.click(divButton)
    
    // Division by zero results in Infinity
    expect(screen.getByText(/Result:/i)).toBeInTheDocument()
  })
})

describe('Calculator Validation Display', () => {
  it('should show input validation status', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: '10' } })
    fireEvent.change(input2, { target: { value: '5' } })
    
    // The validation display should be present
    expect(screen.getByText(/Input 1 valid:/i)).toBeInTheDocument()
    expect(screen.getByText(/Input 2 valid:/i)).toBeInTheDocument()
  })

  it('should show result validation status', () => {
    render(<Calculator />)
    expect(screen.getByText(/Result valid:/i)).toBeInTheDocument()
  })
})

describe('Calculator Edge Cases', () => {
  it('should handle empty inputs', () => {
    render(<Calculator />)
    
    const addButton = screen.getByRole('button', { name: '+' })
    
    // Should not throw when clicking with empty inputs
    expect(() => fireEvent.click(addButton)).not.toThrow()
  })

  it('should handle non-numeric inputs', () => {
    render(<Calculator />)
    
    const input1 = screen.getByPlaceholderText('First number')
    const input2 = screen.getByPlaceholderText('Second number')
    
    fireEvent.change(input1, { target: { value: 'abc' } })
    fireEvent.change(input2, { target: { value: 'xyz' } })
    
    const addButton = screen.getByRole('button', { name: '+' })
    
    // Should handle gracefully
    expect(() => fireEvent.click(addButton)).not.toThrow()
  })

  it('should render all button elements', () => {
    render(<Calculator />)
    
    const buttons = screen.getAllByRole('button')
    expect(buttons.length).toBe(4)
  })
})

