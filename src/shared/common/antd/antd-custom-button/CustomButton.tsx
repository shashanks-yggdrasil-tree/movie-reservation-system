/* eslint-disable @typescript-eslint/no-explicit-any */
import { IsActionAllowed } from '@src/shared/hooks/useActionPermission'
import { Button } from 'antd'
import React, { ReactNode } from 'react'
import './CustomButton.scss'

interface CustomButton {
  readonly buttonVariant?: string
  readonly htmlType?: 'button' | 'submit' | 'reset'
  readonly className?: string
  readonly onClick?: any
  readonly block?: boolean
  readonly disabled?: boolean
  readonly loading?: boolean
  readonly icon?: React.ReactNode
  readonly iconPosition?: 'end' | 'start'
  readonly size?: 'large' | 'middle' | 'small'
  readonly href?: string
  readonly shape?: any
  readonly target?: React.HTMLAttributeAnchorTarget
  readonly title?: string | ReactNode
  readonly width?: string
  readonly style?: React.CSSProperties
  readonly onMouseEnter?: any
  readonly onMouseLeave?: any
  readonly id?: any
  readonly danger?: any
  readonly actions?: string | string[] | null
  readonly tempEnableActionFlag?: boolean | null
}
export default function CustomButton({
  buttonVariant = 'default',
  htmlType,
  className,
  onClick,
  block,
  disabled,
  loading,
  icon,
  iconPosition,
  size = 'middle',
  href,
  target,
  style,
  title,
  id,
  shape,
  onMouseEnter,
  danger,
  onMouseLeave,
  actions = undefined,
  tempEnableActionFlag = false,
  ...restProps
}: CustomButton) {
  return tempEnableActionFlag ? (
    <IsActionAllowed actions={actions}>
      <Button
        id={id}
        itemID={id}
        danger={danger}
        htmlType={htmlType}
        className={`antd-custom-button small-btn ${buttonVariant} ${className}`}
        onClick={onClick}
        block={block}
        disabled={disabled}
        icon={icon}
        iconPosition={iconPosition}
        loading={loading}
        size={size}
        href={href}
        shape={shape}
        target={target}
        style={style}
        onMouseEnter={onMouseEnter}
        onMouseLeave={onMouseLeave}
        {...restProps} // 🔥 This ensures props like `data-testid` reach DOM
      >
        {title}
      </Button>
    </IsActionAllowed>
  ) : (
    <Button
      id={id}
      itemID={id}
      danger={danger}
      htmlType={htmlType}
      shape={shape}
      className={`antd-custom-button ${buttonVariant} ${className} `}
      onClick={onClick}
      block={block}
      disabled={disabled}
      icon={icon}
      iconPosition={iconPosition}
      loading={loading}
      size={size}
      href={href}
      target={target}
      style={style}
      onMouseEnter={onMouseEnter}
      onMouseLeave={onMouseLeave}
      {...restProps} // 🔥 This ensures props like `data-testid` reach DOM
    >
      {title}
    </Button>
  )
}
