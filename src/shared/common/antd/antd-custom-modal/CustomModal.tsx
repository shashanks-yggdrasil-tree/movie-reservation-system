/* eslint-disable */
import { Modal } from 'antd'
import { ReactNode } from 'react'
// import './CustomModal.scss'

interface CustomModal {
  readonly title?: string
  readonly styles?: React.CSSProperties
  readonly onOk?: any
  readonly open: any
  readonly loading?: any
  readonly children: ReactNode
  readonly onCancel?: any
  readonly closable?: any
  readonly closeIcon?: boolean | ReactNode
  readonly centered?: boolean
  readonly footer?: any
  readonly className?: string
  readonly wrapClassName?: string // to add a custom CSS class to the outermost container of the Modal
  readonly maskClosable?: any
  readonly width?: any
  readonly height?: any
  readonly zIndex?:any  
  readonly bodyStyle?: any
}

export default function CustomModal({
  title,
  styles,
  onCancel,
  open,
  loading,
  children,
  onOk,
  closable,
  closeIcon,
  centered = true,
  footer,
  className,
  wrapClassName,
  maskClosable = true,
  width,
  height,
  zIndex
  // bodyStyle
}: CustomModal) {
  return (
    <Modal
      title={title}
      style={styles}
      open={open}
      loading={loading}
      onOk={onOk}
      maskClosable={maskClosable}
      closeIcon={closeIcon ? closeIcon : <img src="/images/modal-close-icon.svg" alt="Close Icon" />}
      closable={closable}
      onCancel={onCancel}
      footer={footer}
      className={`antd-custom-modal ${className}`}
      wrapClassName={wrapClassName}
      centered={centered}
      width={width}
      height={height}
      zIndex={zIndex}  
      // bodyStyle={bodyStyle}
    >
      {children}
    </Modal>
  )
}
