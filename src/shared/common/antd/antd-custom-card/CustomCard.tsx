import { Card } from 'antd'
import { ReactNode } from 'react'
import './CustomCard.scss'

const CustomCard = ({
  title,
  className,
  children,
  size
}: {
  title: string
  className: string
  children: ReactNode
  size: string
}) => {
  //size={size}

  return (
    <Card title={title} className={className}>
      {children}
    </Card>
  )
}

export default CustomCard
