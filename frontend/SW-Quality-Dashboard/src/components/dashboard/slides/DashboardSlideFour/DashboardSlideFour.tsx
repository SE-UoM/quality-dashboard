import './DashboardSlideFour.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import contributionsIcon from "../../../../assets/svg/dashboardIcons/contributions_icon.svg";
import ItemActivityCard from "../../cards/ItemActivityCard/ItemActivityCard.tsx";

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
                <ItemActivityCard
                    cardTitle={"Most Active Developer"}
                    cardTitleIcon={"bi bi-person-workspace"}
                    cardImage={"https://via.placeholder.com/150"}
                    cardIcon={contributionsIcon}
                    countTitle={"Archontis Kostis"}
                    count={0}
                    countCaption={"Commits"}
                    gridArea={"mostActiveDev"}
                />

                <ItemActivityCard
                    cardTitle={"Most Active Project"}
                    cardTitleIcon={"bi bi-fire"}
                    cardImage={"https://via.placeholder.com/150"}
                    cardIcon={contributionsIcon}
                    countTitle={"PyAssess"}
                    count={0}
                    countCaption={"Commits"}
                    gridArea={"mostActiveProj"}
                />

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