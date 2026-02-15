import { Input } from 'antd'
import './CustomSearchInput.scss'

type SearchProps = React.ComponentProps<typeof Search>
const { Search } = Input

export default function CustomSearchInput(props: SearchProps, className?: string) {
  return <Search {...props} className={`custom-search default ${className}`} />
}
