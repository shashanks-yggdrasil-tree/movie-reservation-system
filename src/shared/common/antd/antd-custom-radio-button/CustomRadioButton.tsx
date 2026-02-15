import { Radio } from 'antd'
import './CustomRadioButton.scss'

export interface RadioButton {
  value: string | boolean
  label: string
  disabled?: boolean // ✅ Add this line
}

interface CustomRadioButtonProps {
  options: RadioButton[]
  value?: string | boolean | undefined
  onChange: (event: any) => void
  className?: string
  id?: string
  disabled?: boolean
}

const CustomRadioButtons: React.FC<CustomRadioButtonProps> = ({
  options,
  value,
  className,
  onChange,
  id,
  disabled
}: CustomRadioButtonProps) => {
  return (
    <Radio.Group
      value={value}
      id={id}
      onChange={onChange}
      options={options}
      disabled={disabled}
      className={`${disabled && 'disabled-text'} ${className}`}
    />
  )
}

export default CustomRadioButtons
