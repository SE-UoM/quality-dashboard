import React from 'react';
import './CommitHeatmap.css'
import CalendarHeatmap from 'react-calendar-heatmap';
import 'react-calendar-heatmap/dist/styles.css';
import SimpleDashboardCard from "../../dashboard/cards/SimpleDashboardCard.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts"; // Default styles for the heatmap
import useAxiosGet from "../../../hooks/useAxios.ts";
import apiUrls from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";

// Helper function to aggregate commits by date
const aggregateCommitsByDate = (data) => {
    const commitCounts = {};

    data.forEach(commit => {
        const date = commit.date.split(' ')[0]; // Extract only the date part
        if (commitCounts[date]) {
            commitCounts[date] += 1; // Increment commit count for the date
        } else {
            commitCounts[date] = 1; // Initialize the count
        }
    });

    console.log(commitCounts);

    // Convert the object to the format required by react-calendar-heatmap
    return Object.keys(commitCounts).map(date => ({
        date,
        count: commitCounts[date],
    }));
};

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

const thisYear = new Date().getFullYear();
// Array of years to display in the dropdown
const years = Array.from({ length: thisYear - 2007 + 1 }, (_, index) => 2007 + index).reverse();

const CommitHeatmap = ({gridArea, orgID}) => {
    const [selectedYear, setSelectedYear] = React.useState(thisYear); // State for selected year
    const [heatmapData, setHeatmapData] = React.useState(null);
    const [totalCommits, setTotalCommits] = React.useState(0);

    const {data: commitsData, loading: commitsLoading, error: commitsError, errorMessage: commitsErrorMessage} = useAxiosGet(
        baseApiUrl + apiUrls.routes.dashboard.commitsByYear
            .replace(":organizationId", orgID)
            .replace(":year", selectedYear), ""
    );

    // Effect to aggregate the commits by date once the commitsData is fetched
    React.useEffect(() => {
        if (commitsData && commitsData.commits) {
            const heatmapData = aggregateCommitsByDate(commitsData.commits);
            setHeatmapData(heatmapData);
            setTotalCommits(commitsData.commits.length);
            console.log(commitsData)
            console.log(heatmapData)
        }
    }, [commitsData]);

    // Function to determine the color intensity based on the number of commits
    const getClassForValue = (value) => {
        if (!value) {
            return 'color-empty';
        }

        const commitCount = value.count;

        // Define dynamic thresholds based on totalCommits
        const thresholds = {
            scale1: totalCommits * 0.05, // 5% of total commits
            scale2: totalCommits * 0.2,  // 20% of total commits
            scale3: totalCommits * 0.5,  // 50% of total commits
        };

        if (commitCount <= thresholds.scale1) {
            return 'color-scale-1';
        } else if (commitCount <= thresholds.scale2) {
            return 'color-scale-2';
        } else if (commitCount <= thresholds.scale3) {
            return 'color-scale-3';
        } else {
            return 'color-scale-4';
        }
    };

    return (
        <>
            {commitsLoading ? (
                <SimpleDashboardCard
                    className="skeleton"
                    id={"commitHeatmap"}
                    style={{height: "100%", gridArea: gridArea}}
                />
                ) : (
                <SimpleDashboardCard
                    className=""
                    id={"commitHeatmap"}
                    style={{height: "100%", gridArea: gridArea, padding: '1em 1em 0 1em'}}
                >
                    <div
                        style={{
                            width: "100%",
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                            marginBottom: '1em'
                        }}
                    >
                        <h2 style={{paddingBottom: '1em'}}><strong>Commit Heatmap ({selectedYear})</strong></h2>

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

                    <div style={{
                        height: '100%',
                        width: '100%',
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center'
                    }}>
                        <CalendarHeatmap
                            startDate={new Date(selectedYear, 0)}
                            endDate={new Date(selectedYear, 11, 31)}
                            values={heatmapData}
                            classForValue={getClassForValue}
                            showWeekdayLabels={true}
                            gutterSize={2}
                        />
                    </div>
                </SimpleDashboardCard>
            )}
        </>
    );
};

export default CommitHeatmap;
