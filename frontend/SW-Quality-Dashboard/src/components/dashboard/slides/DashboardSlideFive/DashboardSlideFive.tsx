import './DashboardSlideFive.css';
import '../DashboardSlideStyle.css';

import FooterCard from "../../cards/general/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";

import {formatText} from "../../../../utils/textUtils.ts";
import SimpleDashboardCard from "../../cards/SimpleDashboardCard.tsx";
import DashboardDonutChart from "../../../charts/DashboardDonutChart.tsx";
import React, {useEffect} from "react";
import {BarList, CategoryBar, LineChart} from "@tremor/react";
import {VscIssues} from "react-icons/vsc";
import IconCard from "../../cards/screen1/IconCard/IconCard.tsx";
import CommitsQualityPie from "../../cards/CommitsQualityPie.tsx";
import useAxiosGet from "../../../../hooks/useAxios.ts";
import apiUrls from "../../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import CommitsActivity from "../../cards/CommitsActivity.tsx";
import {TbChartRadar} from "react-icons/tb";
import {TiRadar} from "react-icons/ti";
import CommitHeatmap from "../../../charts/CommitHeatmap/CommitHeatmap.tsx";
import IssuesBar from "../../cards/IssuesBar.tsx";
import HotspotsRadar from "../../cards/HotspotsRadar.tsx";
import CoverageCard from "../../cards/CoverageCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideFive() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const {data: hotspotsData, loading: hotspotsLoading, error: hotspotsError, errorMessage: hotspotsErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.hotspotsDistribution.replace(":organizationId", jwtDecode(accessToken).organizationId), accessToken);

    const [hotspotsQualityData, setHotspotsQualityData] = React.useState(null);
    const [totalHotspots, setTotalHotspots] = React.useState(0);

    const {data: coverageData, loading: coverageLoading, error: coverageError, errorMessage: coverageErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.coverage.replace(":organizationId", jwtDecode(accessToken).organizationId), accessToken);

    const {data: refactoringsData, loading: refactoringsLoading, error: refactoringsError, errorMessage: refactoringsErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.refactorings.replace(":organizationId", jwtDecode(accessToken).organizationId), accessToken);

    const {data: dependenciesData, loading: dependenciesLoading, error: dependenciesError, errorMessage: dependenciesErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.dependencies.replace(":organizationId", jwtDecode(accessToken).organizationId), accessToken);

    // Effect to transform the hotspots data once hotspotsData is fetched
    React.useEffect(() => {
        if (hotspotsData) {
            let normal = hotspotsData.normalPriorityHotspots;
            let low = hotspotsData.lowPriorityHotspots;
            let high = hotspotsData.highPriorityHotspots;
            let medium = hotspotsData.mediumPriorityHotspots;
            let unknown = hotspotsData.unknownPriorityHotspots;

            let total = normal + low + high + medium + unknown;

            // Transforming the data into the desired structure
            // The format is [HIGH, MEDIUM, NORMAL, UNKNOWN, LOW]
            const formattedData = [
                high,
                medium,
                normal,
                unknown,
                low
            ];

            const series = [{
                name: 'Hotspots Priority',
                data: formattedData,
            }];

            // Set the formatted data to the state
            setHotspotsQualityData(series);
            setTotalHotspots(total);
        }

    }, [hotspotsData]);

    return (
        <>
            <div className="dashboard-slide" id="slide5">
                <CommitsQualityPie gridArea='commitQualityDistribution'/>
                <HotspotsRadar
                    gridArea='radarChart'
                    data={hotspotsData ? hotspotsQualityData : null}
                />

                <CoverageCard
                    gridArea="coverageCard"
                    headA={"Statements"}
                    valueA={coverageData ? formatText(coverageData.totalStmts, 'k') : 0}
                    iconA={<i className={"bi bi-code-slash"} style={{fontSize: '6vh'}}> </i>}

                    headB={"Covered"}
                    valueB={coverageData ? formatText(coverageData.totalCoverage, 'k') : 0}
                    iconB={<i className={"bi bi-shield-check"} style={{fontSize: '6vh'}}> </i>}

                    headC={"Missed"}
                    valueC={coverageData ? formatText(coverageData.totalMiss, 'k') : 0}
                    iconC={<i className={"bi bi-shield-x"} style={{fontSize: '6vh'}}> </i>}
                />

                <CoverageCard
                    gridArea="statCard"

                    headA={"Hotspot Files"}
                    valueA={formatText(totalHotspots, "k")}
                    iconA={<i className={"bi bi-exclamation-octagon text-warning"} style={{fontSize: '6vh'}}> </i>}

                    headB={"Refactorings"}
                    valueB={formatText(refactoringsData ? refactoringsData.totalRefactorings : 0, "k")}
                    iconB={<i className={"bi bi-wrench-adjustable-circle text-success"} style={{fontSize: '6vh'}}> </i>}

                    headC={"Python Deps"}
                    valueC={formatText(dependenciesData ? dependenciesData.totalDependencies : 0, "k")}
                    iconC={<i className={"bi bi-filetype-py text-accent"} style={{fontSize: '6vh'}}> </i>}
                />
                <CommitHeatmap gridArea='commitHeatmap'/>
                <CommitsActivity gridArea='commitsTimeline'/>
                <IssuesBar gridArea='issues'/>

                {/*Make it cover the whole row*/}
                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideFive;
