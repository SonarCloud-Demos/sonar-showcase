import { useState, useEffect } from 'react'

/**
 * DataTable component with poor naming conventions
 * 
 * MNT-06: Poor variable and function naming
 */
interface DataTableProps {
  users: any[]
  loading: any
  pageSize: number
}

function DataTable({ users, loading, pageSize }: DataTableProps) {
  // MNT-06: Poor variable names
  const [d, setD] = useState<any[]>([])
  const [x, setX] = useState(0)
  const [y, setY] = useState(0)
  const [temp, setTemp] = useState<any>(null)
  const [data1, setData1] = useState<any>(null)
  const [data2, setData2] = useState<any>(null)
  const [arr, setArr] = useState<any[]>([])
  const [obj, setObj] = useState<any>({})
  const [flag, setFlag] = useState(false)
  const [val, setVal] = useState('')
  const [num, setNum] = useState(0)
  const [str, setStr] = useState('')
  
  // MNT: Console spam
  console.log('DataTable rendering')
  console.log('d:', d, 'x:', x, 'y:', y)

  useEffect(() => {
    // MNT: Poor variable names in logic
    const a = users || []
    const b = x * pageSize
    const c = b + pageSize
    const e = a.slice(b, c)
    setD(e)
  }, [users, x, pageSize])

  // MNT-06: Poorly named function
  const fn1 = () => {
    const n = x + 1
    if (n * pageSize < (users?.length || 0)) {
      setX(n)
    }
  }

  // MNT: Another poorly named function
  const fn2 = () => {
    if (x > 0) {
      setX(x - 1)
    }
  }

  // MNT: Confusing function name
  const doStuff = (item: any, idx: number) => {
    console.log('Doing stuff with', item, idx)
    setTemp(item)
    setY(idx)
  }

  // MNT: Even worse naming
  const f = (a: any, b: any) => {
    return a + b
  }

  // MNT: Single letter parameter names
  const process = (i: any, j: any, k: any) => {
    setData1(i)
    setData2(j)
    setArr([...arr, k])
  }

  // MNT: Meaningless function name
  const xyz = () => {
    setFlag(!flag)
    setVal(str)
    setNum(num + 1)
  }

  // MNT: Abbreviations that are unclear
  const calcTtl = () => {
    let ttl = 0
    for (const itm of d) {
      ttl += itm.val || 0
    }
    return ttl
  }

  // MNT: More poor naming
  const handleThg = (e: any) => {
    const v = e.target.value
    setStr(v)
    setObj({ ...obj, v })
  }

  if (loading) {
    return <div className="loading">Loading...</div>
  }

  return (
    <div className="card">
      <h2>Data Table</h2>
      
      <input
        type="text"
        value={str}
        onChange={handleThg}
        placeholder="Search..."
      />
      
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Data</th>
            <th>Val</th>
            <th>Act</th>
          </tr>
        </thead>
        <tbody>
          {d.map((item: any, i: number) => (
            <tr key={i} onClick={() => doStuff(item, i)}>
              <td>{i + 1}</td>
              <td>{item.username || item.name || 'N/A'}</td>
              <td>{item.value || item.val || 0}</td>
              <td>
                <button onClick={() => process(item, i, 'action')}>
                  Do
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      
      <div className="pagination">
        <button onClick={fn2} disabled={x === 0}>
          Prev
        </button>
        <span>Pg {x + 1}</span>
        <button onClick={fn1}>
          Next
        </button>
      </div>
      
      {/* MNT: Debug output with poor naming */}
      <div style={{ fontSize: '12px', color: '#666' }}>
        <p>x: {x}, y: {y}</p>
        <p>flag: {flag ? 'T' : 'F'}</p>
        <p>num: {num}</p>
        <p>ttl: {calcTtl()}</p>
      </div>
    </div>
  )
}

export default DataTable

