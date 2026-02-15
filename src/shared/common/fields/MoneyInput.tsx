import { formatNumberWithCommas } from "@src/shared/utils/general/common-helpers";
import { Form, InputNumber } from "antd";
import { MoneyInputProps } from "./types";


// In MoneyInput component 
const MoneyInput: React.FC<MoneyInputProps> = ({
  fieldControls,
  suffix,
  addonAfter,
  onChange,
  field, // This should come from parent Controller's render prop
  isTableSection = false,
  customClassName,
  showLabel = true,
  ...formItemProps
}) => {

  const LabelComponent = showLabel ? (
    <span>{fieldControls?.attrLabel || field?.name}</span>
  ) : undefined;

  return (
    <Form.Item
      label={LabelComponent}
      className={`
        ${fieldControls?.className || ''} 
        ${customClassName || ''}
        w-full 
        ${isTableSection ? 'table-section-field' : ''}
      `}
      {...formItemProps}
    >
      <InputNumber<number>
        {...field} // Use the field from parent Controller
        formatter={(value) => (value ? formatNumberWithCommas(value) : '')}
        parser={(value) => value?.replace(/\$\s?|(,*)/g, '') as unknown as number}
        placeholder={fieldControls?.attrPlaceHolder || 'Enter amount'}
        size="middle"
        disabled={fieldControls?.disabled}
        className="w-full"
        min={fieldControls?.minValue}
        max={fieldControls?.maxValue}
        type={fieldControls?.type}
        step={fieldControls?.step || 1}
        readOnly={fieldControls?.readOnly}
        suffix={suffix}
        addonAfter={addonAfter}
        precision={fieldControls?.precision || 2}
        {...(!onChange ? {} : { onChange })}
      />
    </Form.Item>
  );
};

export default MoneyInput;


