import { Breadcrumb } from 'antd'
import { BreadcrumbItemType, BreadcrumbSeparatorType } from 'antd/es/breadcrumb/Breadcrumb'
import './CustomBreadcrumbs.scss'

const CustomBreadcrumbs = ({
  items
}: {
  items: Partial<BreadcrumbItemType & BreadcrumbSeparatorType>[] | undefined
}) => <Breadcrumb separator=">" items={items} className="default" />

export default CustomBreadcrumbs

/**
 * 
 * 
 * 
 * EXAMPLE FOR BREADCRUMBS
 * 
 * 
 * 
 * type BreadcrumbType = {
    '/ldc/deviation/list': { title: string; href: string }[]
    '/ldc/view-deviation': { title: string; href?: string }[] 
    '/ldc/view-deviation/create-select-deviation': ({ title: string; href: string } | { title: string; href?: undefined })[]
    '/ldc/view-deviation/verify-ldc': ({ title: string; href: string } | { title: string; href?: undefined })[]
    '/ldc/view-deviation/deviation-setup': ({ title: string; href: string } | { title: string; href?: undefined })[]
}

const breadcrumbConfig: BreadcrumbType = {
    '/ldc/deviation/list': [{ title: 'LDC Deviation Dashboard', href: '/ldc/deviation/list' }],
    '/ldc/view-deviation': [
        { title: 'LDC Deviation Dashboard', href: '/ldc/deviation/list' },
        { title: 'Create Deviation & Select LDC', }
    ],
    '/ldc/view-deviation/create-select-deviation': [
      { title: 'LDC Deviation Dashboard', href: '/ldc/deviation/list' },
      { title: 'Create Deviation & Select LDC'},
    ],
    '/ldc/view-deviation/verify-ldc': [
        { title: 'LDC Deviation Dashboard', href: '/ldc/deviation/list' },
        { title: 'Create Deviation & Select LDC', href: '/ldc/view-deviation/create-select-deviation' },
        { title: 'Verify LDC' }
    ],
    '/ldc/view-deviation/deviation-setup': [
        { title: 'LDC Deviation Dashboard', href: '/ldc/deviation/list' },
        { title: 'Create Deviation & Select LDC', href: '/ldc/view-deviation' },
        { title: 'Verify LDC', href: '/ldc/view-deviation/verify-ldc' },
        { title: 'Deviation Setup' }
    ]
} as const

const DeviationBreadcrumbs = () => {
    const pathname = useLocation().pathname
    const [realtimeBreadcrumbs, setRealtimeBreadcrumbs] = useState<Array<{ title: string; href?: string }>>([])

    useEffect(() => {
        const breadcrumbs = breadcrumbConfig[pathname as keyof typeof breadcrumbConfig] || []
        setRealtimeBreadcrumbs(breadcrumbs)
    }, [pathname])
    return (
        <div className="m-4">
            <CustomBreadcrumbs items={realtimeBreadcrumbs} />
        </div>
    )
}
 * 
 * 
 * 
 * 
 * 
 */
