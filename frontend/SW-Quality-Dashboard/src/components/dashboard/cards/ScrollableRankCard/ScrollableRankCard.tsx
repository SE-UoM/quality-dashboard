import React from 'react';
import './ScrollableRankCard.css';

function ScrollableRankCard({ title, icon, children, cardId, gridArea }) {
    return (
        <div className="dashboard-card card bg-base-200" id={cardId}
            style={{ gridArea: gridArea }}
        >
            <h3>
                <i className={icon}> </i>
                {title}
            </h3>

            <div className="best-projects-list">
                {children}
            </div>
        </div>
    );
}

export default ScrollableRankCard;
