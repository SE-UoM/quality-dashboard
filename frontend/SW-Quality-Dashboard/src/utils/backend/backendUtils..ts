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