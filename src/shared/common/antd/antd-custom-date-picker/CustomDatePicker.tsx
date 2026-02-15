/* eslint-disable @typescript-eslint/no-explicit-any */
import { DatePicker } from 'antd'
import './CustomDatePicker.scss'

function CustomDatePicker({
  varient = 'default',
  showTime = false,
  defaultValue,
  className,
  format,
  disabledDate,
  onChange,
  value,
  disabled,
  placeholder,
  minDate,
  maxDate,
  id
}: any) {
  return (
    <DatePicker
      showTime={showTime}
      id={id}
      placeholder={placeholder}
      defaultValue={defaultValue}
      className={`custom-date-picker ${varient} ${className}`}
      format={format}
      onChange={onChange}
      value={value}
      disabledDate={disabledDate}
      disabled={disabled}
      minDate={minDate}
      maxDate={maxDate}
    />
  )
}

export default CustomDatePicker
