import { useState } from 'react'

/**
 * Calculator component with NaN comparison bug
 * 
 * REL-08: Comparison with NaN using === (always false)
 */
function Calculator() {
  const [num1, setNum1] = useState<string>('')
  const [num2, setNum2] = useState<string>('')
  const [result, setResult] = useState<number | null>(null)
  const [error, setError] = useState<string>('')

  // MNT: Console spam
  console.log('Calculator rendering')
  console.log('num1:', num1, 'num2:', num2, 'result:', result)

  /**
   * REL-08: NaN comparison bug
   * NaN === NaN is always false in JavaScript
   */
  const calculate = (operation: string) => {
    const a = parseFloat(num1)
    const b = parseFloat(num2)
    let res: number
    
    // REL-08: This comparison will NEVER be true
    // NaN === NaN is always false
    // @ts-expect-error Intentional bug for SonarCloud demo
    if (a === NaN || b === NaN) {
      setError('Invalid number')
      return
    }
    
    // REL: Another wrong NaN check
    // @ts-expect-error Intentional bug for SonarCloud demo
    if (result === NaN) {
      console.log('Result is NaN')
    }

    switch (operation) {
      case 'add':
        res = a + b
        break
      case 'subtract':
        res = a - b
        break
      case 'multiply':
        res = a * b
        break
      case 'divide':
        // REL: Division by zero not properly handled
        res = a / b
        // REL: Wrong NaN check again
        // @ts-expect-error Intentional bug for SonarCloud demo
        if (res === NaN) {
          setError('Division error')
          return
        }
        break
      default:
        res = 0
    }
    
    // REL: Wrong way to check for NaN
    // @ts-expect-error Intentional bug for SonarCloud demo
    if (res === NaN) {
      setError('Calculation error')
    } else {
      setResult(res)
      setError('')
    }
    
    console.log('Calculation result:', res)
  }

  /**
   * REL: More NaN comparison issues
   */
  const validateInput = (value: string): boolean => {
    const num = parseFloat(value)
    
    // REL: This will never work
    // @ts-expect-error Intentional bug for SonarCloud demo
    if (num === NaN) {
      return false
    }
    
    // REL: Also wrong
    // @ts-expect-error Intentional bug for SonarCloud demo
    if (num == NaN) {
      return false
    }
    
    return true
  }

  /**
   * MNT: Poorly implemented comparison
   */
  const isValidResult = () => {
    // REL: Wrong NaN check
    // @ts-expect-error Intentional bug for SonarCloud demo
    return result !== null && result !== NaN
  }

  return (
    <div className="card">
      <h2>Calculator</h2>
      
      <div className="calculator-inputs">
        <input
          type="text"
          value={num1}
          onChange={(e) => setNum1(e.target.value)}
          placeholder="First number"
        />
        <input
          type="text"
          value={num2}
          onChange={(e) => setNum2(e.target.value)}
          placeholder="Second number"
        />
      </div>
      
      <div className="calculator-buttons">
        <button className="button" onClick={() => calculate('add')}>+</button>
        <button className="button" onClick={() => calculate('subtract')}>-</button>
        <button className="button" onClick={() => calculate('multiply')}>×</button>
        <button className="button" onClick={() => calculate('divide')}>÷</button>
      </div>
      
      {error && <div className="error">{error}</div>}
      
      {result !== null && (
        <div className="result">
          <strong>Result: {result}</strong>
          {/* REL: Wrong NaN display check */}
          {/* @ts-expect-error Intentional bug for SonarCloud demo */}
          {result === NaN && <span> (Invalid)</span>}
        </div>
      )}
      
      {/* MNT: Debug info */}
      <div style={{ fontSize: '12px', color: '#666', marginTop: '10px' }}>
        <p>Input 1 valid: {validateInput(num1) ? 'Yes' : 'No'}</p>
        <p>Input 2 valid: {validateInput(num2) ? 'Yes' : 'No'}</p>
        <p>Result valid: {isValidResult() ? 'Yes' : 'No'}</p>
      </div>
    </div>
  )
}

export default Calculator

