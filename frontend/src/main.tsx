import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'

// MNT: Console log left in production code
console.log('Application starting...')
console.log('Environment:', import.meta.env.MODE)

// SEC: Logging sensitive config
console.log('API URL:', 'http://localhost:8080/api')

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)

// MNT: Dead code
function unusedFunction() {
  console.log('This is never called')
}

