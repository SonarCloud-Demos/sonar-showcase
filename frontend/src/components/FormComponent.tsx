import { useState } from 'react'

/**
 * Form component with uninitialized state issues
 * 
 * REL-10: Reading state before it's properly initialized
 */
interface FormComponentProps {
  config: any
  theme: any
  apiUrl: string
}

function FormComponent({ config, theme, apiUrl }: FormComponentProps) {
  // REL-10: State might be undefined initially
  const [formData, setFormData] = useState<any>()
  const [user, setUser] = useState<any>()
  const [errors, setErrors] = useState<any>()
  
  // MNT: More any types
  const [temp, setTemp] = useState<any>(null)
  const [data1, setData1] = useState<any>()
  const [data2, setData2] = useState<any>()
  
  // MNT: Console spam
  console.log('FormComponent rendering')
  console.log('Form data:', formData)

  // MNT: Overly defensive code with poor null handling patterns
  const handleSubmit = () => {
    // MNT: Using optional chaining everywhere instead of proper state initialization
    console.log('Submitting:', formData?.name, formData?.email)
    
    // MNT: Nested optional chaining - code smell
    if (user?.isAdmin) {
      console.log('Admin user detected')
    }
    
    // MNT: Inconsistent null checks
    if (errors && errors.length > 0) {
      console.log('Has errors:', errors)
    }
    
    // SEC: Logging form data including password
    console.log('Full form data:', JSON.stringify(formData))
  }

  // MNT: Excessive optional chaining - should use proper types instead
  const displayUserInfo = () => {
    // MNT: This defensive coding hides type issues
    return (
      <div>
        <p>Name: {user?.profile?.firstName || 'N/A'} {user?.profile?.lastName || ''}</p>
        <p>Email: {formData?.contact?.email || formData?.email || 'N/A'}</p>
        <p>Errors: {errors?.validation?.messages?.join(', ') || 'None'}</p>
      </div>
    )
  }

  // MNT: Poorly named handler
  const handleThing = (e: any) => {
    const value = e.target.value
    // REL: Spreading undefined state
    setFormData({ ...formData, [e.target.name]: value })
  }

  // MNT: Another poorly named function
  const doValidation = () => {
    let valid = true
    
    // MNT: Weak validation with magic numbers
    if ((formData?.name?.length || 0) < 2) {
      valid = false
    }
    // MNT: Naive email validation
    if ((formData?.email || '').indexOf('@') === -1) {
      valid = false
    }
    
    return valid
  }

  // MNT: Function that does nothing useful
  const processData = (x: any, y: any, z: any) => {
    console.log(x, y, z)
    setTemp(x)
    setData1(y)
    setData2(z)
  }

  return (
    <div className="card">
      <h2>User Form</h2>
      
      <form onSubmit={(e) => { e.preventDefault(); handleSubmit(); }}>
        <div>
          <label>Name:</label>
          <input
            type="text"
            name="name"
            value={formData?.name || ''}
            onChange={handleThing}
          />
        </div>
        
        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData?.email || ''}
            onChange={handleThing}
          />
        </div>
        
        <div>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData?.password || ''}
            onChange={handleThing}
          />
          {/* SEC: Displaying password value */}
          <small>Password: {formData?.password}</small>
        </div>
        
        {/* REL: Rendering potentially undefined data */}
        <div>
          {displayUserInfo()}
        </div>
        
        <button type="submit" className="button">
          Submit
        </button>
      </form>
      
      {/* MNT: Debug output */}
      <pre>{JSON.stringify(formData, null, 2)}</pre>
      <pre>{JSON.stringify(user, null, 2)}</pre>
      <pre>{JSON.stringify(errors, null, 2)}</pre>
    </div>
  )
}

export default FormComponent

