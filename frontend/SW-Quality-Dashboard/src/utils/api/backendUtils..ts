import axios from "axios";
import {jwtDecode} from "jwt-decode";
import apiUrls from "../../assets/data/api_urls.json";

let baseApiUrl = import.meta.env.VITE_API_BASE_URL;

export default function getTotalTechnicalDebt (accessToken, setTotalTechDebt, setTechDebtPerMonth, setLoading, setError, setErrorTitle, setErrorMessage) {
    let url = baseApiUrl + apiUrls.routes.dashboard.getTotalTD;

    // Extract the organization ID from the access token
    let decodedToken = jwtDecode(accessToken);
    let orgId = decodedToken.organizationId;

    // Replace :orgId with the organization ID
    url = url.replace(":orgId", orgId);
    let headers = {
        'Authorization': 'Bearer ' + accessToken,
        'Content-Type': 'application/json'
    }

    axios.get(url, { headers: headers })
        .then(response => {
            console.info("Total Tech Debt API Data: ", response.data);

            // Wait half a second before setting the state
            setTimeout(() => {
                setLoading(false);
            }, 1000);

            setTotalTechDebt(response.data.totalTechDebtCost);
            setTechDebtPerMonth(response.data.techDebtCostPerMonth);
        })
        .catch(error => {
            console.warn("Error fetching total technical debt data: ", error);
            setError(true);
            setErrorTitle("Error fetching total technical debt data");
            setErrorMessage("An error occurred while fetching the total technical debt data of the organization. Please try again later.");
        });
};

export function getTechnicalDebtStatistics  (accessToken, setLoading, setAverageProjectTechDebt, setMinTechDebt, setMaxTechDebt, setAverageTechDebtPerLineOfCode, setError, setErrorTitle, setErrorMessage) {
    let url = baseApiUrl + apiUrls.routes.dashboard.getTdStats;

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
};

export function fetchCodeSmellDistribution(
    accessToken,
    setLoading,
    setChartData,
    setChartColors,
    setTotalCodeSmells,
    setCodeSmellDistribution,
    setError,
    setErrorTitle,
    setErrorMessage,
    colors
) {
    let url = baseApiUrl + apiUrls.routes.dashboard.codeSmellDistribution;

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
}

export function getTopProjects(accessToken, setLoadingTopProjects, setBestProjects, setError, setErrorTitle, setErrorMessage, loading) {
    // Extract the organization id from the access token
    const organizationId = jwtDecode(accessToken).organizationId;

    let url = baseApiUrl + apiUrls.routes.dashboard.topProjects;

    // Replace the organization id in the URL
    url = url.replace(":organizationId", organizationId);
    let headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    }

    console.log(url)

    axios.get(url, {headers: headers})
        .then((response) => {
            let data = response.data;

            // Sort based on techDebtPerLoc
            data.sort((a, b) => {
                return a.techDebtPerLoc - b.techDebtPerLoc;
            });

            // Wait half a second before setting the state
            setTimeout(() => {
                setLoadingTopProjects(false);
            }, 1000);

            setBestProjects(data);
            console.log("Top Projects", data)
            console.log(loading)
        })
        .catch((error) => {
            setError(true);
            setErrorTitle("Error");
            setErrorMessage(error.response.data.message);
        });
}
