/* eslint-disable @typescript-eslint/no-explicit-any */
import { IsActionAllowed } from '@src/shared/hooks/useActionPermission'
import { Collapse, CollapseProps } from 'antd'
import './CustomCollapse.scss'

interface CustomCollapse {
  readonly items?: CollapseProps['items']
  readonly ghost?: boolean
  readonly accordion?: boolean
  readonly onChange?: (key: string | string[]) => void
  readonly bordered?: boolean
  readonly defaultActiveKey?: string[]
  readonly destroyOnHidden?: boolean
  readonly activeKey?: string[]
  readonly expandIconPosition?: 'start' | 'end'
  readonly children?: any
  readonly actions?: string | string[] | null // Added for RBAC
  readonly tempEnableActionFlag?: boolean | null // Added for RBAC
  readonly className?: string
}

export default function CustomCollapse({
  items,
  ghost,
  accordion,
  onChange,
  bordered,
  defaultActiveKey,
  destroyOnHidden,
  activeKey,
  expandIconPosition,
  children,
  actions = undefined,
  tempEnableActionFlag = false,
  className
}: CustomCollapse) {
  const collapseComponent = (
    <Collapse
      className={`custom-collapse ${className}`}
      items={items}
      ghost={ghost}
      accordion={accordion}
      onChange={onChange}
      bordered={bordered}
      defaultActiveKey={defaultActiveKey}
      destroyOnHidden={destroyOnHidden}
      activeKey={activeKey}
      expandIconPosition={expandIconPosition}
      expandIcon={({ isActive }) => (
        <img src="/images/arrow-circle-up.svg" alt="img" className={`${isActive ? 'rotate-90' : 'rotate-0'}`} />
      )}
    >
      {children}
    </Collapse>
  )

  return tempEnableActionFlag ? (
    <IsActionAllowed actions={actions}>{collapseComponent}</IsActionAllowed>
  ) : (
    collapseComponent
  )
}

// import { ExtendedCollapseItem } from '@src/shared/globalTypes/globalTypes';
// import { IsActionAllowed } from '@src/shared/hooks/useActionPermission';
// import { Collapse } from 'antd';
// import './CustomCollapse.scss';

// // Extend the CustomCollapse interface
// interface CustomCollapse {
//   readonly items?: ExtendedCollapseItem[]; // Use the extended item type
//   readonly ghost?: boolean;
//   readonly accordion?: boolean;
//   readonly onChange?: (key: string | string[]) => void;
//   readonly bordered?: boolean;
//   readonly defaultActiveKey?: string[];
//   readonly activeKey?: string[];
//   readonly expandIconPosition?: 'start' | 'end';
//   readonly children?: any;
//   readonly actions?: string | string[] | null; // For the entire collapse
//   readonly tempEnableActionFlag?: boolean | null; // For the entire collapse
// }

// export default function CustomCollapse({
//   items,
//   ghost,
//   accordion,
//   onChange,
//   bordered,
//   defaultActiveKey,
//   activeKey,
//   expandIconPosition,
//   children,
//   actions = undefined,
//   tempEnableActionFlag = false,
// }: CustomCollapse) {
//   // Process items to apply RBAC to individual items
//   const processedItems = items?.map((item) => ({
//     ...item,
//     children: item.actions && tempEnableActionFlag ? (
//       <IsActionAllowed actions={item.actions}>{item.children}</IsActionAllowed>
//     ) : (
//       item.children
//     ),
//   }));

//   const collapseComponent = (
//     <Collapse
//       className="custom-collapse"
//       items={processedItems}
//       ghost={ghost}
//       accordion={accordion}
//       onChange={onChange}
//       bordered={bordered}
//       defaultActiveKey={defaultActiveKey}
//       activeKey={activeKey}
//       expandIconPosition={expandIconPosition}
//       expandIcon={({ isActive }) => (
//         <img
//           src="/images/arrow-circle-up.svg"
//           alt="img"
//           className={`${isActive ? 'rotate-90' : 'rotate-0'}`}
//         />
//       )}
//     >
//       {children}
//     </Collapse>
//   );

//   return tempEnableActionFlag && actions ? (
//     <IsActionAllowed actions={actions}>{collapseComponent}</IsActionAllowed>
//   ) : (
//     collapseComponent
//   );
// }
