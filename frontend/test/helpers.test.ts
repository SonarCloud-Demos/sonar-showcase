import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  formatDate,
  formatCurrency,
  validateEmail,
  validatePhone,
  validatePassword,
  truncateText,
  debounce,
  throttle,
  deepClone,
  isEqual,
  retry
} from '../src/utils/helpers'

/**
 * Comprehensive tests for helper utilities
 */

describe('formatDate', () => {
  it('should format a date object', () => {
    const date = new Date('2023-01-15T12:00:00')
    const result = formatDate(date)
    
    expect(result).toBeTruthy()
    expect(typeof result).toBe('string')
  })

  it('should format a date string', () => {
    const result = formatDate('2023-01-15')
    
    expect(result).toBeTruthy()
    expect(typeof result).toBe('string')
  })

  it('should format a timestamp', () => {
    const timestamp = 1673784000000 // 2023-01-15
    const result = formatDate(timestamp)
    
    expect(result).toBeTruthy()
    expect(typeof result).toBe('string')
  })

  it('should handle invalid date', () => {
    const result = formatDate('invalid-date')
    
    expect(result).toBe('Invalid Date')
  })
})

describe('formatCurrency', () => {
  it('should format a number as currency', () => {
    const result = formatCurrency(10)
    
    expect(result).toBe('$10.00')
  })

  it('should format decimal numbers', () => {
    const result = formatCurrency(10.5)
    
    expect(result).toBe('$10.50')
  })

  it('should handle string numbers', () => {
    const result = formatCurrency('25.99')
    
    expect(result).toBe('$25.99')
  })

  it('should format zero', () => {
    const result = formatCurrency(0)
    
    expect(result).toBe('$0.00')
  })

  it('should round to 2 decimal places', () => {
    const result = formatCurrency(10.999)
    
    expect(result).toBe('$11.00')
  })

  it('should handle negative numbers', () => {
    const result = formatCurrency(-10)
    
    expect(result).toBe('$-10.00')
  })
})

describe('validateEmail', () => {
  it('should return true for valid email', () => {
    expect(validateEmail('test@example.com')).toBe(true)
  })

  it('should return true for email with subdomain', () => {
    expect(validateEmail('test@mail.example.com')).toBe(true)
  })

  it('should return false for email without @', () => {
    expect(validateEmail('testexample.com')).toBe(false)
  })

  it('should return false for null', () => {
    expect(validateEmail(null)).toBe(false)
  })

  it('should return false for undefined', () => {
    expect(validateEmail(undefined)).toBe(false)
  })

  it('should return false for empty string', () => {
    expect(validateEmail('')).toBe(false)
  })
})

describe('validatePhone', () => {
  it('should return true for 10 digit phone', () => {
    expect(validatePhone('1234567890')).toBe(true)
  })

  it('should return true for phone with dashes', () => {
    expect(validatePhone('123-456-7890')).toBe(true)
  })

  it('should return false for short phone', () => {
    expect(validatePhone('123456')).toBe(false)
  })

  it('should return false for null', () => {
    expect(validatePhone(null)).toBe(false)
  })

  it('should return false for undefined', () => {
    expect(validatePhone(undefined)).toBe(false)
  })

  it('should return false for empty string', () => {
    expect(validatePhone('')).toBe(false)
  })
})

describe('validatePassword', () => {
  it('should return true for password with 4+ characters', () => {
    expect(validatePassword('password123')).toBe(true)
  })

  it('should return true for exactly 4 characters', () => {
    expect(validatePassword('pass')).toBe(true)
  })

  it('should return false for password with 3 characters', () => {
    expect(validatePassword('abc')).toBe(false)
  })

  it('should return false for null', () => {
    expect(validatePassword(null)).toBe(false)
  })

  it('should return false for undefined', () => {
    expect(validatePassword(undefined)).toBe(false)
  })

  it('should return false for empty string', () => {
    expect(validatePassword('')).toBe(false)
  })
})

describe('truncateText', () => {
  it('should truncate long text', () => {
    const result = truncateText('This is a very long text', 10)
    
    expect(result).toBe('This is a ...')
  })

  it('should not truncate short text', () => {
    const result = truncateText('Short', 10)
    
    expect(result).toBe('Short')
  })

  it('should return empty string for null', () => {
    const result = truncateText(null, 10)
    
    expect(result).toBe('')
  })

  it('should return empty string for undefined', () => {
    const result = truncateText(undefined, 10)
    
    expect(result).toBe('')
  })

  it('should handle exact length', () => {
    const result = truncateText('1234567890', 10)
    
    expect(result).toBe('1234567890')
  })

  it('should truncate with 0 max length', () => {
    const result = truncateText('test', 0)
    
    expect(result).toBe('...')
  })
})

describe('debounce', () => {
  it('should return a function', () => {
    const fn = vi.fn()
    const debounced = debounce(fn, 100)
    
    expect(typeof debounced).toBe('function')
  })

  it('should be callable', () => {
    const fn = vi.fn()
    const debounced = debounce(fn, 100)
    
    expect(() => debounced()).not.toThrow()
  })

  it('should call the original function', () => {
    const fn = vi.fn()
    const debounced = debounce(fn, 0)
    
    debounced()
    
    expect(fn).toHaveBeenCalled()
  })
})

