import { DownloadOutlined } from '@ant-design/icons';
import type { TabsProps } from 'antd';
import { Button, Tabs } from 'antd';
import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import * as XLSX from 'xlsx';
import { showErrorToast, showSuccessToast } from '../../toast/toastConfig';
import CustomModal from '../antd-custom-modal/CustomModal';
import CustomDataTable from '../antd-custom-table/CustomDataTable';
import './tabs.scss';

const ResultModal = ({
  isModalVisible,
  setIsModalVisible,
  data,
  columns,
  successToast,
  failureToast,
  marqueeValueOnSucess,
  marqueeValueOnFailure,
  isPricingInput,
  updatePricingData
}: {
  isModalVisible: boolean
  setIsModalVisible: Dispatch<SetStateAction<boolean>>
  data: any
  columns: any
  successToast?: any
  failureToast?: any
  marqueeValueOnSucess?: any
  marqueeValueOnFailure?: any
  isPricingInput?:any
  updatePricingData?: () => void 
}) => {
  const failureData = data?.failure?.data || []
  const successData = data?.success?.data || []
  const [successMessage, setSuccessMessage] = useState('')
  const [failureMessage, setFailureMessage] = useState('')

  // console.log("yashika",columns)
  const exportToExcel = (tabKey: string) => {
    const exportData = tabKey === '1' ? successData : failureData
    const fileName = tabKey === '1' ? 'success_data.xlsx' : 'failure_data.xlsx'

    // Transform data to match column headers
    const exportColumns = tabKey === '1' ? columns.slice(1, -1) : columns
    const formattedData = exportData.map((item: any) => {
      const row: any = {}
      exportColumns.forEach((col: any) => {
        row[col.title] = item[col.dataIndex]
      })
      return row
    })

    // Create worksheet
    const ws = XLSX.utils.json_to_sheet(formattedData)
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, 'Data')

    // Save file
    XLSX.writeFile(wb, fileName)
  }

  useEffect(() => {
    if (!isModalVisible) return
    if (failureToast !== undefined) {
      showErrorToast(failureToast)
    } else if (successToast !== undefined) {
      showSuccessToast(successToast)
    }
  }, [isModalVisible])

  useEffect(() => {
    if (data?.failureCount > 0 || marqueeValueOnFailure) {
      setFailureMessage('Note: No data saved to database. Correct highlighted errors, then re-upload.')
    }
    if (data?.successCount > 0 || marqueeValueOnSucess) {
      setSuccessMessage('Note: Record(s) Uploaded Successfully')
    }
  }, [data])

  const TabHeader = ({ tabKey }: { tabKey: string }) => (
    <div className="flex w-full items-center justify-between">
      <Button
        type="primary"
        icon={<DownloadOutlined />}
        onClick={(e) => {
          e.stopPropagation()
          exportToExcel(tabKey)
        }}
        className="ml-4"
      >
        Export to Excel
      </Button>
    </div>
  )

  const items: TabsProps['items'] = [
    {
      key: '1',
      label: (
        <div className="flex items-center">
          <span>Success {data?.successCount}</span>
        </div>
      ),
      children: (
        <>
          <div className="animate-marquee inline-block text-green-500">{successMessage}</div>
          {data?.successCount > 0 ? (
            <>
              <div className="flex items-center space-x-2">
                <TabHeader tabKey="1" />
              </div>
              <CustomDataTable
                columns={columns?.filter((column: any) => column.dataIndex !== 'errors')}
                data={successData ? successData : []}
              />
            </>
          ) : (
            <CustomDataTable
              columns={columns?.filter((column: any) => column.dataIndex !== 'errors')}
              data={successData ? successData : []}
            />
          )}
        </>
      )
    },
    {
      key: '2',
      label: (
        <div className="flex items-center">
          <span>Failure {data?.failureCount}</span>
        </div>
      ),
      children: (
        <>
          <div className="animate-marquee inline-block">{failureMessage}</div>
          {data?.failureCount > 0 ? (
            <>
              <div className="flex items-center space-x-2">
                <TabHeader tabKey="2" />
              </div>
              <CustomDataTable columns={columns} data={failureData ? failureData : []} />
            </>
          ) : (
            <CustomDataTable columns={columns} data={failureData ? failureData : []} />
          )}
        </>
      )
    }
  ]

  const TabsComponent: React.FC = () => {
    return (
      <Tabs
        defaultActiveKey={data?.successCount > 0 ? '1' : '2'}
        type="card"
        items={items}
        indicator={{ size: (origin) => origin - 20 }}
      />
    )
  }
  const handleCancel=()=>{
    if(isPricingInput){
     console.log(updatePricingData?.());
    }
    setIsModalVisible(false)
  }
  return (
    <CustomModal
      title="Import Data"
      centered={false}
      open={isModalVisible}
      onCancel={() =>handleCancel() }
      footer={null}
      width="100%"
      styles={{
        body: { height: 'calc(100vh - 110px)' }
      }}
      style={{paddingBottom: 0 }}
      wrapClassName="exportModal"
      className="bigModal fullScreenModal"
    >
      <TabsComponent />
    </CustomModal>
  )
}

export default ResultModal

// import type { TabsProps } from 'antd'
// import { Tabs } from 'antd'
// import React, { Dispatch, SetStateAction } from 'react'
// import CustomModal from '../antd-custom-modal/CustomModal'
// import CustomDataTable from '../antd-custom-table/CustomDataTable'
// import './tabs.scss'

// // const onChange = (key: string) => {
// //   console.log(key)
// // }

// const ResultModal = ({
//   isModalVisible,
//   setIsModalVisible,
//   data,
//   columns
// }: {
//   isModalVisible: boolean
//   setIsModalVisible: Dispatch<SetStateAction<boolean>>
//   data: any
//   columns: any
// }) => {
//   const failureData = data?.failure?.data || []
//   const successData = data?.success?.data || []

//   const items: TabsProps['items'] = [
//     {
//       key: '1',
//       label: `Success ${data?.successCount}`,
//       children: data?.successCount > 0 ? <CustomDataTable columns={columns.slice(1, -1)} data={successData} /> : ''
//     },
//     {
//       key: '2',
//       label: `Failure ${data?.failureCount}`,
//       children: data?.failureCount > 0 ? <CustomDataTable columns={columns} data={failureData} /> : ''
//     }
//   ]

//   const TabsComponent: React.FC = () => {
//     return (
//       <>
//         <Tabs
//           defaultActiveKey={data?.successCount > 0 ? '1' : '2'}
//           type="card"
//           items={items}
//           // onChange={onChange}
//           indicator={{ size: (origin) => origin - 20 }}
//         />
//       </>
//     )
//   }

//   const content = <TabsComponent />

//   return (
//     <CustomModal
//       title="Export Data"
//       centered={true}
//       open={isModalVisible}
//       onCancel={() => setIsModalVisible(false)}
//       footer={null}
//       width={2000}
//       className="bigModal"
//     >
//       {content}
//     </CustomModal>
//   )
// }

// export default ResultModal
