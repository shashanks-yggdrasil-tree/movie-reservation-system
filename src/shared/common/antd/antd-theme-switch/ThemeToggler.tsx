/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { getPreferredTheme } from '../../../../helpers/functions'
import { updateAppTheme } from '../../../../store/appSlice'
import ThemeSwitch from './ThemeSwitch'

const ThemeToggler = () => {
  const appData = useSelector((state: any) => state.app)
  const [theme, setTheme] = useState(appData.theme ? appData.theme : getPreferredTheme(appData.theme))
  const dispatch = useDispatch()

  useEffect(() => {
    const rootElement = document.documentElement
    if (theme === 'dark') {
      rootElement.classList.add('dark')
      dispatch(updateAppTheme('dark'))
    } else {
      rootElement.classList.remove('dark')
      dispatch(updateAppTheme('light'))
    }
  }, [theme, dispatch])

  const handleThemeToggle = () => {
    setTheme((prevTheme: any) => (prevTheme === 'dark' ? 'light' : 'dark'))
  }

  return <ThemeSwitch handleThemeToggle={handleThemeToggle} theme={theme} />
}

export default ThemeToggler