describe('throttle', () => {
  it('should return a function', () => {
    const fn = vi.fn()
    const throttled = throttle(fn, 100)
    
    expect(typeof throttled).toBe('function')
  })

  it('should be callable', () => {
    const fn = vi.fn()
    const throttled = throttle(fn, 100)
    
    expect(() => throttled()).not.toThrow()
  })

  it('should call the original function', () => {
    const fn = vi.fn()
    const throttled = throttle(fn, 0)
    
    throttled()
    
    expect(fn).toHaveBeenCalled()
  })
})

describe('deepClone', () => {
  it('should clone a simple object', () => {
    const obj = { a: 1, b: 2 }
    const result = deepClone(obj)
    
    expect(result).toEqual(obj)
    expect(result).not.toBe(obj)
  })

  it('should clone a nested object', () => {
    const obj = { a: { b: { c: 1 } } }
    const result = deepClone(obj)
    
    expect(result).toEqual(obj)
    expect(result.a).not.toBe(obj.a)
  })

  it('should clone an array', () => {
    const arr = [1, 2, 3]
    const result = deepClone(arr)
    
    expect(result).toEqual(arr)
    expect(result).not.toBe(arr)
  })

  it('should clone a mixed structure', () => {
    const obj = { arr: [1, 2], nested: { key: 'value' } }
    const result = deepClone(obj)
    
    expect(result).toEqual(obj)
    expect(result.arr).not.toBe(obj.arr)
    expect(result.nested).not.toBe(obj.nested)
  })

  it('should handle null', () => {
    const result = deepClone(null)
    
    expect(result).toBeNull()
  })

  it('should handle primitives', () => {
    expect(deepClone(5)).toBe(5)
    expect(deepClone('test')).toBe('test')
    expect(deepClone(true)).toBe(true)
  })
})

describe('isEqual', () => {
  it('should return true for equal objects', () => {
    const a = { x: 1, y: 2 }
    const b = { x: 1, y: 2 }
    
    expect(isEqual(a, b)).toBe(true)
  })

  it('should return false for different objects', () => {
    const a = { x: 1, y: 2 }
    const b = { x: 1, y: 3 }
    
    expect(isEqual(a, b)).toBe(false)
  })

  it('should return true for equal arrays', () => {
    const a = [1, 2, 3]
    const b = [1, 2, 3]
    
    expect(isEqual(a, b)).toBe(true)
  })

  it('should return false for different arrays', () => {
    const a = [1, 2, 3]
    const b = [1, 2, 4]
    
    expect(isEqual(a, b)).toBe(false)
  })

  it('should return true for equal primitives', () => {
    expect(isEqual(5, 5)).toBe(true)
    expect(isEqual('test', 'test')).toBe(true)
    expect(isEqual(true, true)).toBe(true)
  })

  it('should return false for different primitives', () => {
    expect(isEqual(5, 6)).toBe(false)
    expect(isEqual('test', 'other')).toBe(false)
  })

  it('should compare nested objects', () => {
    const a = { nested: { value: 1 } }
    const b = { nested: { value: 1 } }
    
    expect(isEqual(a, b)).toBe(true)
  })
})

describe('retry', () => {
  it('should call the function', async () => {
    const fn = vi.fn().mockResolvedValue('success')
    
    const result = await retry(fn, 3)
    
    expect(fn).toHaveBeenCalled()
    expect(result).toBe('success')
  })

  it('should return the function result', async () => {
    const fn = vi.fn().mockResolvedValue({ data: 'test' })
    
    const result = await retry(fn, 1)
    
    expect(result).toEqual({ data: 'test' })
  })

  it('should handle synchronous functions', async () => {
    const fn = vi.fn().mockReturnValue('sync result')
    
    const result = await retry(fn, 1)
    
    expect(result).toBe('sync result')
  })

  it('should throw if function throws', async () => {
    const fn = vi.fn().mockRejectedValue(new Error('Failed'))
    
    await expect(retry(fn, 1)).rejects.toThrow('Failed')
  })
})

describe('Helper Edge Cases', () => {
  it('formatDate should handle Date.now()', () => {
    const result = formatDate(Date.now())
    
    expect(result).toBeTruthy()
    expect(typeof result).toBe('string')
  })

  it('formatCurrency should handle large numbers', () => {
    const result = formatCurrency(1000000)
    
    expect(result).toBe('$1000000.00')
  })

  it('truncateText should handle very long text', () => {
    const longText = 'a'.repeat(1000)
    const result = truncateText(longText, 50)
    
    expect(result.length).toBe(53) // 50 chars + '...'
  })

  it('deepClone should handle empty objects', () => {
    const result = deepClone({})
    
    expect(result).toEqual({})
  })

  it('deepClone should handle empty arrays', () => {
    const result = deepClone([])
    
    expect(result).toEqual([])
  })

  it('isEqual should handle empty objects', () => {
    expect(isEqual({}, {})).toBe(true)
  })

  it('isEqual should handle empty arrays', () => {
    expect(isEqual([], [])).toBe(true)
  })
})

