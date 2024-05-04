import './DashboardSlideOne.css';
import '../DashboardSlideStyle.css';
import LanguageRankCard from "../../cards/screen1/LanguageRankCard/LanguageRankCard.tsx";
import totalProjectsIcon from "../../../../assets/svg/dashboardIcons/total_projects_icon.svg";
import totalLanguagesIcon from "../../../../assets/svg/dashboardIcons/languages_icon.svg";
import totalDevelopersIcon from "../../../../assets/svg/dashboardIcons/developers_icon.svg";
import totalFilesIcon from "../../../../assets/svg/dashboardIcons/files_general_icon.svg";
import totalLocIcon from "../../../../assets/svg/dashboardIcons/loc_icon.svg";
import totalContributionsIcon from "../../../../assets/svg/dashboardIcons/contributions_icon.svg";

import IconCard from "../../cards/screen1/IconCard/IconCard.tsx";
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";
import LanguageDistributionCard from "../../cards/screen1/LanguageDistributionCard/LanguageDistributionCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideOne() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [totalProjects, setTotalProjects] = useState();
    const [totalLanguages, setTotalLanguages] = useState();
    const [totalDevelopers, setTotalDevelopers] = useState();
    const [totalFiles, setTotalFiles] = useState();
    const [totalContributions, setTotalContributions] = useState();
    const [totalLoc, setTotalLoc] = useState();

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    // Call the API to get the general statistics
    useEffect(() => {
        let url = baseApiUrl + apiUrls.routes.dashboard.generalStats;

        // Decode the access token to get the organization ID
        let organizationId = jwtDecode(accessToken).organizationId;

        // Replace ":organizationId" with the actual organization ID
        url = url.replace(":organizationId", organizationId);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.get(url, { headers: headers })
            .then((response) => {
                let data = response.data;

                setTotalProjects(data.totalProjects);
                setTotalLanguages(data.totalLanguages);
                setTotalDevelopers(data.totalDevs);
                setTotalFiles(data.totalFiles);
                setTotalContributions(data.totalCommits);
                setTotalLoc(data.totalLinesOfCode);

                setTimeout(() => {
                    // Do nothing
                    console.log("waiting...")
                    setLoading(false); // Set loading to false once data is fetched
                }, 1000);
            })
            .catch((error) => {
                    console.warn("Error fetching general stats: ", error);
                    setError(true);
                    setErrorTitle("Error fetching general statistics");
                    setErrorMessage("An error occurred while fetching the general statistics of the organization. Please try again later.");
                    setLoading(false); // Set loading to false on error
                }
            );
    }, [accessToken]);


    return (
        <>
            {error &&
                <ErrorModal
                    modalTitle={errorTitle}
                    modalAlertMessage={errorMessage}
                />
            }
            <div className="dashboard-slide" id="slide1">
                <LanguageRankCard />

                <IconCard
                    icon={totalProjectsIcon}
                    headerText={totalProjects}
                    caption="Projects"
                    gridAreaName="totalProjects"
                    loading={loading}
                />

                <IconCard
                    icon={totalLanguagesIcon}
                    headerText={totalLanguages}
                    caption="Languages"
                    gridAreaName="totalLanguages"
                    loading={loading}
                />

                <IconCard
                    icon={totalDevelopersIcon}
                    headerText={totalDevelopers}
                    caption="Developers"
                    gridAreaName="totalDevelopers"
                    loading={loading}
                />

                <IconCard
                    icon={totalFilesIcon}
                    headerText={totalFiles}
                    caption="Files"
                    gridAreaName="totalFiles"
                    loading={loading}
                />

                <IconCard
                    icon={totalContributionsIcon}
                    headerText={totalContributions}
                    caption="Contributions"
                    gridAreaName="totalContributions"
                    loading={loading}
                />

                <IconCard
                    icon={totalLocIcon}
                    headerText={totalLoc}
                    caption="Lines of Code"
                    gridAreaName="totalLinesOfCode"
                    loading={loading}
                />

                <LanguageDistributionCard />

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideOne;
