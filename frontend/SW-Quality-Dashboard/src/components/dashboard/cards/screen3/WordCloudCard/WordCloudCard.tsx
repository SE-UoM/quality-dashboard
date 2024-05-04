import React, { useState, useEffect } from 'react';
import './WordCloudCard.css';
import WordCloud from "../../../../ui/WordCloud/WordCloud.tsx";
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import apiUrls from "../../../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

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
                
                setWords(response.data);
                console.info(data)
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });

    }, [accessToken]);

    return (
        <div className="dashboard-card card bg-base-200"
             style={{gridArea: "wordcloud"}}
        >
            {error &&
                <ErrorModal
                    modalTitle={errorTitle}
                    modalAlertMessage={errorMessage}
                />
            }

            {words.length > 0 && (
                // Force re-render by changing key prop
                <WordCloud words={words}/>
            )}
        </div>
    );
};

export default WordCloudCard;
