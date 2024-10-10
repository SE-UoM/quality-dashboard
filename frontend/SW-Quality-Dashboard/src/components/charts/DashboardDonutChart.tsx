import {DonutChart} from "@tremor/react";
import React, {useEffect} from "react";
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
                padding: "0.5rem",
                height: "4vh !important",
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
                    {categoryPayload.name}: {formatText(categoryPayload.value, "k")}
                </p>
            </div>
        </div>
    );
};

export default function DashboardDonutChart({data, colors, centerLabel, showLabel, labelStyle}) {
    // Inject the label size to the css
    useEffect(() => {
        const style = document.createElement('style');
        style.innerHTML = labelStyle ? labelStyle : '';

        document.head.appendChild(style);

        return () => {
            document.head.removeChild(style);
        }
    }, []);

    return (
        <>
            <DonutChart
                data={data}
                category="value"
                index="name"
                colors={colors}
                customTooltip={customTooltip}
                variant="donut"
                // label={centerLabel}
                showLabel={showLabel}
                showAnimation={true}
                className=""
                valueFormatter={(value) => formatText(value, "k")}
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