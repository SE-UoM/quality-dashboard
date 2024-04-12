import './ItemActivityCard.css'

function ItemActivityCard({cardTitle, cardTitleIcon, cardImage, cardIcon, countTitle, count, countCaption, gridArea}) {
    return (
        <div className="dashboard-card"
             id={"itemActivity"}
             style={{gridArea: gridArea}}
        >
            <h3 className="header">
                <i className={cardTitleIcon}> </i>
                {cardTitle}
            </h3>

            <div className="card-content">
                <img src={cardImage} className="card-image" />

                <div className="card-content-details">
                    <h3 className={"name"}>
                        {countTitle}
                    </h3>

                    <div className="card-content-details-caption">
                        <img src={cardIcon} className="card-icon"/>

                        <div className="card-content-details-count">
                            <h4>
                                {count}
                            </h4>

                            <p>
                                {countCaption}
                            </p>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )
}

export default ItemActivityCard