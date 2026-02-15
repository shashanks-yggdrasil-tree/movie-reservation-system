/* eslint-disable @typescript-eslint/no-explicit-any */
import { Tag } from 'antd'
import './CustomTags.scss'
interface CustomTagsProps {
  readonly variant?: string
  readonly color: 'success' | 'processing' | 'error' | 'warning' | '#f50' | '#2db7f5' | '#87d068' | '#108ee9'
  readonly title: string
  readonly className?: string
}
export default function CustomTags({ variant = 'default', color, title, className }: CustomTagsProps) {
  return (
    <Tag
      className={`antd-custom-tags m-auto px-[4px] py-[2px] text-center text-xs md:min-w-14 ${variant} ${className}`}
      color={color}
    >
      {title}
    </Tag>
  )
}
