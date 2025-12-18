import { useEffect, useState } from 'react'
import { fetchUsers, fetchOrders } from '../services/api'

/**
 * Dashboard component with infinite useEffect loop
 * 
 * REL-02: Infinite useEffect loop - missing dependency array
 */
interface DashboardProps {
  users: any[]
  setUsers: any
  selectedUser: any
  setSelectedUser: any
  loading: any
  setLoading: any
  error: any
  setError: any
  config: any
  setConfig: any
  theme: any
  setTheme: any
  notifications: any[]
  setNotifications: any
  apiUrl: string
  appName: string
  version: string
  maxRetries: number
  timeout: number
  pageSize: number
  onHandleStuff: any
  onDoThing: any
}

function Dashboard(props: DashboardProps) {
  // MNT: Destructuring with any types
  const {
    users,
    setUsers,
    loading,
    setLoading,
    error,
    setError,
    apiUrl,
  } = props

  // MNT: More any types
  const [data, setData] = useState<any>(null)
  const [stats, setStats] = useState<any>({})
  const [temp, setTemp] = useState<any>(null)
  
  // MNT: Console.log spam
  console.log('Dashboard rendering')
  console.log('Props:', props)
  console.log('Data:', data)

  /**
   * REL-02: PROBLEMATIC useEffect - has stale closure issues
   * The dependency array is incomplete (missing setUsers, setLoading, etc.)
   * SonarQube will flag: react-hooks/exhaustive-deps
   */
  useEffect(() => {
    console.log('Fetching data...')
    setLoading(true)
    
    fetchUsers()
      .then((response: any) => {
        setUsers(response.data)
        setData(response.data)
        setLoading(false)
      })
      .catch((err: any) => {
        setError(err.message)
        setLoading(false)
      })
    
    // REL: Incomplete dependency array - missing setUsers, setLoading, setError, setData
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  /**
   * Another problematic useEffect
   */
  useEffect(() => {
    // REL: This also causes issues - state update triggers re-render
    setStats({ count: users?.length || 0, lastUpdate: new Date() })
  }, [users]) // Dependency on users is correct but stats update can cascade

  /**
   * REL: useEffect with object dependency
   */
  useEffect(() => {
    console.log('Config changed')
    // REL: Object in dependency array causes infinite loop
    // because objects are compared by reference
  }, [props.config])

  // MNT: Poorly named function
  const doStuff = (x: any) => {
    console.log('Doing stuff', x)
    setTemp(x)
  }

  // MNT: Unused variable
  const unusedVar = 'this is never used'

  if (loading) {
    return <div className="loading">Loading...</div>
  }

  if (error) {
    return <div className="error">Error: {error}</div>
  }

  return (
    <div className="card">
      <h2>Dashboard</h2>
      
      <div className="stats">
        <p>Total Users: {users?.length || 0}</p>
        <p>Last Update: {stats.lastUpdate?.toString()}</p>
        <p>API URL: {apiUrl}</p>
      </div>
      
      <button 
        className="button" 
        onClick={() => doStuff({ test: true })}
      >
        Refresh Data
      </button>
      
      {/* MNT: Rendering without proper checks */}
      <ul>
        {(users || []).map((user: any, index: any) => (
          <li key={index} onClick={() => props.onHandleStuff(user)}>
            {user.username} - {user.email}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default Dashboard

