import './DashboardSlideFive.css';
import '../DashboardSlideStyle.css';
import FooterCard from "../../cards/general/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import { formatText } from "../../../../utils/textUtils.ts";
import SimpleDashboardCard from "../../cards/SimpleDashboardCard.tsx";
import DashboardDonutChart from "../../../charts/DashboardDonutChart.tsx";
import React, { useEffect } from "react";
import { BarList, CategoryBar, LineChart } from "@tremor/react";
import { VscIssues } from "react-icons/vsc";
import IconCard from "../../cards/screen1/IconCard/IconCard.tsx";
import CommitsQualityPie from "../../cards/CommitsQualityPie/CommitsQualityPie.tsx";
import useAxiosGet from "../../../../hooks/useAxios.ts";
import apiUrls from "../../../../assets/data/api_urls.json";
import { jwtDecode } from "jwt-decode";
import CommitsActivity from "../../cards/CommitsActivity.tsx";
import { TbChartRadar } from "react-icons/tb";
import { TiRadar } from "react-icons/ti";
import CommitHeatmap from "../../../charts/CommitHeatmap/CommitHeatmap.tsx";
import IssuesBar from "../../cards/IssuesBar.tsx";
import HotspotsRadar from "../../cards/HotspotsRadar.tsx";
import CoverageCard from "../../cards/CoverageCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function DashboardSlideFive({ orgID }) {
    const { data: hotspotsData, loading: hotspotsLoading } =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.hotspotsDistribution.replace(":organizationId", orgID), "");

    const [hotspotsQualityData, setHotspotsQualityData] = React.useState(null);
    const [totalHotspots, setTotalHotspots] = React.useState(0);

    const { data: coverageData, loading: coverageLoading } =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.coverage.replace(":organizationId", orgID), "");

    const { data: refactoringsData, loading: refactoringsLoading } =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.refactorings.replace(":organizationId", orgID), "");

    const { data: dependenciesData, loading: dependenciesLoading } =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.dependencies.replace(":organizationId", orgID), "");

    React.useEffect(() => {
        if (hotspotsData) {
            let normal = hotspotsData.normalPriorityHotspots || 0;
            let low = hotspotsData.lowPriorityHotspots || 0;
            let high = hotspotsData.highPriorityHotspots || 0;
            let medium = hotspotsData.mediumPriorityHotspots || 0;
            let unknown = hotspotsData.unknownPriorityHotspots || 0;

            let total = normal + low + high + medium + unknown;

            const formattedData = [high, medium, normal, unknown, low];
            const series = [{ name: 'Hotspots Priority', data: formattedData }];

            setHotspotsQualityData(series);
            setTotalHotspots(total);
        }
    }, [hotspotsData]);

    // Create the stats array dynamically based on the data fetched
    const coverageStats = [
        {
            icon: <i className={"bi bi-code-slash"} style={{ fontSize: '6vh' }}></i>,
            title: "Statements",
            value: coverageData?.totalStmts ? formatText(coverageData.totalStmts, 'k') : 0, // Safe access with optional chaining
            color: "secondary",
            desc: "*for Python files"
        },
        {
            icon: <i className={"bi bi-shield-check"} style={{ fontSize: '6vh' }}></i>,
            title: "Covered",
            value: coverageData?.totalCoverage ? formatText(coverageData.totalCoverage, 'k') : 0, // Safe access with optional chaining
            color: "primary",
            desc: "*for Python files"
        },
        {
            icon: <i className={"bi bi-shield-x"} style={{ fontSize: '6vh' }}></i>,
            title: "Missed",
            value: coverageData?.totalMiss ? formatText(coverageData.totalMiss, 'k') : 0, // Safe access with optional chaining
            color: "success",
            desc: "*for Python files"
        },
        {
            icon: <i className={"bi bi-filetype-py"} style={{ fontSize: '6vh' }}></i>,
            title: "Dependencies",
            value: dependenciesData?.totalDependencies ? formatText(dependenciesData.totalDependencies, 'k') : 0, // Safe access with optional chaining
            color: "accent",
            desc: "*for Python files"
        }
    ];

    const hotspotsCardData = [
        {
            icon: <i className={"bi bi-exclamation-octagon text-warning"} style={{ fontSize: '6vh' }}></i>,
            title: "Hotspot Files",
            value: formatText(totalHotspots, "k"),
            color: "warning",
            desc: "by CodeInspector"
        }
    ];

    const refactoringsCardData = [
        {
            icon: <i className={"bi bi-wrench-adjustable-circle text-success"} style={{ fontSize: '6vh' }}></i>,
            title: "Refactorings",
            value: formatText(refactoringsData ? refactoringsData.totalRefactorings : 0, "k"),
            color: "success",
            desc: "by RefactoringMiner"
        },
        {
            icon: <i className={"bi bi-boxes text-info"} style={{ fontSize: '6vh' }}></i>,
            title: "Refactoring Types",
            value: formatText(refactoringsData ? refactoringsData.totalRefactoringTypes : 0, "k"),
            color: "success",
            desc: "by RefactoringMiner"
        }
    ]

    return (
        <>
            <div className="dashboard-slide" id="slide5">
                <CommitsQualityPie orgID={orgID} gridArea='commitQualityDistribution' />
                <HotspotsRadar
                    gridArea='radarChart'
                    data={hotspotsData ? hotspotsQualityData : null}
                    loading={hotspotsLoading}
                />

                <CoverageCard
                    gridArea="coverageCard"
                    stats={coverageStats}
                    loading={coverageLoading}
                />

                <CoverageCard
                    gridArea="hotspotsTotal"
                    stats={hotspotsCardData}
                    loading={hotspotsLoading || refactoringsLoading || dependenciesLoading}
                />

                <CoverageCard
                    gridArea="refactoringsTotal"
                    stats={refactoringsCardData}
                    loading={refactoringsLoading}
                />

                <CommitHeatmap gridArea='commitHeatmap' orgID={orgID} />
                <CommitsActivity gridArea='commitsTimeline' orgID={orgID} />
                <IssuesBar gridArea='issues' orgID={orgID} />

                <FooterCard gridAreaName="footerCard" />
            </div>
        </>
    );
}

export default DashboardSlideFive;
