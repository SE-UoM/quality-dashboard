import React from 'react';
import {PieChart, useDrawingArea} from '@mui/x-charts';
import './CodeSmellDistributionCard.css';
import apiRoutes from '../../../../../assets/data/api_urls.json';
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import { jwtDecode } from "jwt-decode";
import {styled} from "@mui/material/styles";
import CustomPieChart from "../../../../charts/CustomPieChart.tsx";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

const colors = {
    "MINOR": "#67B279",
    "MAJOR": "#FDD835",
    "CRITICAL": "#FF7F50",
    "BLOCKER": "#FF5252",
    "INFO": "#58BBFB"
}

function formatText(text) {
    let roundedNum;
    if (!isNaN(text) && parseInt(text) > 1000) {
        const num = parseInt(text);
        roundedNum = Math.round(num / 100) / 10;
        return parseInt(roundedNum) + "k";
    }
    return text;
}

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

const StyledText = styled('text')(({ theme }) => ({
    textAnchor: 'middle',
    dominantBaseline: 'central',
    fontWeight: "bold",
    // fontSize: "15vh !important",
    padding: "1em",
    color: "var(--text-primary)"
}));

function PieCenterLabel({ children }: { children: React.ReactNode }) {
    const { width, height, left, top } = useDrawingArea();
    return (
        <StyledText className="lang-count" x={left + width / 2} y={top + height / 2}>
            {children}
        </StyledText>
    );
}
function CodeSmellDistributionCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [totalCodeSmells, setTotalCodeSmells] = React.useState(0);
    const [codeSmellDistribution, setCodeSmellDistribution] = React.useState([]);

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

    // Format data for PieChart
    const pieChartData = codeSmellDistribution.map((item, index) => ({
        key: item.severity,
        value: item.count,
        label: item.severity + " (" + item.count + ")",
        color: colors[item.severity]
    }));

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
                        <h3>
                            <i className="bi bi-radioactive"> </i>
                            Code Smell Distribution
                        </h3>
                        <div className="code-smell-distribution-container">
                            <div className="code-smells-distribution-chart">
                                <CustomPieChart
                                    data={pieChartData}
                                    centerText={totalCodeSmells}
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
