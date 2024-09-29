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
import useAxiosGet from "../../../../../hooks/useAxios.ts";

const colors = [
    /* Language Distribution */
    "#b5da54ff",
    "#91d2fbff",
    "#d998cbff",
    "#5bb9e6",
    "#f7a35cff"
];

const landDistDefaultData = [
    {name: "Java", value: 40, color: colors[0]},
    {name: "Python", value: 30, color: colors[1]},
    {name: "JavaScript", value: 20, color: colors[2]},
    {name: "Other", value: 10, color: colors[3]}
]

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

export default function LanguageDistributionCard({languageDistributionData, languageDistributionLoading, languageDistributionError, languageDistributionErrorMessage}) {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [totalLanguages, setTotalLanguages] = React.useState(0);
    const [languageDistribution, setLanguageDistribution] = React.useState([]);

    // Call the API to get the language distribution data
    useEffect(() => {
        if (!languageDistributionData) return

        setTotalLanguages(languageDistributionData.totalLanguages);
        setLanguageDistribution(languageDistributionData.languageDistribution);
    }, [languageDistributionData]);


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
            name: language.name === 'CXX' ? 'C++' : language.name,
            value: language.linesOfCode,
            color: colors[topFourLanguages.indexOf(language)]
        }
    });

    console.log(data)

    return (
        <>
            {languageDistributionLoading ? (
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
                                // If data is empty, return dummy data
                                data={data.length > 1 ? data : landDistDefaultData}
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