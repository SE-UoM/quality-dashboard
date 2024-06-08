import './DashboardSlideTwo.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/general/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import CodeSmellDistributionCard from "../../cards/screen2/CodeSmellDistributionCard/CodeSmellDistributionCard.tsx";
import TotalTechDebtCard from "../../cards/screen2/TotalTechDebtCard/TotalTechDebtCard.tsx";
import TechDebtStatsCard from "../../cards/screen2/TechDebtStatsCard/TechDebtStatsCard.tsx";
import BestPracticesCard from "../../cards/screen2/BestPracticesCard/BestPracticesCard.tsx";
import getTotalTechnicalDebt, {
    fetchCodeSmellDistribution,
    getTechnicalDebtStatistics
} from "../../../../utils/api/backendUtils.ts";
import DetailsIconCard from "../../cards/general/DetailsIconCard/DetailsIconCard.tsx";
import {Tracker} from "@tremor/react";
import SimpleDashboardCard from "../../cards/SimpleDashboardCard.tsx";
import TrackerCard from "../../cards/general/TrackerCard.tsx";
import useAxios from "../../../../hooks/useAxios.ts";
import apiUrls from "../../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";

const colorsCodeSmells = {
    "MINOR": "#67B279",
    "MAJOR": "#FDD835",
    "CRITICAL": "#FF7F50",
    "BLOCKER": "#FF5252",
    "INFO": "#58BBFB"
}

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideTwo() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [codeSmellChartData, setCodeSmellChartData] = useState([]);
    const [codeSmellChartColors, setCodeSmellChartColors] = useState([]);
    const [trackerData, setTrackerData] = useState([]);

    const {data: totalTechDebtData, error: totalTdError, loading: totalTdLoading, errorMessage: totalTdErrorMsg} =
        useAxios(baseApiUrl + apiUrls.routes.dashboard.getTotalTD.replace(":organizationId", jwtDecode(accessToken).organizationId), accessToken);

    const {data: techDebtStatsData, error: tdStatsError, loading: tdStatsLoading, errorMessage: tdStatsErrorMsg} =
        useAxios(baseApiUrl + apiUrls.routes.dashboard.getTdStats.replace(":organizationId", jwtDecode(accessToken).organizationId), accessToken);

    const {data: codeSmellData, error: codeSmellError, loading: codeSmellLoading, errorMessage: codeSmellErrorMsg} =
        useAxios(baseApiUrl + apiUrls.routes.dashboard.codeSmellDistribution.replace(":organizationId", jwtDecode(accessToken).organizationId), accessToken);

    const {data: bestPracticesData, error: bestPracticesError, loading: bestPracticesLoading, errorMessage: bestPracticesErrorMsg} =
        useAxios(baseApiUrl + apiUrls.routes.dashboard.bestPractices.replace(":organizationId", jwtDecode(accessToken).organizationId), accessToken);

    // ------ CODE SMELL DISTRIBUTION CHART DATA SETUP ------
    useEffect(() => {
        if (!codeSmellData) return;

        setupChartDataAndColors();
        setupTrackerData();
    }, [codeSmellData]);

    function setupChartDataAndColors() {
        const chartData = codeSmellData.codeSmellsDistribution.map(
            (item) => {
                return {
                    name: item.severity,
                    value: item.count,
                    color: colorsCodeSmells[item.severity]
                }
            }
        );
        const colorArray = chartData.map(item => colorsCodeSmells[item.name]);

        setCodeSmellChartData(chartData);
        setCodeSmellChartColors(colorArray);
    }

    function setupTrackerData() {
        let langDistr = codeSmellData.codeSmellsDistribution;
        // FInd the percentage of each code smell type and make a new map
        const list = langDistr.map((distribution) => {
            let percentage = (distribution.count / codeSmellData.totalCodeSmells) * 100;

            return {
                name: distribution.severity,
                value: (Math.round(percentage) * 60) / 100
            };
        });

        let trackerDATA = []

        list.forEach((item) => {
            for (let i = 0; i < item.value; i++) {
                trackerDATA.push({color: colorsCodeSmells[item.name]})
            }
        })

        setTrackerData(trackerDATA);
    }

    return (
        <>
            <div className="dashboard-slide" id="slide2">
                {codeSmellData &&
                    <CodeSmellDistributionCard
                        loading={codeSmellLoading}
                        chartData={codeSmellChartData}
                        chartColors={codeSmellChartColors}
                        totalCodeSmells={codeSmellData.totalCodeSmells}
                    />
                }

                {totalTechDebtData && techDebtStatsData &&
                    <TotalTechDebtCard
                        totalTechDebt={totalTechDebtData.totalTechDebtCost}
                        tdPerLine={techDebtStatsData.avgTechDebtPerLOC}
                        tdPerProject={techDebtStatsData.avgTechDebt}
                        loading={totalTdLoading && tdStatsLoading}
                    />
                }

                {techDebtStatsData && totalTechDebtData &&
                    <>
                        <DetailsIconCard
                            cardId="minDebt"
                            style={{
                                gridArea: "minDebt",
                                overflow: "hidden",
                            }}

                            icon="bi bi-patch-check"
                            statTitle="Min Project Debt"
                            statValue={techDebtStatsData.minProjectTechDebtPerLOC.toFixed(2) + "'"}
                            statDesc="Minimum Project TD per Line of Code"
                            loading={tdStatsLoading && totalTdLoading}
                        />

                        <DetailsIconCard
                            cardId="maxDebt"
                            style={{
                                gridArea: "maxDebt",
                                overflow: "hidden",
                            }}

                            icon="bi bi-patch-exclamation"
                            statTitle="Max Project Debt"
                            statValue={techDebtStatsData.maxProjectTechDebtPerLOC.toFixed(2) + "'"}
                            statDesc="Maximum Project TD per Line of Code"
                            loading={tdStatsLoading && totalTdLoading}
                        />

                        <DetailsIconCard
                            cardId="techDebtperMonth"
                            style={{
                                gridArea: "techDebtperMonth",
                                overflow: "hidden",
                            }}

                            icon="bi bi-cash-stack"
                            statTitle="Org. Debt per Month"
                            statValue={totalTechDebtData.techDebtCostPerMonth.toFixed(2) + "€"}
                            statDesc="Organization Tech Debt per Month"
                            loading={tdStatsLoading && totalTdLoading}
                        />
                    </>
                }

                {bestPracticesData &&
                    <BestPracticesCard
                        bestPracticesData={bestPracticesData}
                        loading={bestPracticesLoading}
                    />
                }

                {codeSmellData &&
                    <TrackerCard
                        cardId={"tracker"}
                        style={{
                            gridArea: "tracker",
                            overflow: "hidden",
                        }}
                        headerLeftTxt={"Code Smell Tracker"}
                        headerRightTxt={codeSmellData.totalCodeSmells + " Total Code Smells"}
                        loading={codeSmellLoading}

                        data={trackerData}
                    />
                }

                <FooterCard gridAreaName="footerCard" />
            </div>
        </>
    )
}

export default DashboardSlideTwo