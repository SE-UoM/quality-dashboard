import './DashboardSlideTwo.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/general/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import CodeSmellDistributionCard from "../../cards/screen2/CodeSmellDistributionCard/CodeSmellDistributionCard.tsx";
import TotalTechDebtCard from "../../cards/screen2/TotalTechDebtCard/TotalTechDebtCard.tsx";
import TechDebtStatsCard from "../../cards/screen2/TechDebtStatsCard/TechDebtStatsCard.tsx";
import BestPracticesCard from "../../cards/screen2/BestPracticesCard/BestPracticesCard.tsx";
import getTotalTechnicalDebt, {getTechnicalDebtStatistics} from "../../../../utils/backend/backendUtils..ts";

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


    // Call the API to get the total technical debt
    useEffect(() => {
        getTotalTechnicalDebt(accessToken, setTotalTechDebt, setTechDebtPerMonth , setTotalTdLoading, setTotalTdError, setTotalTdErrorTitle, setTotalTdErrorMessage)
        getTechnicalDebtStatistics(accessToken, setTdStatsLoading, setAverageProjectTechDebt, setMinTechDebt, setMaxTechDebt, setAverageTechDebtPerLineOfCode, setTdStatsError, setTdStatsErrorTitle, setTdStatsErrorMessage)
    }, [accessToken]);

    return (
        <>
            <div className="dashboard-slide" id="slide2">
                <CodeSmellDistributionCard />

                <TotalTechDebtCard
                    totalTechDebt={totalTechDebt}
                    tdPerLine={averageTechDebtPerLineOfCode}
                    tdPerProject={averageProjectTechDebt}
                    loading={totalTdLoading && tdStatsLoading}
                />

                <div className="stats shadow bg-base-200" id={"minDebt"}
                     style={{
                         gridArea: "minDebt",
                         overflow: "hidden",
                     }}
                >
                    <div className="stat">
                        <div className="stat-figure">
                            <i className="bi bi-patch-check"
                                style={{fontSize: "5vh"}}
                            ></i>
                        </div>
                        <div className="stat-title">Min Project Debt</div>
                        <div className="stat-value">{minTechDebt.toFixed(2)}'</div>
                        <div className="stat-desc">
                            Minimum Project TD per Line of Code
                        </div>
                    </div>

                </div>

                <div className="stats shadow bg-base-200" id={"maxDebt"}
                     style={{
                         gridArea: "maxDebt",
                         overflow: "hidden",
                     }}
                >
                    <div className="stat">
                        <div className="stat-figure">
                            <i className="bi bi-patch-exclamation" style={{fontSize: "5vh"}}> </i>
                        </div>
                        <div className="stat-title">Max Project Debt</div>
                        <div className="stat-value">{maxTechDebt.toFixed(2)}'</div>
                        <div className="stat-desc">
                            Maximum Project TD per Line of Code
                        </div>
                    </div>
                </div>


                <div className="stats shadow bg-base-200"
                     style={{
                         gridArea: "techDebtperMonth",
                         overflow: "hidden",
                     }}
                >
                    <div className="stat">
                        <div className="stat-figure">
                            <i className="bi bi-cash-coin" style={{fontSize: "5vh"}}></i>
                        </div>
                        <div className="stat-title">Tech Debt per Month</div>
                        <div className="stat-value">
                            &euro;{techDebtPerMonth.toFixed(2)}
                        </div>
                        <div className="stat-desc">
                            Organization TD per Month Cost
                        </div>
                    </div>

                </div>

                {/*<TechDebtStatsCard />*/}
                <BestPracticesCard />
                <FooterCard gridAreaName="footerCard" />
            </div>
        </>
    )
}

export default DashboardSlideTwo