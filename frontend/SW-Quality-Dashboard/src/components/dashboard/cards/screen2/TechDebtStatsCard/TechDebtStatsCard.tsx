import './TechDebtStatsCard.css'
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiRoutes from '../../../../../assets/data/api_urls.json';
import {jwtDecode} from "jwt-decode";
import axios from "axios";

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

                let avgTD = data.avgTechDebt;
                let minTD = data.minProjectTechDebt;
                let maxTD = data.maxProjectTechDebt;
                let avgTDPerLineOfCode = data.avgTechDebtPerLOC;

                // Wait half a second before setting the state
                setTimeout(() => {
                    setLoading(false);
                }, 500);

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
            <div className="dashboard-card" id="techDebtStats">
                {loading ? (
                    <>
                        <div className="td-stats-skeleton">
                            <div className="td-stats-skeleton-item-top"> </div>
                            <div className="td-stats-skeleton-item-bottom">
                                <div className="td-stats-skeleton-item"> </div>
                                <div className="td-stats-skeleton-item"> </div>
                            </div>
                        </div>
                    </>
                    ):
                    <>
                        <h3>
                            <i className={"bi bi-bar-chart"}> </i>
                            Technical Debt Statistics
                        </h3>

                        <section className="td-stats-container">
                            <p className={"td-stat-item"}>
                                <strong>
                                    <i className="bi bi-stopwatch"> </i>
                                    Average Project Debt:
                                </strong>
                                {" " + parseFloat(averageProjectTechDebt.toFixed(2))} '
                            </p>

                            <p className={"td-stat-item"}>
                                <strong>
                                    <i className="bi bi-piggy-bank"> </i>
                                    Maximum Debt:
                                </strong>
                                {" " + parseFloat(maxTechDebt.toFixed(2))} <i className={"bi bi-currency-euro"}> </i>
                            </p>

                            <p className={"td-stat-item"}>
                                <strong>
                                    <i className="bi bi-stopwatch"> </i>
                                    Debt per Line of Code:
                                </strong>
                                {" " + parseFloat(averageTechDebtPerLineOfCode.toFixed(2))} '
                            </p>

                            <p className={"td-stat-item"}>
                                <strong>
                                    <i className="bi bi-piggy-bank"> </i>
                                    Minimum Debt:
                                </strong>
                                {" " + parseFloat(minTechDebt.toFixed(2))} <i className={"bi bi-currency-euro"}> </i>
                            </p>
                        </section>
                    </>
            }
            </div>
        </>
    );
}

export default TechDebtStatsCard;