import './IconCard.css'
import '../../DashboardCardStyle.css'

function formatHeaderText(headerText) {
    if (!isNaN(headerText) && parseInt(headerText) >= 1000) {
        const num = parseInt(headerText);
        const roundedNum = Math.round(num / 100) / 10;
        return roundedNum + "k";
    }
    return headerText;
}

function IconCard({icon, headerText, caption, gridAreaName}) {
    const formattedHeaderText = formatHeaderText(headerText);

    return (
        <>
            <div
                className={"dashboard-card " }
                id="iconCard"
                style={{gridArea: gridAreaName}}
            >
                <img src={icon} className="icon-card-icon"/>
                <div className="icon-card-header">
                    <h2>{formattedHeaderText}</h2>
                    <h3>{caption}</h3>
                </div>
            </div>
        </>
    )
}

export default IconCard
