import React from "react";
import SimpleDashboardCard from "./SimpleDashboardCard.tsx";

function CoverageCard({ gridArea, stats, loading }) {
    return (
        <>
            {loading ? (
                <SimpleDashboardCard
                    className="skeleton"
                    id={"coverageCard"}
                    style={{ height: "100%", gridArea: gridArea }}
                />
            ) : (
                <div
                    className="stats shadow bg-base-200"
                    id=""
                    style={{
                        gridArea: gridArea,
                        overflow: "hidden",
                        padding: "0",
                    }}
                >
                    {stats.map((stat, index) => (
                        <div className="stat" key={index}>
                            <div className={`stat-figure text-${stat.color}`}>
                                {stat.icon}
                            </div>
                            <div className="stat-title">{stat.title}</div>
                            <div className="stat-value">{stat.value}</div>
                            <div className="stat-desc">
                                {stat.desc}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </>
    );
}

export default CoverageCard;
