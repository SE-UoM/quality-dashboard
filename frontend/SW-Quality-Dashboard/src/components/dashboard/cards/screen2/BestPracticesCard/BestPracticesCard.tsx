import './BestPracticesCard.css';
import { useEffect, useState } from "react";
import apiRoutes from '../../../../../assets/data/api_urls.json';
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import axios from "axios";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function BestPracticesCard({bestPracticesData, loading}) {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [bestPracticeTitle, setBestPracticeTitle] = useState("Eliminate Magic Numbers");
    const [bestPracticeDescription, setBestPracticeDescription] = useState("Magic numbers are hard-coded values that are used in the code without any explanation. They make the code difficult to read and maintain. Make sure to replace magic numbers with named constants.");

    useEffect(() => {
        if (!bestPracticesData) return;

        // Now and every ten seconds, get a random best practice
        let bestPractice = getRandomBestPractice();

        setBestPracticeTitle(bestPractice.title);
        setBestPracticeDescription(bestPractice.explanation);

        const interval = setInterval(() => {
            bestPractice = getRandomBestPractice();
            setBestPracticeTitle(bestPractice.title);
            setBestPracticeDescription(bestPractice.explanation);
        }, 10000);

        return () => clearInterval(interval);

    }, [bestPracticesData]);

    function getRandomBestPractice() {
        let index = Math.floor(Math.random() * bestPracticesData.length);
        return bestPracticesData[index];
    }

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
