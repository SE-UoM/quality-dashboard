import './BestPracticesCard.css';
import { useEffect, useState } from "react";
import apiRoutes from '../../../../../assets/data/api_urls.json';
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import axios from "axios";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function BestPracticesCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false); // Change here

    const [bestPracticeTitle, setBestPracticeTitle] = useState("Eliminate Magic Numbers");
    const [bestPracticeDescription, setBestPracticeDescription] = useState("Magic numbers are hard-coded values that are used in the code without any explanation. They make the code difficult to read and maintain. Make sure to replace magic numbers with named constants.");

    useEffect(() => {
        const fetchData = () => {
            setLoading(true); // Set loading to true before making the API call

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
                    console.warn("Error Calling Best Practices API: " + error);
                    setError(true);
                    setErrorTitle("Error");
                    setErrorMessage("An error occurred while trying to get the best practices.");
                })
                .finally(() => {
                    // Wait half a second before setting the loading state to false
                    setTimeout(() => {
                        setLoading(false);
                    }, 1000);
                });
        };

        // Initial fetch
        fetchData();

        // Fetch data every 20 seconds
        const intervalId = setInterval(fetchData, 20000);

        // Clear the interval on component unmount to avoid memory leaks
        return () => clearInterval(intervalId);

    }, [accessToken]);

    return (
        <>
            {loading ? (
                    <SimpleDashboardCard
                        className="skeleton"
                        id={"bestPractices"}
                        style={{height: "100%", gridArea: "bestPractices"}}
                    />
            ) : (
                <>
                <SimpleDashboardCard
                    id="bestPractices"
                    style={{
                        gridArea: "bestPractices",
                        display: "grid",
                        gridTemplateColumns: "1fr 9fr",
                        gap: "2vw",
                    }}
                >
                    <div className={"best-practices-icon"}
                         style={{
                             display: "flex",
                             justifyContent: "center",
                             alignItems: "center",
                         }}
                    >
                        <i className="bi bi-shield-check" style={{fontSize: "20vh"}}> </i>
                    </div>
                    <div className={"best-practices-content"}>
                        <h4
                            style={{
                                fontSize: "5vh",
                                fontWeight: "bold",
                                margin: "0",
                            }}
                        >
                            {bestPracticeTitle}
                        </h4>
                        <p
                            style={{
                                fontSize: "3vh",
                                margin: "0",
                            }}
                        >
                            {bestPracticeDescription}
                        </p>
                    </div>
                </SimpleDashboardCard>
                </>
            )}
        </>
    );
}

export default BestPracticesCard;
