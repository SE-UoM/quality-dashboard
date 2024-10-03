import {VscIssues} from "react-icons/vsc";
import {CategoryBar} from "@tremor/react";
import SimpleDashboardCard from "./SimpleDashboardCard.tsx";
import React from "react";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import useAxiosGet from "../../../hooks/useAxios.ts";
import {jwtDecode} from "jwt-decode";
import apiUrls from "../../../assets/data/api_urls.json";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function IssuesBar({gridArea, orgID}) {
    const [totalIssues, setTotalIssues] = React.useState(0);
    const [openIssues, setOpenIssues] = React.useState(0);
    const [closedIssues, setClosedIssues] = React.useState(0);

    const [closedIssuesPercentage, setClosedIssuesPercentage] = React.useState(0);
    const [openIssuesPercentage, setOpenIssuesPercentage] = React.useState(0);

    const {data: issuesData, loading: issuesLoading, error: issuesError, errorMessage: issuesErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.issues.replace(":organizationId", orgID), "");

    // Effect to transform the issues data once issuesData is fetched
    React.useEffect(() => {
        console.log(issuesData);
        if (issuesData) {
            const totalIssues = issuesData.totalIssuesCount;
            const openIssues = issuesData.openIssuesCount;
            const closedIssues = issuesData.closedIssuesCount;

            // we need to calculate the percentage of open and closed issues
            // This should like: Open = 10 (10%), Closed = 90 (90%)
            const openIssuesPercentage = Math.round((openIssues / totalIssues) * 100);
            const closedIssuesPercentage = 100 - openIssuesPercentage;

            setTotalIssues(totalIssues);
            setOpenIssues(openIssues);
            setClosedIssues(closedIssues);

            setOpenIssuesPercentage(openIssuesPercentage);
            setClosedIssuesPercentage(closedIssuesPercentage);
        }
    }, [issuesData]);

    return (
    <>
        {issuesLoading ? (
            <SimpleDashboardCard
                className="skeleton"
                id={"isues"}
                style={{height: "100%", gridArea: gridArea}}
            />
            ) : (
            <SimpleDashboardCard
                className=""
                id={"isues"}
                style={{
                    height: "100%",
                    gridArea: {gridArea},
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    padding: "0",
                    justifyContent: "center"
            }}
            >
                <div className="issues-container" style={{width: "100%"}}>
                    <div style={{padding: '1em 1em 0 1em'}}>
                    <h4 style={{
                        width: "100%",
                        paddingBottom: "1em",
                        display: "flex",
                        flexDirection: "row",
                        alignItems: "center",
                        gap: "0.5em"
                    }}>
                        <VscIssues style={{height: "100%", justifySelf: "center"}} /> Issues
                    </h4>
                    <CategoryBar
                        values={[openIssuesPercentage, closedIssuesPercentage]}
                        showLabels={false}
                        colors={["#6366f1", "#10b981"]}
                        style={{width: "100%", borderRadius: "10vh"}}
                    />
                    <p style={{width: "100%", paddingTop: "0.5em"}}>
                        <strong>{openIssues}</strong> Open Issues
                    </p>
                    </div>

                    <div
                        style={{
                            width: "100%",
                            height: "50px",
                            marginTop: "0.5em",
                            borderTop: "1px solid #757474",
                            display: "grid",
                            gridTemplateColumns: "1fr 1fr",
                        }}
                    >
                        <div style={{
                            borderRight: "1px solid #757474",
                            display: "flex",
                            flexDirection: "column",
                            justifyContent: "center",
                            alignItems: "center"
                        }}>
                            <h5>
                                <i className={"bi bi-exclamation-circle"} style={{color: "#6366f1"}}> </i>
                                <strong>{openIssues}</strong>
                            </h5>
                            Open Issues
                        </div>

                        <div style={{
                            display: "flex",
                            flexDirection: "column",
                            justifyContent: "center",
                            alignItems: "center"
                        }}>
                            <h5>
                                <i className={"bi bi-check-circle"} style={{color: "#10b981"}}> </i>
                                <strong>{closedIssues}</strong>
                            </h5>
                            Closed Issues
                        </div>
                    </div>
                </div>
            </SimpleDashboardCard>
        )}

    </>
    );
}

export default IssuesBar;