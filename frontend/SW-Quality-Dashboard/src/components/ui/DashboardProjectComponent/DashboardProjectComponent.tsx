import './DashboardProjectComponent.css'
import medalIcon from '../../../assets/svg/dashboardIcons/simple_medal_icon.svg'

function DashboardProjectComponent({projectName, owner, rank}) {
    let rankColor;
    if (rank === 1) {
        rankColor = 'gold';
    } else if (rank === 2) {
        rankColor = 'silver';
    } else if (rank === 3) {
        rankColor = 'bronze';
    } else {
        rankColor = ''; // Empty rank color for ranks beyond 3
    }

    return (
        <div className="best-project-component">
            <div className={"best-project-medal "}>
                <img src={medalIcon} className={rankColor}/>
                <span>{rank}</span>
            </div>
            <h2 className="best-project-item-title">{projectName}</h2>
            <p className="best-project-item-owner">
                By: {owner}
            </p>
        </div>
    )
}

export default DashboardProjectComponent