import type { SelectProps } from 'antd'
import { Select, Spin } from 'antd'
import debounce from 'lodash/debounce'
import React, { useEffect, useMemo, useRef, useState } from 'react'
import './CustomSelect.scss'

export interface DebounceSelectProps<ValueType = any>
  extends Omit<SelectProps<ValueType | ValueType[]>, 'options' | 'children'> {
  fetchOptions: (search: string) => Promise<ValueType[]>
  externalOptions: ValueType[] // Add this prop to accept external options
  debounceTimeout?: number
}

export default function DebounceSelect<
  ValueType extends { key?: string; label: React.ReactNode; value: string | number } = any
>({ fetchOptions, debounceTimeout = 800, externalOptions, ...props }: DebounceSelectProps<ValueType>) {
  const [fetching, setFetching] = useState(false)
  // const [options, setOptions] = useState<ValueType[]>([])
  const [options, setOptions] = useState<ValueType[]>(externalOptions || [])
  const fetchRef = useRef(0)

  useEffect(() => {
    if (externalOptions && externalOptions.length > 0) {
      setOptions(externalOptions)
    }
  }, [externalOptions])

  const debounceFetcher = useMemo(() => {
    const loadOptions = (value: string) => {
      fetchRef.current += 1
      const fetchId = fetchRef.current
      // setOptions([])
      setOptions(externalOptions || []) // Reset to external options if they exist
      setFetching(true)

      fetchOptions(value).then((newOptions) => {
        if (fetchId !== fetchRef.current) {
          // for fetch callback order
          return
        }

        setOptions(newOptions)
        setFetching(false)
      })
    }

    return debounce(loadOptions, debounceTimeout)
  }, [fetchOptions, debounceTimeout, externalOptions])

  return (
    <Select
      className="antd-custom-select default"
      filterOption={false}
      onSearch={debounceFetcher}
      notFoundContent={fetching ? <Spin size="small" /> : 'Type atleast two characters'}
      {...props}
      allowClear
      options={options}
    />
  )
}
