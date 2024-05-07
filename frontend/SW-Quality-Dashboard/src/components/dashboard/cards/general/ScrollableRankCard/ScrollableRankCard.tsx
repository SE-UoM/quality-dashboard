import React from 'react';
import './ScrollableRankCard.css';
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

function ScrollableRankCard({ title, icon, children, cardId, gridArea }) {
    return (
        <SimpleDashboardCard id={cardId}
            style={{ gridArea: gridArea, height: "100%" }}
        >
            <h3>
                <i className={icon}> </i>
                {title}
            </h3>

            <div className="best-projects-list">
                {children}
            </div>
        </SimpleDashboardCard>
    );
}

export default ScrollableRankCard;
