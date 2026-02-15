import { CurrentContext } from '@src/modules/configurators/modules/ldc/ldc-deviation/Index'
import { Steps } from 'antd'
import { useContext } from 'react'

const CustomSteps = () => {
  const context = useContext(CurrentContext)

  if (!context) {
    throw new Error('ChildComponent must be used within a CurrentContext.Provider')
  }

  const { current, onChange, visibleSteps, stepItems } = context

  const vsLength = visibleSteps.length
  const sLength = stepItems.length

  // const calcPercent = (vsLength / sLength) * 100

  return (
    <>
      <div>
        <Steps
          type="navigation"
          size="small"
          current={current}
          onChange={onChange}
          className="site-navigation-steps"
          items={visibleSteps}
        />
      </div>
    </>
  )
}

export default CustomSteps
