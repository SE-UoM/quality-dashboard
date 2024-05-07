import './ItemActivityCard.css'
import {Image} from "react-bootstrap";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

function ItemActivityCard({cardTitle, cardTitleUrl, cardTitleIcon, cardImage, cardIcon, countTitle, count, countCaption, gridArea, loading}) {
    return (
        <>
            {loading ? (
                <SimpleDashboardCard
                    id={"itemActivity"}
                    style={{gridArea: gridArea}}
                    className={"skeleton"}
                />
            ) : (
                <SimpleDashboardCard
                    id={"itemActivity"}
                    style={{gridArea: gridArea}}
                >
                    <h3 className="header">
                        <i className={cardTitleIcon}> </i>
                        {cardTitle}
                    </h3>

                    <div className="card-content">
                        <Image src={cardImage} className="card-image" roundedCircle/>

                        <div className="card-content-details">
                            <h3 className={"name"}>
                                <a
                                    href={cardTitleUrl}
                                    rel={"noreferrer"}
                                    target={"_blank"}
                                >
                                    {countTitle}
                                </a>
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
                </SimpleDashboardCard>
            )}
        </>
    );
}

export default ItemActivityCard;