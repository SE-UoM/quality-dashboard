import React, { useState, useEffect } from 'react';
import './WordCloudCard.css';
import WordCloud from "../../../../ui/WordCloud/WordCloud.tsx";
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import apiUrls from "../../../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import ReactWordcloud from "react-wordcloud";
import {data} from "autoprefixer";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

const testWords = [
    {
        text: 'told',
        value: 64,
    },
    {
        text: 'mistake',
        value: 11,
    },
    {
        text: 'thought',
        value: 16,
    },
    {
        text: 'bad',
        value: 17,
    },
]
const WordCloudCard = () => {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(true);

    const [words, setWords] = useState([]);

    const [size, setSize] = useState(0);

    // Call the API to get the word cloud data
    useEffect(() => {
        // Extract the organization id from the access token
        const organizationId = jwtDecode(accessToken).organizationId;

        let url = baseApiUrl + apiUrls.routes.dashboard.languageNames

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

                // Wait half a second before setting the state
                setTimeout(() => {
                    setLoading(false);
                }, 3000);

                let respData = response.data;

                let finalData = respData.map((word) => {
                    if (word.toUpperCase() === "CXX") word = "C++";

                    return {
                        text: word,
                        value: Math.floor(Math.random() * 100) + 1
                    }
                });

                setWords(finalData);
                console.info(data)
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });

    }, [accessToken]);

    return (
        <>
            {loading ? (
                <SimpleDashboardCard
                    id="wordcloud-card"
                    className="skeleton"
                    style={{
                        gridArea: "wordcloud",
                        height: "100%",
                    }}
                >
                </SimpleDashboardCard>
                    ) : (
                    <SimpleDashboardCard id="wordcloud-card"
                                         style={{
                                             gridArea: "wordcloud",
                                             paddingTop: "10vh",
                                             height: "100%",
                    }}
                    >
                        {error &&
                            <ErrorModal
                                modalTitle={errorTitle}
                                modalAlertMessage={errorMessage}
                            />
                        }

                        {words.length > 0 && (
                            // Force re-render by changing key prop
                            <ReactWordcloud
                                words={words}
                                minSize={[300, 300]}
                                size={[560, 300]}
                                options={{
                                    scale: "sqrt",
                                    spiral: "archimedean",
                                    fontSizes: [60, 150],
                                    rotations: 0,
                                    enableTooltip: false,
                                    deterministic: true,
                                }}
                            />
                        )}
                    </SimpleDashboardCard>
                    )}
        </>
    );
};

export default WordCloudCard;
