import InfoToolTip from '@src/shared/components/common-form-components/InfoToolTip'
import { Select, SelectProps, Spin } from 'antd'
import './CustomSelect.scss'

// Import BaseSelectRef from antd
import type { BaseSelectRef } from 'rc-select'
import { forwardRef } from 'react'

// Extend BaseSelectRef to include custom methods
export interface CustomSelectRef extends BaseSelectRef {
  focus: () => void
  blur: () => void
  focusSearch?: () => void // Made optional to avoid type mismatch
}

const { Option } = Select

interface CustomSelectProps extends SelectProps {
  readonly inputVariant?: string
  readonly customPopupClassName?: string
  readonly helperMsg?: string
  readonly showCountryCode?: boolean
  readonly showDropDownSearch?: boolean
  readonly showDropDownClear?: boolean
  readonly clearField?: () => void
  readonly required?: boolean
  readonly style?: any
  readonly disableOptionsAsPerData?: boolean
}

const CustomSelect = forwardRef<CustomSelectRef, CustomSelectProps>(
  (
    {
      inputVariant = 'default',
      mode,
      id,
      open,
      showSearch,
      className,
      notFoundContent,
      customPopupClassName,
      popupClassName = `bg-[#ffffff1f] backdrop-blur-3xl ${customPopupClassName}`,
      style,
      placeholder = 'Select',
      helperMsg,
      placement,
      value,
      onClear,
      onChange,
      onSearch,
      options = [],
      status,
      disabled,
      virtual,
      labelRender,
      loading = false,
      popupMatchSelectWidth,
      maxCount,
      defaultValue,
      optionLabelProp,
      onBlur,
      allowClear = true,
      onClick,
      tokenSeparators,
      disableOptionsAsPerData = true,
      dropdownRender,
      ...restProps
    },
    ref
  ) => {
    // Intelligent value transformer
    const transformValue = (val: any) => {
      // If value is an array of objects
      if (Array.isArray(val)) {
        return val.map((item) =>
          // Automatically detect and extract ID-like property
          typeof item === 'object' && item !== null
            ? item.productTypeId || item.branchId || item.id || item.value
            : item
        )
      }

      // If single object
      if (typeof val === 'object' && val !== null) {
        return val.productTypeId || val.branchId || val.id || val.value || val
      }

      // Return original value if no transformation needed
      return val
    }



     // ✅ Global filter: exclude options marked as `isExcluded`
    const filteredOptions = Array.isArray(options)
    ? options.filter((opt) => !opt?.isExcluded)
    : []


    return (
      <div className="relative">
        <Select
          ref={ref}
          showSearch={showSearch ?? true}
          filterOption={(inputValue: any, option: any) => {
            const label = option?.label || option?.children
            return (label ?? '').toLowerCase().includes(inputValue.toLowerCase())
          }}
          id={id}
          onClear={onClear}
          allowClear={allowClear}
          mode={mode}
          className={`antd-custom-select ${inputVariant} ${className} rounded-[12px]`}
          onClick={onClick}
          onMouseDown={(e) => e.stopPropagation()}
          popupClassName={`${popupClassName} custom-select-dropdown`}
          style={style}
          placeholder={placeholder}
          placement={placement}
          open={open}
          value={transformValue(value)}
          onChange={onChange}
          onSearch={onSearch}
          status={status}
          disabled={disabled}
          virtual={virtual}
          labelRender={labelRender}
          loading={loading}
          notFoundContent={
            loading ? (
              <Spin size="small" />
            ) : notFoundContent ? (
              notFoundContent
            ) : (
              <div className="text-white">No Data</div>
            )
          }
          popupMatchSelectWidth={popupMatchSelectWidth}
          dropdownStyle={{ maxHeight: 300, overflowY: 'auto' }}
          maxCount={maxCount}
          defaultValue={defaultValue}
          optionLabelProp={optionLabelProp}
          onBlur={onBlur}
          dropdownAlign={{
            overflow: {
              adjustY: false,
              adjustX: false
            }
          }}
          // dropdownRender={dropdownRender}// this was deprecated
          popupRender={dropdownRender}
          tokenSeparators={tokenSeparators}
          {...restProps} // 🔥 This ensures props like `data-testid` reach DOM
        >
          {filteredOptions.map((option: any) => (
            <Option key={option.value} value={option.value} disabled={disableOptionsAsPerData ? option.disabled : false} title={option.title}>
              {option.label}
            </Option>
          ))}
        </Select>
        {helperMsg && (
          <div className="absolute bottom-3 right-7 top-1/2 -translate-y-1/2 transform">
            <InfoToolTip helperMsg={helperMsg} />
          </div>
        )}
      </div>
    )
  }
)

CustomSelect.displayName = 'CustomSelect'

export default CustomSelect
