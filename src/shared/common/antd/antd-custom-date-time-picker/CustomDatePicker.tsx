import { DatePicker, Form } from 'antd'
import dayjs from 'dayjs'

const CustomDateTimePicker = ({ control, fieldName, form }) => {
  const handleDateChange = (date) => {
    // Simply store the ISO string if date exists, null if it doesn't
    form.setFieldsValue({
      [fieldName()]: date ? date.toISOString() : null
    })
  }

  // Simple function to convert string to dayjs object
  const getCurrentValue = () => {
    const value = form.getFieldValue(fieldName())
    return value ? dayjs(value) : null
  }

  return (
    <Form.Item
      name={fieldName()}
      label={control?.label}
      rules={control?.rules}
      className={`${control?.className} mb-4`}
    >
      <DatePicker
        className="w-full"
        showTime
        format="YYYY-MM-DD HH:mm:ss"
        placeholder={control?.attrPlaceHolder}
        disabled={control?.disabled}
        onChange={handleDateChange}
        value={getCurrentValue()}
      />
    </Form.Item>
  )
}

export default CustomDateTimePicker
