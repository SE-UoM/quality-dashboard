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
                    style={{gridArea: gridAreaName}}
                >
                    <div className="icon-card-icon skeleton"></div>
                    <div className="icon-card-header">
                        <h2 className="skeleton"></h2>
                        <h3 className="skeleton"></h3>
                    </div>
                </SimpleDashboardCard>
            ) : (
                <SimpleDashboardCard
                    className={"dashboard-card  card bg-base-200" }
                    id="iconCard"
                    style={{gridArea: gridAreaName}}
                >
                    <img src={icon} className="icon-card-icon"/>
                    <div className="icon-card-header">
                        <h2>{formattedHeaderText}</h2>
                        <h3>{caption}</h3>
                    </div>
                </SimpleDashboardCard>
            )}
        </>
    )
}

export default IconCard;
