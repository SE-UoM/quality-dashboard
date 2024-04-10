import './DashboardSlideTwo.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";
import CodeSmellDistributionCard from "../../cards/screen2/CodeSmellDistributionCard/CodeSmellDistributionCard.tsx";
import TotalTechDebtCard from "../../cards/screen2/TotalTechDebtCard/TotalTechDebtCard.tsx";
import TechDebtStatsCard from "../../cards/screen2/TechDebtStatsCard/TechDebtStatsCard.tsx";
import BestPracticesCard from "../../cards/screen2/BestPracticesCard/BestPracticesCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideTwo() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

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
                <TotalTechDebtCard />
                <TechDebtStatsCard />
                <BestPracticesCard />
                <FooterCard gridAreaName="footerCard" />
            </div>
        </>
    )
}

export default DashboardSlideTwo