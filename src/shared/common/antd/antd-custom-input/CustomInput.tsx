/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import { Input } from 'antd'
import React, { KeyboardEventHandler, forwardRef } from 'react'
// import './CustomInput.scss'

interface CustomInputProps {
  readonly inputVariant?: string
  readonly pattern?:string  
  readonly styles?: React.CSSProperties
  readonly className?: string
  readonly type?: string
  readonly id?: string
  readonly testLabel?: string
  readonly name?: string
  readonly value: string | number | undefined
  readonly onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void
  readonly onKeyDown?: KeyboardEventHandler<HTMLInputElement>
  readonly placeholder?: string
  readonly showCount?: boolean
  readonly prefix?: React.ReactNode
  readonly suffix?: React.ReactNode
  readonly maxLength?: number
  readonly status?: '' | 'warning' | 'error' | undefined
  readonly onBlur?: React.FocusEventHandler<HTMLInputElement>
  readonly onFocus?: React.FocusEventHandler<HTMLInputElement>
  readonly disabled?: boolean
  readonly addonBefore?: React.ReactNode
  readonly addonAfter?: React.ReactNode
  readonly autoComplete?: string
  readonly autoFocus?: boolean | undefined
  readonly readOnly?: boolean | undefined
  readonly count?: any
  readonly required?: any
  allowClear?: boolean
}

// Convert to forwardRef and remove ref from props
const CustomInput = forwardRef<any, CustomInputProps>((props, ref) => {
  const {
    inputVariant = 'default',
    pattern,
    styles,
    className,
    type,
    id,
    testLabel,
    value,
    onChange,
    onKeyDown,
    placeholder,
    showCount,
    prefix,
    suffix,
    maxLength,
    status,
    onBlur,
    onFocus,
    disabled,
    readOnly,
    addonBefore,
    addonAfter,
    autoComplete,
    autoFocus,
    count,
    required,
    ...restProps
  } = props

    // Global key restriction for number inputs
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    // Call existing onKeyDown if provided
    if (onKeyDown) {
      onKeyDown(e);
    }

    // Only apply restriction for number inputs and if not prevented by parent
    if (type === 'number' && !e.defaultPrevented) {
      // Prevent 'e', 'E', '+', '-' characters in number inputs
      if (['e', 'E', '+', '-'].includes(e.key)) {
        e.preventDefault();
      }
    }
  }


  return (
    <Input
      aria-label={testLabel}
      ref={ref}
      pattern={pattern}
      style={styles}
      className={`antd-custom-input ${inputVariant} ${className}`}
      id={id}
      type={type}
      value={value}
      onChange={onChange}
      onKeyDown={handleKeyDown}
      placeholder={placeholder}
      showCount={showCount}
      prefix={prefix}
      suffix={suffix}
      maxLength={maxLength}
      status={status}
      onBlur={onBlur}
      onFocus={onFocus}
      disabled={disabled}
      addonBefore={addonBefore}
      addonAfter={addonAfter}
      autoComplete={autoComplete}
      autoFocus={autoFocus}
      readOnly={readOnly}
      count={count}
      {...restProps} // 🔥 This ensures props like `data-testid` reach DOM
    />
  )
})

export default CustomInput
