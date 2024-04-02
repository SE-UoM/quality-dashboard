import './IconCard.css'
import '../DashboardCardStyle.css'

function IconCard({icon, headerText, caption, gridAreaName}) {
    return (
        <>
            <div
                className={"dashboard-card " }
                id="iconCard"
                style={{gridArea: gridAreaName}}
            >
                <img src={icon} className="icon-card-icon"/>
                <div className="icon-card-header">
                    <h2>{headerText}</h2>
                    <h3>{caption}</h3>
                </div>
            </div>
        </>
    )
}

export default IconCard