import { useEffect, useState } from 'react'

/**
 * UserList component with any type abuse
 * 
 * MNT-01: Excessive use of 'any' type
 */
interface UserListProps {
  users: any[]
  selectedUser: any
  onSelect: any
  loading: any
  theme: any
  apiUrl: any
}

// MNT-01: All types are 'any'
function UserList({ users, selectedUser, onSelect, loading, theme, apiUrl }: UserListProps) {
  // MNT: More any types
  const [filteredUsers, setFilteredUsers] = useState<any[]>([])
  const [searchTerm, setSearchTerm] = useState<any>('')
  const [sortOrder, setSortOrder] = useState<any>('asc')
  const [sortField, setSortField] = useState<any>('name')
  const [temp, setTemp] = useState<any>(null)
  const [data, setData] = useState<any>({})
  
  // MNT: Console spam
  console.log('UserList rendering')
  console.log('Users:', users)
  console.log('Selected:', selectedUser)

  useEffect(() => {
    // MNT: any type in filter callback
    const filtered = users?.filter((user: any) => {
      const term: any = searchTerm.toLowerCase()
      return user?.username?.toLowerCase().includes(term) ||
             user?.email?.toLowerCase().includes(term)
    })
    setFilteredUsers(filtered || [])
  }, [users, searchTerm])

  // MNT: any types everywhere
  const handleSort = (field: any) => {
    const newOrder: any = sortOrder === 'asc' ? 'desc' : 'asc'
    setSortField(field)
    setSortOrder(newOrder)
    
    // MNT: Sorting with any types
    const sorted: any[] = [...filteredUsers].sort((a: any, b: any) => {
      if (newOrder === 'asc') {
        return a[field] > b[field] ? 1 : -1
      } else {
        return a[field] < b[field] ? 1 : -1
      }
    })
    
    setFilteredUsers(sorted)
  }

  // MNT: Poorly typed handler
  const handleClick = (user: any, event: any) => {
    console.log('User clicked:', user)
    console.log('Event:', event)
    onSelect(user)
    setTemp(user)
  }

  // MNT: any return type (implicit)
  const formatUser = (user: any) => {
    return {
      ...user,
      displayName: `${user.firstName} ${user.lastName}`,
      formattedEmail: `<${user.email}>`,
    }
  }

  // MNT: Function with all any parameters
  const processData = (a: any, b: any, c: any, d: any): any => {
    console.log(a, b, c, d)
    return { a, b, c, d }
  }

  if (loading) {
    return <div className="loading">Loading users...</div>
  }

  return (
    <div className="card">
      <h2>Users</h2>
      
      <input
        type="text"
        value={searchTerm}
        onChange={(e: any) => setSearchTerm(e.target.value)}
        placeholder="Search users..."
      />
      
      <div className="sort-buttons">
        <button onClick={() => handleSort('username')}>Sort by Username</button>
        <button onClick={() => handleSort('email')}>Sort by Email</button>
      </div>
      
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredUsers.map((user: any, index: any) => (
            <tr 
              key={user.id || index}
              onClick={(e: any) => handleClick(user, e)}
              style={{
                backgroundColor: selectedUser?.id === user.id ? '#e6f3ff' : 'transparent'
              }}
            >
              <td>{user.id}</td>
              <td>{user.username}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>
                <button onClick={(e: any) => {
                  e.stopPropagation()
                  console.log('Edit user:', user)
                }}>
                  Edit
                </button>
                <button onClick={(e: any) => {
                  e.stopPropagation()
                  console.log('Delete user:', user)
                }}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      
      {/* MNT: Debug output */}
      <div style={{ marginTop: '10px', fontSize: '12px', color: '#666' }}>
        <p>Total: {users?.length || 0}</p>
        <p>Filtered: {filteredUsers.length}</p>
        <p>Sort: {sortField} ({sortOrder})</p>
      </div>
    </div>
  )
}

export default UserList

