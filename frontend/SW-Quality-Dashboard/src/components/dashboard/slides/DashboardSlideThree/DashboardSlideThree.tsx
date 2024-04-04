import './DashboardSlideThree.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";
import DashboardProjectComponent from "../../../ui/DashboardProjectComponent/DashboardProjectComponent.tsx";
import BestProjectsCard from "../../cards/BestProjectsCard/BestProjectsCard.tsx";
const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideThree() {
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
            <div className="dashboard-slide" id="slide3">
                <BestProjectsCard />

                <div className="dashboard-card"
                     style={{gridArea: "wordcloud"}}
                >
                    <h1>World cloud</h1>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "topCommiters"}}
                >
                    <h1>Top Contributors</h1>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "submittedProjects"}}
                >
                    <h1>Submitted Projects</h1>
                </div>

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideThree