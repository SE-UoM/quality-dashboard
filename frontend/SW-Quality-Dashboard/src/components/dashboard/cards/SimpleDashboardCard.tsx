
export default function SimpleDashboardCard({id, style, className, ...rest}) {
    return (
        <div className={"dashboard-card card bg-base-200 shadow " + className}
             style={style}
             id={id}
        >
            {rest.children}
        </div>
    )
}