import './BestPracticesCard.css'
import {useEffect, useState} from "react";
import apiRoutes from '../../../../assets/data/api_urls.json';
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import axios from "axios";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function BestPracticesCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const [bestPracticeTitle, setBestPracticeTitle] = useState("Eliminate Magic Numbers");
    const [bestPracticeDescription, setBestPracticeDescription] = useState("Magic numbers are hard-coded values that are used in the code without any explanation. They make the code difficult to read and maintain. Make sure to replace magic numbers with named constants.");

    useEffect(() => {
        // Every 20 seconds, call the API to get a random best practice
        const intervalId = setInterval(() => {
            callApi();
        }, 20000); // Call the API every 20 seconds

        // Clear the interval on component unmount to avoid memory leaks
        return () => clearInterval(intervalId);

    }, []);

    function callApi() {
        let url = baseApiUrl + apiRoutes.routes.dashboard.randomBestPractice;
        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.get(url, {headers: headers})
            .then((response) => {
                let data = response.data;
                setBestPracticeTitle(data.title);
                setBestPracticeDescription(data.explanation);
            })
            .catch((error) => {
                console.log(error);
                setError(true);
                setErrorTitle("Error");
                setErrorMessage("An error occurred while trying to get the best practices.");
            });
    }

    return (
        <div className="dashboard-card"
             id="bestPractices"
             style={{gridArea: "bestPractices"}}
        >
            <h3>
                <i className="bi bi-stars"> </i>
                Best Practices
            </h3>

            <div className="best-practices-content">
                <h4>{bestPracticeTitle}</h4>
                <p>
                    {bestPracticeDescription}
                </p>
            </div>
        </div>
    )
}

export default BestPracticesCard;
