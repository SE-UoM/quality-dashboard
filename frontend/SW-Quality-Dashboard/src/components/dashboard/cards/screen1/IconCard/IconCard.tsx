import './IconCard.css';
import '../../DashboardCardStyle.css';
import { useState, useEffect } from 'react';
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

function formatHeaderText(headerText) {
    if (!isNaN(headerText) && parseInt(headerText) >= 1000) {
        const num = parseInt(headerText);
        const roundedNum = Math.round(num / 100) / 10;
        return roundedNum + "k";
    }
    return headerText;
}

function IconCard({icon, headerText, caption, gridAreaName, loading}) {
    const formattedHeaderText = formatHeaderText(headerText);


    return (
        <>
            {loading ? (
                <SimpleDashboardCard
                    id="iconCard"
                    className="skeleton"
                    style={{
                        gridArea: gridAreaName,
                        height: "100%",
                    }}
                >

                </SimpleDashboardCard>
            ) : (
                <SimpleDashboardCard
                    className={"dashboard-card  card bg-base-200" }
                    id="iconCard"
                    style={{gridArea: gridAreaName}}
                >
                    {/*<img src={icon} className="icon-card-icon"/>*/}
                    <i className={icon} style={{
                        fontSize: "9vh",
                        opacity: 0.5
                    }}></i>
                    <div className="icon-card-header">
                        <h2 className="text-base-content">{formattedHeaderText}</h2>
                        <h3 className="text-base-content">{caption}</h3>
                    </div>
                </SimpleDashboardCard>
            )}
        </>
    )
}

export default IconCard;
