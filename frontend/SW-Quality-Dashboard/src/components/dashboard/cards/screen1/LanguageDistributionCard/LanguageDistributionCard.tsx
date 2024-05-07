import React, {useEffect} from 'react';
import './LanguageDistributionCard.css';
import apiRoutes from '../../../../../assets/data/api_urls.json';
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import DashboardDonutChart from "../../../../charts/DashboardDonutChart.tsx";
import {formatText} from "../../../../../utils/textUtils.ts";

const colors = [
    /* Language Distribution */
    "#b5da54ff",
    "#91d2fbff",
    "#d998cbff",
    "#5bb9e6",
    "#f7a35cff"
];

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

export default function LanguageDistributionCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [totalLanguages, setTotalLanguages] = React.useState(0);
    const [languageDistribution, setLanguageDistribution] = React.useState([]);

    const [error, setError] = React.useState(false);
    const [errorTitle, setErrorTitle] = React.useState("");
    const [errorMessage, setErrorMessage] = React.useState("");
    const [loading, setLoading] = React.useState(true);


    // Call the API to get the language distribution data
    useEffect(() => {
        let url = baseApiUrl + apiRoutes.routes.dashboard.languageDistribution;

        // Decode the access token to get the organization ID
        let organizationId = jwtDecode(accessToken).organizationId;

        // Replace ":organizationId" with the actual organization ID
        url = url.replace(":organizationId", organizationId);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }
        axios.get(url, { headers: headers })
            .then(response => {
                console.info("Language distribution data: ", response.data)

                // Wait half a second before setting the state to false
                setTimeout(() => {
                    setLoading(false);
                }, 1000);

                setTotalLanguages(response.data.totalLanguages);
                setLanguageDistribution(response.data.languageDistribution);
            })
            .catch(error => {
                console.warn("Error fetching language distribution data: ", error);
                setError(true);
                setErrorTitle("Error fetching language distribution data");
                setErrorMessage("An error occurred while fetching the language distribution data of the organization. Please try again later.");
            });
    }, [accessToken]);


    // Sort the language distribution by linesOfCode in descending order
    const sortedLanguages = languageDistribution.sort((a, b) => b.linesOfCode - a.linesOfCode);

    // Extract the first four languages and sum linesOfCode for the rest
    let topFourLanguages = sortedLanguages.slice(0, 4);
    let otherLanguagesTotalLines = sortedLanguages.slice(4).reduce((total, language) => total + language.linesOfCode, 0);

    // Add an "Other" label with the total lines of code for other languages
    topFourLanguages.push({
        id: 'other',
        name: 'Other',
        imageUrl: null,
        linesOfCode: otherLanguagesTotalLines
    });

    // Map data to format required by the DonutChart component
    const data = topFourLanguages.map(language => {
        return {
            name: language.name,
            value: language.linesOfCode,
            color: colors[topFourLanguages.indexOf(language)]
        }
    });

    return (
        <>
            {error &&
                <ErrorModal
                    modalTitle={errorTitle}
                    modalAlertMessage={errorMessage}
                />
            }

            {loading ? (
                <SimpleDashboardCard
                    id="languageDistribution"
                    className="skeleton"
                    style={{
                        gridArea: "languageDistribution",
                        height: "100%",
                    }}
                >
                </SimpleDashboardCard>
            ) : (
                <>

                <SimpleDashboardCard id="languageDistribution">
                    <div className="language-distribution-container">
                        <h3>
                            <i className="bi bi-pie-chart-fill"> </i>
                            Language Distribution
                        </h3>

                        <div className="lang-distribution-chart">
                            <DashboardDonutChart
                                data={data}
                                colors={colors}
                                centerLabel={formatText(totalLanguages, "k")}
                            />
                        </div>
                    </div>
                </SimpleDashboardCard>
                </>
            )}
        </>
    )
}