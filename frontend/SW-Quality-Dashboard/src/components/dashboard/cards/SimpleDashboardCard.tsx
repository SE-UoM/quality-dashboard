
export default function SimpleDashboardCard({id, style, ...rest}) {
    return (
        <div className="dashboard-card card bg-base-200 shadow-sm"
             style={style}
             id={id}
        >
            {rest.children}
        </div>
    )
}