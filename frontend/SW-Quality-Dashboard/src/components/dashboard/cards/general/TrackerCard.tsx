import {Tracker} from "@tremor/react";
import SimpleDashboardCard from "../SimpleDashboardCard.tsx";

export default function TrackerCard({cardId, style, headerLeftTxt, headerRightTxt, data, loading}) {
    return (
        <>
            {loading ? (
                <SimpleDashboardCard
                    id={cardId}
                    style={{
                        gridArea: "tracker",
                    }}
                    className="skeleton"
                />
            ) : (
                <SimpleDashboardCard
                    id={cardId}
                    style={style}
                >
                    <div className="tracker-header"
                         style={{
                             display: "flex",
                             flexDirection: "row",
                             alignItems: "center",
                             justifyContent: "space-between",
                             width: "100%"
                         }}
                    >
                        <h2 className="text card-title"
                            style={{
                                fontSize: "2vh",
                                margin: "0",
                                paddingLeft: "2vh",
                                fontWeight: "bold",
                            }}
                        >
                            {headerLeftTxt}
                        </h2>

                        <p className="text card-title"
                           style={{
                               fontSize: "1.9vh",
                               margin: "0",
                               paddingLeft: "2vh",
                               fontWeight: "normal",
                           }}
                        >
                            {headerRightTxt}
                        </p>
                    </div>
                    <Tracker
                        style={{
                            height: "100%",
                            width: "100%",
                            padding: "2vh"
                        }}
                        data={data}
                    />
                </SimpleDashboardCard>
            )
            }
        </>
    )
}