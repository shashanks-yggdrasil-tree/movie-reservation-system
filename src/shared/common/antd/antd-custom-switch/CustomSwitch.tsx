import { Switch } from 'antd'
import React, { ReactNode } from 'react'
import './CustomSwitch.scss'

interface CustomSwitchProps {
  onChange: (checked: boolean) => void
  value: boolean
  className?: string
  rightText?: string | ReactNode
  citation?: boolean
  id?: string
  checked?: boolean
  disabled?: boolean
}

const CustomSwitch: React.FC<CustomSwitchProps> = ({
  onChange,
  value,
  className,
  rightText,
  id,
  checked,
  disabled
}) => (
  <div className="relative flex gap-2">
    <Switch
      defaultChecked
      onChange={onChange}
      value={value}
      id={id}
      className={`${className} custom-switch`}
      checked={checked}
      disabled={disabled}
    />
    {rightText && <p className="text-white/50">{rightText}</p>}
  </div>
)

export default CustomSwitch
