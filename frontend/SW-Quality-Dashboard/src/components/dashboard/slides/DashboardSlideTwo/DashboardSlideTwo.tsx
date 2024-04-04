import './DashboardSlideTwo.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";
import CodeSmellDistributionCard from "../../cards/CodeSmellDistributionCard/CodeSmellDistributionCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideTwo() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    // Call the API to get the general statistics


    return (
        <>
            {error &&
                <ErrorModal
                    modalTitle={errorTitle}
                    modalAlertMessage={errorMessage}
                />
            }
            <div className="dashboard-slide" id="slide2">
                <CodeSmellDistributionCard />

                <div className="dashboard-card"
                     style={{gridArea: "techDebt"}}
                >
                    <h1>Tech Debt</h1>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "techDebtStats"}}
                >
                    <h1>Tech Debt Stats</h1>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "bestPractices"}}
                >
                    <h1>Best Practices</h1>
                </div>

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideTwo