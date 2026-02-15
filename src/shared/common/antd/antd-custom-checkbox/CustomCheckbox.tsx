import CommonErrorMessage from '@src/shared/components/common-form-components/CommonErrorMessageField'
import CommonLabel from '@src/shared/components/common-form-components/CommonLabel'
import { Checkbox } from 'antd'
import { CheckboxChangeEvent } from 'antd/es/checkbox'
import React, { ReactNode } from 'react'
import { FieldError, FieldErrorsImpl, Merge } from 'react-hook-form'

import './CustomCheckbox.scss'

interface CustomCheckboxProps {
  readonly name: string
  readonly label: string | ReactNode
  readonly value: boolean | undefined
  readonly onChange?: (e: CheckboxChangeEvent) => void
  readonly showAsterisk?: boolean
  readonly disabled?: boolean
  readonly indeterminate?: boolean
  readonly error?: FieldError | Merge<FieldError, FieldErrorsImpl<any>> | undefined
  readonly labelClassName?: string
  readonly containerClassName?: string
  readonly checkboxClassName?: string
}

const CustomCheckbox: React.FC<CustomCheckboxProps> = ({
  name,
  label,
  onChange,
  value,
  showAsterisk = false,
  disabled = false,
  indeterminate = false,
  error,
  labelClassName,
  containerClassName,
  checkboxClassName,
  ...checkboxProps
}) => {
  return (
    <div className={`row-span-1 flex items-center gap-2 ${containerClassName || ''}`}>
      {label && (
        <CommonLabel htmlFor={name} showAsterisk={showAsterisk} className={`cursor-pointer ${labelClassName}`}>
          {label}:
        </CommonLabel>
      )}
      <Checkbox
        type="checkbox"
        onChange={onChange}
        disabled={disabled}
        indeterminate={indeterminate}
        value={value}
        checked={value}
        id={name}
        className={`default ${checkboxClassName}`}
        {...checkboxProps}
      />
      {error && <CommonErrorMessage error={error} />}
    </div>
  )
}

export default CustomCheckbox
