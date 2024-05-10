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
} from "../../../../utils/api/backendUtils..ts";
import DetailsIconCard from "../../cards/general/DetailsIconCard/DetailsIconCard.tsx";
import {Tracker} from "@tremor/react";
import SimpleDashboardCard from "../../cards/SimpleDashboardCard.tsx";
import TrackerCard from "../../cards/general/TrackerCard.tsx";

const colorsCodeSmells = {
    "MINOR": "#67B279",
    "MAJOR": "#FDD835",
    "CRITICAL": "#FF7F50",
    "BLOCKER": "#FF5252",
    "INFO": "#58BBFB"
}

function DashboardSlideTwo() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    // TOTAL TECH DEBT API CALL
    const [techDebtPerMonth, setTechDebtPerMonth] = useState(0);
    const [totalTechDebt, setTotalTechDebt] = useState(0);
    const [totalTdError, setTotalTdError] = useState(false);
    const [totalTdErrorTitle, setTotalTdErrorTitle] = useState("");
    const [totalTdErrorMessage, setTotalTdErrorMessage] = useState("");
    const [totalTdLoading, setTotalTdLoading] = useState(true);

    // TECH DEBT STATS API CALL
    const [averageProjectTechDebt, setAverageProjectTechDebt] = useState(0);
    const [minTechDebt, setMinTechDebt] = useState(0);
    const [maxTechDebt, setMaxTechDebt] = useState(0);
    const [averageTechDebtPerLineOfCode, setAverageTechDebtPerLineOfCode] = useState(0);
    const [tdStatsError, setTdStatsError] = useState(false);
    const [tdStatsErrorTitle, setTdStatsErrorTitle] = useState("");
    const [tdStatsErrorMessage, setTdStatsErrorMessage] = useState("");
    const [tdStatsLoading, setTdStatsLoading] = useState(true);

    // CODE SMELL DISTRIBUTION API CALL
    const [codeSmellDistribution, setCodeSmellDistribution] = useState([]);
    const [chartData, setChartData] = useState([]);
    const [colors, setColors] = useState(colorsCodeSmells);
    const [totalCodeSmells, setTotalCodeSmells] = useState(0);
    const [codeSmellError, setCodeSmellError] = useState(false);
    const [codeSmellErrorTitle, setCodeSmellErrorTitle] = useState("");
    const [codeSmellErrorMessage, setCodeSmellErrorMessage] = useState("");
    const [codeSmellLoading, setCodeSmellLoading] = useState(true);

    // TRACKER CARD
    const [trackerData, setTrackerData] = useState([]);
    const [codeSmellsPercentage, setCodeSmellsPercentage] = useState([]);

    // Call the API to get the total technical debt
    useEffect(() => {
        getTotalTechnicalDebt(accessToken, setTotalTechDebt, setTechDebtPerMonth , setTotalTdLoading, setTotalTdError, setTotalTdErrorTitle, setTotalTdErrorMessage)
        getTechnicalDebtStatistics(accessToken, setTdStatsLoading, setAverageProjectTechDebt, setMinTechDebt, setMaxTechDebt, setAverageTechDebtPerLineOfCode, setTdStatsError, setTdStatsErrorTitle, setTdStatsErrorMessage)
        fetchCodeSmellDistribution(
            accessToken,
            setCodeSmellLoading,
            setChartData,
            setColors,
            setTotalCodeSmells,
            setCodeSmellDistribution,
            setCodeSmellError,
            setCodeSmellErrorTitle,
            setCodeSmellErrorMessage,
            colorsCodeSmells
        )
    }, [accessToken]);

    // Parse the code smell distribution data to be used in the tracker
    useEffect(() => {
        if (codeSmellDistribution.length <= 0) return;

        let trackerData = [];
        // FInd the percentage of each code smell type and make a new map
        const list = codeSmellDistribution.map((distribution) => {
            let percentage = (distribution.count / totalCodeSmells) * 100;
            return {
                name: distribution.severity,
                value: (Math.round(percentage) * 60) / 100
            };
        });

        setCodeSmellsPercentage(list);

        let trackerDATA = []

        list.forEach((item) => {
            for (let i = 0; i < item.value; i++) {
                trackerDATA.push({color: colorsCodeSmells[item.name]})
            }
        })

        setTrackerData(trackerDATA);

    }, [codeSmellDistribution]);

    return (
        <>
            <div className="dashboard-slide" id="slide2">
                <CodeSmellDistributionCard
                    loading={codeSmellLoading}
                    chartData={chartData}
                    chartColors={colors}
                    totalCodeSmells={totalCodeSmells}
                />

                <TotalTechDebtCard
                    totalTechDebt={totalTechDebt}
                    tdPerLine={averageTechDebtPerLineOfCode}
                    tdPerProject={averageProjectTechDebt}
                    loading={totalTdLoading && tdStatsLoading}
                />

                <DetailsIconCard
                    cardId="minDebt"
                    style={{
                        gridArea: "minDebt",
                        overflow: "hidden",
                    }}

                    icon="bi bi-patch-check"
                    statTitle="Min Project Debt"
                    statValue={minTechDebt.toFixed(2)}
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
                    statValue={maxTechDebt.toFixed(2)}
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
                    statTitle="Average Project Debt"
                    statValue={averageProjectTechDebt.toFixed(2)}
                    statDesc="Average Project TD per Line of Code"
                    loading={tdStatsLoading && totalTdLoading}
                />

                <BestPracticesCard />

                <TrackerCard
                    cardId={"tracker"}
                    style={{
                        gridArea: "tracker",
                        overflow: "hidden",
                    }}
                    headerLeftTxt={"Code Smell Tracker"}
                    headerRightTxt={totalCodeSmells + " Total Code Smells"}
                    loading={codeSmellLoading}

                    data={trackerData}
                />

                <FooterCard gridAreaName="footerCard" />
            </div>
        </>
    )
}

export default DashboardSlideTwo