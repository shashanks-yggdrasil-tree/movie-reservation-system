/* eslint-disable @typescript-eslint/no-explicit-any */
import { Table, TablePaginationConfig, TableProps } from 'antd'
import { createStyles } from 'antd-style'
import { SizeType } from 'antd/es/config-provider/SizeContext'
import { ColumnsType } from 'antd/es/table'
import './CustomDataTable.scss'

interface DataType {
  key?: string
  name?: string
  age?: number
  address?: string
}

export const useStyle = createStyles(({ css, token }) => {
  const { antCls } = token
  return {
    customTable: css`
      ${antCls}-table {
        ${antCls}-table-container {
          ${antCls}-table-body,
          ${antCls}-table-content {
            scrollbar-width: thin;
            scrollbar-color: #eaeaea transparent;
            scrollbar-gutter: stable;
          }
        }
      }
    `
  }
})

// Props interface with precise types
interface CustomDataTableProps<T = any> {
  // Required props
  readonly columns: ColumnsType<T>
  readonly data: readonly T[]

  // Optional props
  readonly rowKey?: string | ((record: T) => string)
  readonly components?: any
  readonly rowClassName?: string | ((record: T, index: number) => string)
  readonly className?: string
  readonly size?: SizeType
  readonly loading?: boolean
  readonly bordered?: boolean
  readonly onChange?: TableProps<T>['onChange']
  readonly rowSelection?: TableProps<T>['rowSelection']
  readonly pagination?: TablePaginationConfig | any
  readonly scroll?: any
  readonly isScrollable?: boolean
  readonly total?: number
}

const CustomDataTable = <T extends any>({
  rowKey = 'key',
  components,
  rowClassName,
  className = '',
  columns,
  data,
  size = 'small',
  loading = false,
  bordered = true,
  onChange,
  rowSelection,
  pagination,
  scroll,
  isScrollable = true
}: CustomDataTableProps<T>) => {
  const { styles } = useStyle()

  const enhancedColumns = columns.map((column) => ({
    ...column,
    ellipsis: column.ellipsis ?? true // Add ellipsis if not already set
  }))
  console.log('pagination in custom', pagination)

  const mergedRowClassName = (record: T, index: number): string => {
    let externalClass = ''

    if (typeof rowClassName === 'function') {
      externalClass = rowClassName(record, index)
    } else if (typeof rowClassName === 'string') {
      externalClass = rowClassName
    }

    const altClass = index % 2 === 0 ? 'table-row-light' : 'table-row-dark'
    return [externalClass, altClass].filter(Boolean).join(' ')
  }

  return (
    <Table<T>
      rowKey={rowKey}
      rowClassName={mergedRowClassName}
      // rowClassName={(_, index) =>
      //   index % 2 === 0 ? "table-row-light" : "table-row-dark"
      // }
      components={components}
      className={`antd-custom-table ${className} ${styles.customTable}`.trim()}
      columns={enhancedColumns}
      bordered={bordered}
      rowSelection={rowSelection}
      dataSource={data}
      onChange={onChange}
      size={size}
      scroll={scroll ?? (isScrollable ? { x: 'max-content' } : undefined)}
      // scroll={scroll}
      loading={loading}
      // scroll={{ x: isScrollable ? 'max-content' : '' }}
      pagination={
        pagination
          ? {
              ...pagination,
              showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} items`,
              showQuickJumper: true,
              showSizeChanger: true
            }
          : false
      }

      // Pagination example:
      // {{
      //   current: current,
      //   pageSize: pageSize,
      //   total: totalRecords,
      //   pageSizeOptions: ['10', '20', '50', '100'],
      //   showSizeChanger: true
      // }}
    />
  )
}

export default CustomDataTable

/**
 * 
 * scroll
    Property	Description	Type	Default
    scrollToFirstRowOnChange	Whether to scroll to the top of the table when paging, sorting, filtering changes	boolean	-
    x	Set horizontal scrolling, can also be used to specify the width of the scroll area, could be number, percent value, true and 'max-content'	string | number | true	-
    y	Set vertical scrolling, can also be used to specify the height of the scroll area, could be string or number	string | number	-
 * 
 */
