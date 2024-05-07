
export default function DetailsIconCard({cardId, style, icon, statTitle, statValue, statDesc}) {
    return (
        <>
            <div className="stats shadow bg-base-200" id={cardId}
                 style={style}
            >
                <div className="stat">
                    <div className="stat-figure">
                        <i className={icon}
                           style={{fontSize: "5vh"}}
                        ></i>
                    </div>
                    <div className="stat-title">{statTitle}</div>
                    <div className="stat-value">{statValue}'</div>
                    <div className="stat-desc">{statDesc}</div>
                </div>
            </div>
        </>
    )
}