import { useUploadFileMutation } from '@src/shared/services/sharedApi'
import { getItems } from '@src/shared/utils/general/common-helpers'
import { GetProp, Tooltip, Upload, UploadFile, UploadProps } from 'antd'
import { UploadListProgressProps, UploadListType } from 'antd/es/upload/interface'
import React, { useState } from 'react'
import { showErrorToast, showSuccessToast } from '../../toast/toastConfig'
import CustomButton from '../antd-custom-button/CustomButton'

import './CustomFileInput.scss'

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0]

interface CustomFileInput {
  actions?: any
  tempEnableActionFlag?: any
  accept?: string
  multiple?: boolean
  maxCount?: number
  disabled?: boolean
  fileList?: UploadFile[]
  progress?: UploadListProgressProps
  className?: string
  listType?: UploadListType
  isVerification?: boolean
  isTidDetails?: boolean
  isTidDetailsId?: string
  isLargeCorporate?: boolean

  setIsModalVisible?: any
  setResponseData?: any
  refetch?: any
  setTableData?: any
  uploadEndpoint?: any
  handleFileUpload?: any
  onChange?: any
  isPricingInput?: any
  applicationId?: any
  isImport?: any
  createdBy?: any
  title?: any
  onlyExcelAllowed?: any
  filePayload?: any
  isMasterFalse?: any
  isAllowed?: boolean /// check is mandatory or optional
  isRBI?: boolean
}

const CustomFileInput: React.FC<CustomFileInput> = ({
  actions,
  tempEnableActionFlag,
  accept,
  multiple = false,
  disabled,
  maxCount = 1,
  progress,
  className,
  listType = 'picture',
  isVerification = false,
  isTidDetails = false,
  isTidDetailsId,
  isLargeCorporate = false,

  setIsModalVisible = false,
  setResponseData = false,
  refetch,
  setTableData,
  uploadEndpoint,
  handleFileUpload,
  onChange,
  isPricingInput,
  applicationId,
  isImport,
  createdBy,
  title = 'Import Data',
  onlyExcelAllowed = false,
  filePayload,
  isMasterFalse = false,
  isAllowed,
  isRBI
}) => {
  const [fileList, setFileList] = useState<UploadFile[]>([])
  const [iconSrc, setIconSrc] = useState('/images/upload-icon-black.svg')
  const [uploadFile, { isLoading: isUploading, isSuccess, isError }] = useUploadFileMutation()
  /*   const [uploadFileData, { isLoading: isFileUploading }] = useSubmitFilePayloadMutation() */
  const handleUpload = async () => {
    try {
      const formData = new FormData()
      fileList.forEach((file: any) => {
        if (isLargeCorporate || isRBI) {
          formData.append('file', file)
        } else {
          formData.append('files', file)
        }
        if (isRBI) {
          formData.append('userId', createdBy)
        }
      })
      if (isLargeCorporate) {
        formData.append('createdBy', createdBy)
      }
      if (isVerification) {
        const payload = {
          listOfUniqueDocumentTypeCodes: ['VERIFICATION'],
          application_id: isTidDetailsId,
          document_type_code: 'VERIFICATION',
          dms_file_extension_names: ['.pdf', '.xls', '.xlsx']
        }
        formData.append('payload', JSON.stringify(payload))
      }
      if (isTidDetails) {
        const payload = {
          listOfUniqueDocumentTypeCodes: ['PROCESSOR_INFO'],
          application_id: isTidDetailsId,
          document_type_code: 'PROCESSOR_INFO',
          dms_file_extension_names: ['.jpg', '.pdf', '.png']
        }

        formData.append('payload', JSON.stringify(payload))
      }
      if (isPricingInput && isImport) {
        formData.append('applicationId', applicationId)
        fileList.forEach((file) => {
          formData.append('file', file)
        })
        formData.append('createdBy', createdBy)
      }
      console.log('omg', formData)
      const response = await uploadFile({ url: uploadEndpoint, data: formData }).unwrap()
      console.log('AI is imp', response)

      if (response && (response.successCount > 0 || response.failureCount > 0)) {
        setIsModalVisible(true)
        setResponseData(response)
        console.log(response)
        console.log('response set')

        setFileList([])

        if (refetch) {
          const { data } = await refetch()
          const items = getItems(data)
          setTableData(items || [])
        }

        if (response.successCount > 0) {
          // showSuccessToast('File uploaded successfully')
        } else if (response.failureCount > 0) {
          // showErrorToast('Failed to upload file')
        } else {
          const errorMessage = response.errors?.files[0] || 'Upload failed'
          showErrorToast(errorMessage)
          setResponseData(null)
        }
      }
    } catch (error) {
      console.error('Upload error:', error)
      // showErrorToast('Failed to upload file')
      setResponseData(null)
    }
  }

  const beforeUpload = (file: File) => {
    console.log('for uploading', file)
    if (file) {
      const isFileExcel =
        file.type === 'application/vnd.ms-excel' || // .xls
        file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || // .xlsx
        file.type === 'application/vnd.ms-excel.sheet.macroEnabled.12' // .xlsx

      if (!isFileExcel && onlyExcelAllowed) {
        showErrorToast('You can upload file in .xls or .xlsx formats only.')
        return false
      }

      const isLt20M = file.size / 1024 / 1024 < 20

      if (!isLt20M) {
        showErrorToast('File size must be smaller than 20MB.')
        return false
      }
    }

    setFileList([...fileList, file])

    return false
  }

  const itemRender = (originNode: any, file: any) => {
    console.log('file', file)

    return (
      <div className="ant-upload-list-item-custom">
        {React.cloneElement(originNode, {
          children: (
            <>
              <div className="ant-upload-custom-file-name">
                {isAllowed == true || (isMasterFalse == false && <Tooltip title={file.name}>{file.name}</Tooltip>)}
              </div>
              {/* {originNode.props.children} // commented */}
              {isAllowed == true || (isMasterFalse == false && originNode.props.children)}
            </>
          )
        })}
      </div>
    )
  }

  const props: UploadProps = {
    onRemove: (file) => {
      const index = fileList.indexOf(file)
      const newFileList = fileList.slice()
      newFileList.splice(index, 1)
      setFileList(newFileList)
    },
    beforeUpload: beforeUpload,
    fileList,
    multiple: multiple,
    accept: accept,
    disabled: disabled,
    maxCount: maxCount,
    progress: progress,
    className: className,
    itemRender: itemRender,
    listType: listType,
    onChange: onChange
  }

  return (
    <>
      <Upload {...props}>
        <CustomButton
          actions={actions}
          tempEnableActionFlag={tempEnableActionFlag}
          title={title}
          className="btn-tertiary"
          id="importFile"
          onMouseEnter={() => setIconSrc('/images/upload-icon-white.svg')}
          onMouseLeave={() => setIconSrc('/images/upload-icon-black.svg')}
          icon={<img src={iconSrc} alt="upload" />}
        />
      </Upload>
      {fileList.length !== 0 && isMasterFalse == false && (
        <CustomButton
          title={isUploading ? 'Uploading' : 'Start Upload'}
          className="btn-tertiary"
          id="upload"
          // onClick={handleUpload}
          onClick={handleFileUpload || handleUpload}
          disabled={fileList.length === 0}
          loading={isUploading}
        />
      )}
    </>
  )
}

export default CustomFileInput
