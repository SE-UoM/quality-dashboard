import React from 'react';
import './ScrollableRankCard.css';
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

export default function ScrollableRankCard({ title, icon, children, cardId, gridArea, setAutoScroll, autoscroll }) {

    return (
        <SimpleDashboardCard id={cardId}
            style={{
                gridArea: gridArea,
                height: "100%",
                width: "100%",
                padding: "0",
            }}
        >
            <h3
                style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                    fontWeight: "bold",
                    fontSize: "3vh",
                    width: "100%",
                    textAlign: "center",
                    padding: "1.5vh",
                    margin: "0",
                    textWrap: "nowrap",
                }}
            >
                <div>
                    <i className={icon}>&nbsp;</i>
                    {title}
                </div>

                <button
                    onClick={() => {
                        setAutoScroll(!autoscroll)
                    }}
                    className="tooltip tooltip-left btn"
                    data-tip={"Auto Scroll (" + (autoscroll ? "On" : "Off") + ")"}
                    disabled={true}
                >
                    <i className={autoscroll ? "bi bi-pause-circle-fill" : "bi bi-play-circle-fill"}> </i>
                </button>
            </h3>

            <div className="best-projects-list">
                {children}
            </div>
        </SimpleDashboardCard>
    );
}