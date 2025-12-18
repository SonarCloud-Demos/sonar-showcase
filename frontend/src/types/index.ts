/**
 * Types file - incomplete and partially unused
 * 
 * MNT: Many types are incomplete or unused
 */

// MNT: Incomplete type definition
export interface User {
  id?: number
  username?: string
  email?: string
  // TODO: Add more fields
}

// MNT: Type with 'any' - defeats the purpose of TypeScript
export interface ApiResponse {
  data: any
  status: any
  message: any
  error: any
}

// MNT: Unused interface
export interface Order {
  id: number
  userId: number
  items: any[]
  total: number
  status: string
}

// MNT: Another unused interface
export interface Product {
  id: number
  name: string
  price: number
  description: string
}

// MNT: Overly generic type
export type GenericData = any

// MNT: Type that should be more specific
export type UserRole = string  // Should be: 'admin' | 'user' | 'guest'

// MNT: Incomplete enum
export enum Status {
  PENDING = 'PENDING',
  // TODO: Add more statuses
}

// MNT: Unused type
export type ID = number | string

// MNT: Type with all optional fields (too permissive)
export interface Config {
  apiUrl?: string
  timeout?: number
  retries?: number
  debug?: boolean
  [key: string]: any  // MNT: Index signature allows anything
}

// TODO: Define proper types for the application
// TODO: Remove 'any' types
// FIXME: These types don't match the backend models

