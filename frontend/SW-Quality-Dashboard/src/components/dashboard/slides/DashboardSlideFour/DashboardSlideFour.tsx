import './DashboardSlideFour.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideFour() {
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
            <div className="dashboard-slide" id="slide4">
                <div className="dashboard-card"
                     style={{gridArea: "mostActiveDev"}}
                >
                    <h1>Most Active Dev</h1>
                    <i className="bi bi-cone-striped"><strong> Coming Soon...</strong></i>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "mostActiveProj"}}
                >
                    <h1>Most Active Project</h1>
                    <i className="bi bi-cone-striped"><strong> Coming Soon...</strong></i>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "mostStarredProj"}}
                >
                    <h1>Most Starred Project</h1>
                    <i className="bi bi-cone-striped"><strong> Coming Soon...</strong></i>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "commitGraph"}}
                >
                    <h1>Commit Graph</h1>
                    <i className="bi bi-cone-striped"><strong> Coming Soon...</strong></i>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "mostForked"}}
                >
                    <h1>Most Forked</h1>
                    <i className="bi bi-cone-striped"><strong> Coming Soon...</strong></i>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "developersSlides"}}
                >
                    <h1>Developers</h1>
                    <i className="bi bi-cone-striped"><strong> Coming Soon...</strong></i>
                </div>

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideFour