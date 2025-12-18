import { useState, useEffect } from 'react'
import axios from 'axios'

/**
 * Custom hook with any type abuse
 * 
 * MNT-01: Excessive use of 'any' type
 */

// MNT: Hook with all any types
export const useData = (url: any, options?: any) => {
  // MNT: State with any types
  const [data, setData] = useState<any>(null)
  const [loading, setLoading] = useState<any>(true)
  const [error, setError] = useState<any>(null)
  const [temp, setTemp] = useState<any>(null)

  // MNT: Console spam
  console.log('useData hook called with:', url)

  useEffect(() => {
    console.log('useData effect running')
    
    const fetchData = async () => {
      try {
        console.log('Fetching:', url)
        const response: any = await axios.get(url, options)
        console.log('Response:', response)
        
        setData(response.data)
        setLoading(false)
      } catch (err: any) {
        console.log('Error:', err)
        setError(err)
        setLoading(false)
      }
    }

    fetchData()
  }, [url]) // MNT: Missing options in dependency array

  // MNT: any return type
  const refetch = () => {
    console.log('Refetching data')
    setLoading(true)
    // TODO: Implement refetch
  }

  // MNT: Returning object with any types
  return { data, loading, error, refetch }
}

// MNT: Another hook with any types
export const useFetch = (endpoint: any): any => {
  const [result, setResult] = useState<any>(null)
  
  console.log('useFetch called with:', endpoint)

  useEffect(() => {
    // TODO: Implement fetch logic
    console.log('TODO: Implement useFetch')
  }, [endpoint])

  return result
}

// MNT: Hook without proper typing
export const useLocalStorage = (key: any, initialValue?: any) => {
  const [value, setValue] = useState<any>(() => {
    try {
      const item = localStorage.getItem(key)
      return item ? JSON.parse(item) : initialValue
    } catch (error) {
      console.log('Error reading localStorage:', error)
      return initialValue
    }
  })

  // MNT: any parameter
  const setStoredValue = (newValue: any) => {
    console.log('Setting localStorage:', key, newValue)
    try {
      setValue(newValue)
      localStorage.setItem(key, JSON.stringify(newValue))
    } catch (error) {
      console.log('Error setting localStorage:', error)
    }
  }

  return [value, setStoredValue]
}

export default useData

