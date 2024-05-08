import './DashboardSlideThree.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/general/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";
import DashboardRankedItem from "../../../ui/DashboardRankedItem/DashboardRankedItem.tsx";
import ScrollableRankCard from "../../cards/general/ScrollableRankCard/ScrollableRankCard.tsx";
import WordCloudCard from "../../cards/screen3/WordCloudCard/WordCloudCard.tsx";
import starIcon from "../../../../assets/svg/dashboardIcons/github_stars_icon.svg";
import ProjectDetailsIcon from "../../../ui/ProjectDetailsIcon/ProjectDetailsIcon.tsx";
import SubmittedProjectsCard from "../../cards/screen3/SubmittedProjectsCard/SubmittedProjectsCard.tsx";
import BestProjectsCard from "../../cards/screen3/BestProjectsCard/BestProjectsCard.tsx";
import TopContibutorsCard from "../../cards/screen3/TopContibutorsCard/TopContibutorsCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function truncateString(str, maxLength) {
    if (str.length > maxLength) {
        return str.slice(0, maxLength) + '...';
    } else {
        return str;
    }
}

function DashboardSlideThree() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loadingTopProjects, setLoadingTopProjects] = useState(true);
    const [loadingTopContributors, setLoadingTopContributors] = useState(true);

    const [bestProjects, setBestProjects] = useState([]);
    const [topContributors, setTopContributors] = useState([]);

    useEffect(() => {
        const organizationId = jwtDecode(accessToken).organizationId;
        let url = baseApiUrl + apiUrls.routes.dashboard.topContributors;

        url = url.replace(":organizationId", organizationId);
        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        axios.get(url, {headers: headers})
            .then((response) => {
                let data = response.data;

                data.sort((a, b) => {
                    return b.totalCommits - a.totalCommits;
                });

                // Wait half a second before setting the state
                setTimeout(() => {
                    setLoadingTopContributors(false);
                },  1000);

                setTopContributors(data);
                console.info("Top contributors API Response: ", data)
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);

                console.warn("Error fetching top contributors: ", error)
            })
    }, [accessToken]);

    return (
        <>
            {error &&
                <ErrorModal
                    modalTitle={errorTitle}
                    modalAlertMessage={errorMessage}
                />
            }
            <div className="dashboard-slide" id="slide3">
                <BestProjectsCard
                    truncateString={truncateString}
                />

                <WordCloudCard />

                <TopContibutorsCard
                    topContributors={topContributors}
                    loadingTopContributors={loadingTopContributors}
                />

                <SubmittedProjectsCard />

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideThree