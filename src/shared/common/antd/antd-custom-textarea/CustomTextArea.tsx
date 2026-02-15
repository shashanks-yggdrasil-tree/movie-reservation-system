import { Input } from 'antd'
import './CustomTextArea.scss'

const { TextArea } = Input

interface CustomTextArea {
  readonly inputVariant?: string
  readonly className?: string
  readonly placeholder?: string
  readonly rows?: number
  readonly name: string
  readonly onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void
  readonly value: any
  readonly readOnly?:any  
  readonly showCount?: boolean
  readonly id?:any
  readonly maxLength?: number
  readonly allowClear?: boolean
  readonly status?: '' | 'warning' | 'error' | undefined
  readonly disabled?: boolean
  readonly style?: React.CSSProperties | object
  readonly resize?: 'both' | 'none'
}

export default function CustomTextArea({
  inputVariant = 'default',
  className,
  placeholder,
  rows = 4,
  name,
  onChange,
  readOnly,
  value,
  showCount,
  id,
  maxLength,
  allowClear,
  status,
  disabled,
  style,
  resize = 'both'
}: CustomTextArea) {
  return (
    <TextArea
      className={`antd-custom-textarea ${inputVariant} ${className}`}
      placeholder={placeholder}
      rows={rows}
      id={id}
      name={name}
      readOnly={readOnly}
      value={value}
      onChange={onChange}
      showCount={showCount}
      maxLength={maxLength}
      allowClear={allowClear}
      status={status}
      disabled={disabled}
      style={{ resize: resize, ...style }}
    />
  )
}
