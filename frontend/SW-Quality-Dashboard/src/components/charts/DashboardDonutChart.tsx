import {DonutChart} from "@tremor/react";
import React from "react";
import {formatText} from "../../utils/textUtils.ts";

const customTooltip = ({    payload, active, label  }) => {
    if (!active || !payload) return null;

    const categoryPayload = payload?.[0];

    if (!categoryPayload) return null;
    return (
        <div
            className="w-56 card shadow-sm bg-neutral"
            style={{
                display: "grid",
                gridTemplateColumns: "1fr 9fr",
                minHeight: "5vh",
                padding: "0.5rem"
            }}
        >
            <div className="card-line"
                 style={{
                     backgroundColor: categoryPayload.color,
                     minHeight: "4vh",
                     width: "0.25rem",
                     borderRadius: "0.25rem 0 0 0.25rem",
                     marginRight: "0.5rem"
                 }}
            >

            </div>

            <div className="card-body"
                 style={{
                     padding: "0"
                 }}
            >
                <p
                    className={"card-text text-neutral-content"}
                    style={{
                        width: "100%",
                        textAlign: "left"
                    }}
                >
                    {categoryPayload.name}: {formatText(categoryPayload.value, "k")} Lines of Code.
                </p>
            </div>
        </div>
    );
};

export default function DashboardDonutChart({data, colors, centerLabel}) {
    return (
        <>
            <DonutChart
                data={data}
                category="value"
                index="name"
                colors={colors}
                customTooltip={customTooltip}
                variant="donut"
                label={centerLabel}
                showAnimation={true}
                className="h-full"
            />

            <div className="donut-chart-legend">
                <ul>
                    {data.map((item, index) => (
                        <li key={index}
                            style={{
                                display: "flex",
                                justifyContent: "flex-start",
                                alignItems: "center",
                                flexDirection: "row",
                                gap: "1vw",
                                fontSize: "2.8vh",
                            }}
                        >
                                            <span className="legend-color" style={{
                                                backgroundColor: item.color,
                                                width: "3vh",
                                                height: "3vh",
                                                borderRadius: "5vh"
                                            }}> </span>
                            <span className="legend-name">{item.name}</span>
                        </li>
                    ))}
                </ul>
            </div>
        </>
    );
}