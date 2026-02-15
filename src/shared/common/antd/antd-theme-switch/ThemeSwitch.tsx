/* eslint-disable @typescript-eslint/no-explicit-any */
import { Switch } from 'antd'

import './ThemeSwitch.scss'

const ThemeSwitch = ({ handleThemeToggle, theme }: { handleThemeToggle: any; theme: any }) => {
  return (
    <>
      <Switch
        onChange={handleThemeToggle}
        checkedChildren={<div className="dark:bg-sun h-[22px] bg-center"></div>}
        unCheckedChildren={<div className="bg-moon h-[22px] rotate-[270deg] bg-cover bg-center"></div>}
        checked={theme === 'dark' ? true : false}
        className="theme-switch"
      />
    </>
  )
}

export default ThemeSwitch
