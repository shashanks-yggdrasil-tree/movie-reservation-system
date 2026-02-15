import { FormItemProps } from 'antd';

export interface MoneyInputControl {
  attrName?: string;
  attrLabel?: string;
  attrPlaceHolder?: string;
  className?: string;
  disabled?: boolean;
  required?: boolean;
  minValue?: number;
  maxValue?: number;
  step?: number;
  precision?: number;
  currencySymbol?: string;
  type?: string
  readOnly?: boolean
}

export interface MoneyInputProps extends Omit<FormItemProps, 'name' | 'label' | 'rules'> {
  fieldControls: MoneyInputControl;
  field?: any;
  onChange?:any  
  suffix?:any  
  addonAfter?:any  
  isTableSection?: boolean;
  customValidationRules?: any[];
  type?: any,
  readOnly?: boolean,
  disabled?: boolean,
  customClassName?: string;
  showLabel?: boolean;
}