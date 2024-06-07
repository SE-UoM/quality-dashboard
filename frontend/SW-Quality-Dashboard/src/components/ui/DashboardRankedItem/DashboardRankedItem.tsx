import './DashboardRankedItem.css'
import medalIcon from '../../../assets/svg/dashboardIcons/simple_medal_icon.svg'

function DashboardRankedItem({projectName, headerUrl, rank, loading, children}) {
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
        <div className={"best-project-component" + (loading ? " loading" : "")}
             style={{
                 display: "grid",
                 width: "100%",
                 height: "min-content",
                 gridTemplateColumns: "repeat(3, 1fr)",
                 gridTemplateRows: "repeat(2, 1fr)",

                 gridTemplateAreas: `
                    "medal projectTitle projectTitle"
                    "medal projectOwner projectOwner"
                 `,

                 borderBottom: "1px solid var(--border-primary)",
             }}
        >
                <>
                    <div className={"best-project-medal " + rankColor}
                        style={{
                            gridArea: "medal",
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            height: "8vh",
                            width: "8vh",
                            margin: "1vh",
                            justifySelf: "center",
                        }}
                    >
                        <span
                            style={{
                                backgroundImage: `url(${medalIcon}), `,
                                backgroundSize: "85%",
                                backgroundRepeat: "no-repeat",
                                backgroundPositionY: "calc(55% + 0.5vh)", // Center the image
                                backgroundPositionX: "calc(17% + 0.5vh)", // Center the image
                                width: "100%",
                                height: "100%",
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center",
                            }}
                        >
                            {rank}
                        </span>
                    </div>
                    <h2 className="best-project-item-title">
                        <a
                            className={"link link-hover"}
                            href={headerUrl}
                            target="_blank"
                            rel="noreferrer"
                        >
                            {projectName}
                        </a>
                    </h2>
                    <p className="best-project-item-owner">
                        <strong>
                            {children}
                        </strong>
                    </p>
                </>
        </div>
    )
}

export default DashboardRankedItem