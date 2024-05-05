import './TechDebtStatsCard.css'
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiRoutes from '../../../../../assets/data/api_urls.json';
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function TechDebtStatsCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(true);

    const [averageProjectTechDebt, setAverageProjectTechDebt] = useState(0);
    const [minTechDebt, setMinTechDebt] = useState(0);
    const [maxTechDebt, setMaxTechDebt] = useState(0);
    const [averageTechDebtPerLineOfCode, setAverageTechDebtPerLineOfCode] = useState(0);

    // Call the API to get the technical debt statistics
    useEffect(() => {
        let url = baseApiUrl + apiRoutes.routes.dashboard.getTdStats;

        // get the user organization id from the access token
        let orgId = jwtDecode(accessToken).organizationId;

        // Replace :orgId with the organization ID
        url = url.replace(":orgId", orgId);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.get(url, { headers: headers })
            .then(response => {
                console.log(response.data);
                let data = response.data;

                let avgTD = data.avgTechDebt || 0;
                let minTD = data.minProjectTechDebtPerLOC || 0;
                let maxTD = data.maxProjectTechDebtPerLOC || 0;
                let avgTDPerLineOfCode = data.avgTechDebtPerLOC || 0;

                // Wait half a second before setting the state
                setTimeout(() => {
                    setLoading(false);
                }, 1000);

                setAverageProjectTechDebt(avgTD);
                setMinTechDebt(minTD);
                setMaxTechDebt(maxTD);
                setAverageTechDebtPerLineOfCode(avgTDPerLineOfCode);
            })
            .catch(error => {
                console.warn("Error fetching technical debt statistics: ", error);
                setError(true);
                setErrorTitle("Error fetching technical debt statistics");
                setErrorMessage("An error occurred while fetching the technical debt statistics of the organization. Please try again later.");
            });
    }, []);

    return (
        <>
                {loading ? (
                    <>
                        <SimpleDashboardCard
                            className="skeleton"
                            id={"techDebtStats"}
                            style={{height: "100%"}}
                        />
                    </>
                    ):
                    <>
                        <SimpleDashboardCard id="techDebtStats">
                        <h3>
                            <i className={"bi bi-bar-chart"}> </i>
                            Technical Debt Statistics
                        </h3>

                        <section className="td-stats-container">
                            <p className={"td-stat-item"}>
                                <strong>
                                    <i className="bi bi-stopwatch"> </i>
                                    Average Debt:
                                </strong>
                                {" " + parseFloat(averageProjectTechDebt.toFixed(2))} '
                            </p>

                            <p className={"td-stat-item"}>
                                <strong>
                                    <i className="bi bi-piggy-bank"> </i>
                                    Max Debt per Line:
                                </strong>
                                {" " +  parseFloat(maxTechDebt.toFixed(2))} <i className={"bi bi-currency-euro"}> </i>
                            </p>

                            <p className={"td-stat-item"}>
                                <strong>
                                    <i className="bi bi-stopwatch"> </i>
                                    Debt per Line:
                                </strong>
                                {" " + parseFloat(averageTechDebtPerLineOfCode.toFixed(2))} '
                            </p>

                            <p className={"td-stat-item"}>
                                <strong>
                                    <i className="bi bi-piggy-bank"> </i>
                                    Min Debt per Line:
                                </strong>
                                {" " + parseFloat(minTechDebt.toFixed(2))} <i className={"bi bi-currency-euro"}> </i>
                            </p>
                        </section>
                        </SimpleDashboardCard>
                    </>
            }
        </>
    );
}

export default TechDebtStatsCard;