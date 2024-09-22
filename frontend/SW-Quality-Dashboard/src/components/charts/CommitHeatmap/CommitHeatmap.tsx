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

const CommitHeatmap = ({gridArea }) => {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [heatmapData, setHeatmapData] = React.useState(null);
    const [totalCommits, setTotalCommits] = React.useState(0);
    const [year, setYear] = React.useState(2021);

    const {data: commitsData, loading: commitsLoading, error: commitsError, errorMessage: commitsErrorMessage} = useAxiosGet(
        baseApiUrl + apiUrls.routes.dashboard.commitsByYear
            .replace(":organizationId", jwtDecode(accessToken).organizationId)
            .replace(":year", year)
        , accessToken);

    // Effect to aggregate the commits by date once the commitsData is fetched
    React.useEffect(() => {
        if (commitsData && commitsData.commits) {
            const heatmapData = aggregateCommitsByDate(commitsData.commits);
            setHeatmapData(heatmapData);
            setTotalCommits(commitsData.commits.length);
        }
    }, [commitsData]);

    // Function to determine the color intensity based on the number of commits
    const getClassForValue = (value) => {
        if (!value) {
            return 'color-empty';
        } else if (value.count >= 1 && value.count <= 10) {
            return 'color-scale-1';
        } else if (value.count >= 10 && value.count <= 50) {
            return 'color-scale-2';
        } else if (value.count >= 50 && value.count <= 100) {
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
                    style={{height: "100%", gridArea: gridArea}}
                >
                    <h2 style={{paddingBottom: '1em'}}>Commit Heatmap ({year})</h2>
                    <div style={{height: '100%', width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                        <CalendarHeatmap
                            startDate={new Date('2021-01-01')}
                            endDate={new Date('2021-12-31')}
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
