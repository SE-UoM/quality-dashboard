import React from 'react';
import './CodeSmellDistributionCard.css';
import apiRoutes from '../../../../../assets/data/api_urls.json';
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import { jwtDecode } from "jwt-decode";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import DashboardDonutChart from "../../../../charts/DashboardDonutChart.tsx";
import {formatText} from "../../../../../utils/textUtils.ts";

const colors = {
    "MINOR": "#67B279",
    "MAJOR": "#FDD835",
    "CRITICAL": "#FF7F50",
    "BLOCKER": "#FF5252",
    "INFO": "#58BBFB"
}

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function CodeSmellDistributionCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [totalCodeSmells, setTotalCodeSmells] = React.useState(0);
    const [codeSmellDistribution, setCodeSmellDistribution] = React.useState([]);
    const [chartData, setChartData] = React.useState([]);
    const [chartColors, setChartColors] = React.useState([]);

    const [error, setError] = React.useState(false);
    const [errorTitle, setErrorTitle] = React.useState("");
    const [errorMessage, setErrorMessage] = React.useState("");
    const [loading, setLoading] = React.useState(true);

    // Call the API to get the language distribution data
    React.useEffect(() => {
        let url = baseApiUrl + apiRoutes.routes.dashboard.codeSmellDistribution;

        // Get the User Organization from the JWT Token
        let userOrganization = jwtDecode(accessToken).organizationId;

        url = url.replace(":organizationId", userOrganization);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }
        axios.get(url, { headers: headers })
            .then(response => {
                let data = response.data;

                console.info("Code Smell Distribution Data: ", data);

                let totalCodeSmells = response.data.totalCodeSmells;
                let codeSmellDistribution = response.data.codeSmellsDistribution;

                // Map data to format required by the DonutChart component
                const chartData = codeSmellDistribution.map(
                    (item) => {
                        return {
                            name: item.severity,
                            value: item.count,
                            color: colors[item.severity]
                        }

                    }
                );

                const colorArray = chartData.map(item => colors[item.name]);

                setChartData(chartData);
                setChartColors(colorArray);

                // Wait half a second before setting the state to false
                setTimeout(() => {
                    setLoading(false);
                }, 1000);

                setTotalCodeSmells(totalCodeSmells);
                setCodeSmellDistribution(codeSmellDistribution);
            })
            .catch(error => {
                console.warn("Error fetching language distribution data: ", error);
                setError(true);
                setErrorTitle("Error fetching language distribution data");
                setErrorMessage("An error occurred while fetching the language distribution data of the organization. Please try again later.");
            });
    }, [accessToken]);

    console.log(codeSmellDistribution)

    return (
        <>
            {error &&
                <ErrorModal
                    modalTitle={errorTitle}
                    modalAlertMessage={errorMessage}
                />
            }
                {!loading ? (
                    <>
                        <SimpleDashboardCard id="codeSmellDistribution">
                            <div className="language-distribution-container">
                                <h3>
                                    <i className="bi bi-radioactive"> </i>
                                    Code Smell Distribution
                                </h3>

                                <div className="lang-distribution-chart">
                                    <DashboardDonutChart
                                        data={chartData}
                                        colors={chartColors}
                                        centerLabel={formatText(totalCodeSmells, "k")}
                                    />
                                </div>
                            </div>
                        </SimpleDashboardCard>
                    </>
                ) : (
                    <SimpleDashboardCard
                        className="skeleton"
                        id={"codeSmellDistribution"}
                        style={{height: "100%"}}
                    />
                )}

        </>
    )
}

export default CodeSmellDistributionCard;
