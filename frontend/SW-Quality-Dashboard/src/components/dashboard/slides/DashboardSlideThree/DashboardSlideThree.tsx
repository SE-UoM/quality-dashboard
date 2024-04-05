import './DashboardSlideThree.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";
import DashboardRankedItem from "../../../ui/DashboardRankedItem/DashboardRankedItem.tsx";
import ScrollableRankCard from "../../cards/ScrollableRankCard/ScrollableRankCard.tsx";
import WordCloudCard from "../../cards/WordCloudCard/WordCloudCard.tsx";
import starIcon from "../../../../assets/svg/dashboardIcons/github_stars_icon.svg";
import ProjectDetailsIcon from "../../../ui/ProjectDetailsIcon/ProjectDetailsIcon.tsx";
import SubmittedProjectsCard from "../../cards/SubmittedProjectsCard/SubmittedProjectsCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideThree() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const [bestProjects, setBestProjects] = useState([]);
    const [topContributors, setTopContributors] = useState([]);

    useEffect(() => {
        // Extract the organization id from the access token
        const organizationId = jwtDecode(accessToken).organizationId;

        let url = baseApiUrl + apiUrls.routes.dashboard.topProjects;

        // Replace the organization id in the URL
        url = url.replace(":organizationId", organizationId);
        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        axios.get(url, {headers: headers})
            .then((response) => {
                let data = response.data;

                // Sort based on techDebtPerLoc
                data.sort((a, b) => {
                    return a.techDebtPerLoc - b.techDebtPerLoc;
                });

                setBestProjects(data);
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });

    }, [accessToken]);

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

                setTopContributors(data);
                console.log(data);
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
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
                <ScrollableRankCard
                    title="Best Projects"
                    icon="bi bi-bookmark-star"
                    cardId="scrollableRankCard"
                    gridArea={"bestProjects"}
                >
                    {bestProjects.map((item, index) => {
                        let name = item.name;
                        let techDebtPerLoc = item.techDebtPerLoc;
                        let rank = index + 1;
                        return (
                            <DashboardRankedItem
                                key={index}
                                projectName={name}
                                owner={techDebtPerLoc}
                                rank={rank}
                            />
                        )
                    })}
                </ScrollableRankCard>

                <WordCloudCard />

                <ScrollableRankCard
                    title="Top Contributors"
                    icon="bi bi-person"
                    cardId="scrollableRankCard"
                    gridArea={"topCommiters"}
                >
                    {topContributors.map((item, index) => {
                        let name = item.name;
                        let totalCommits = item.totalCommits;
                        let rank = index + 1;
                        return (
                            <DashboardRankedItem
                                key={index}
                                projectName={name}
                                owner={totalCommits}
                                rank={rank}
                            />
                        )
                    })}
                </ScrollableRankCard>

                <SubmittedProjectsCard />

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideThree