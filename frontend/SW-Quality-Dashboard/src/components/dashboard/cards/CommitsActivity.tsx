import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import useAxiosGet from "../../../hooks/useAxios.ts";
import apiUrls from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import React, {useEffect, useState} from "react";
import SimpleDashboardCard from "./SimpleDashboardCard.tsx";
import {LineChart} from "@tremor/react";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL


const thisYear = new Date().getFullYear();

// Array of years to display in the dropdown
const years = Array.from({ length: thisYear - 2007 + 1 }, (_, index) => 2007 + index).reverse();

function CommitsActivity({gridArea, orgID}) {
    const [selectedYear, setSelectedYear] = useState(thisYear); // State for selected year

    const {data: commitsActivity, loading: commitsActivityLoading, error: commitsActivityError, errorMessage: commitsActivityErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.commitsByYear.replace(":organizationId", orgID).replace(":year", selectedYear), "");

    // State to manage chart data
    const [chartdata, setChartdata] = useState(
        Array(12).fill(0).map((_, index) => ({
            month: new Date(0, index).toLocaleString('en', { month: 'long' }).toUpperCase(),
            commits: 0
        }))
    );


    // Effect to transform the commitsActivity data once commitsActivity is fetched
    useEffect(() => {
        // Clone the initial state to avoid direct mutation
        const updatedChartData = [...chartdata];

        if (commitsActivity && commitsActivity.commits) {
            commitsActivity.commits.forEach(commit => {
                const commitDate = new Date(commit.date);
                const monthIndex = commitDate.getMonth(); // getMonth() returns 0 for January, 11 for December
                updatedChartData[monthIndex].commits += 1; // Increment the commit count for the corresponding month

                setChartdata(updatedChartData);
            });
        }
    }, [commitsActivity]);

    return (
        <>
            {commitsActivityLoading ? (
                <SimpleDashboardCard
                    className="skeleton"
                    id={"commitsTimeline"}
                    style={{height: "100%", gridArea: gridArea}}
                />
            ) : (
                <SimpleDashboardCard
                    className=""
                    id={"commitsTimeline"}
                    style={{height: "100%", gridArea: "commitsTimeline"}}
                >
                    <div
                        style={{
                            width: "100%",
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                        }}
                    >
                        <h4 style={{width: "100%"}}>
                            <strong>Commits Activity</strong>
                        </h4>

                        <select
                            value={selectedYear}
                            onChange={(e) => setSelectedYear(parseInt(e.target.value))}
                            className="select select-bordered max-w-xs"
                        >
                            {years.map(year => (
                                // <option key={year} value={year}>{year}</option>
                                // Also set the selected year as selected
                                <option key={year} value={year} selected={year === selectedYear}>{year}</option>
                            ))}
                        </select>
                    </div>

                    <LineChart className="h-80" data={chartdata} index="month" categories={["commits"]}
                               valueFormatter={(number: number) => `${Intl.NumberFormat("us").format(number).toString()}`}
                               onValueChange={(v) => console.log(v)}/>
                </SimpleDashboardCard>
            )}
        </>
    )
}

export default CommitsActivity;