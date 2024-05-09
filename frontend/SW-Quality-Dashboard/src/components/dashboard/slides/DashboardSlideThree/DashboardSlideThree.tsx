import './DashboardSlideThree.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/general/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";
import WordCloudCard from "../../cards/screen3/WordCloudCard/WordCloudCard.tsx";
import SubmittedProjectsCard from "../../cards/screen3/SubmittedProjectsCard/SubmittedProjectsCard.tsx";
import BestProjectsCard from "../../cards/screen3/BestProjectsCard/BestProjectsCard.tsx";
import TopContibutorsCard from "../../cards/screen3/TopContibutorsCard/TopContibutorsCard.tsx";
import {truncateString} from "../../../../utils/textUtils.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideThree() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loadingTopProjects, setLoadingTopProjects] = useState(true);
    const [loadingTopContributors, setLoadingTopContributors] = useState(true);

    const [bestProjects, setBestProjects] = useState([]);
    const [topContributors, setTopContributors] = useState([]);

    const [words, setWords] = useState([]);
    const [loading, setLoading] = useState(true);

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

    // Call the API to get the word cloud data
    useEffect(() => {
        // Extract the organization id from the access token
        const organizationId = jwtDecode(accessToken).organizationId;

        let url = baseApiUrl + apiUrls.routes.dashboard.languageNames

        // Replace the organization id in the URL
        url = url.replace(":organizationId", organizationId);
        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        console.log(url)

        axios.get(url, {headers: headers})
            .then((response) => {
                let data = response.data;

                // Wait half a second before setting the state
                setTimeout(() => {
                    setLoading(false);
                }, 3000);

                let respData = response.data;

                let finalData = respData.map((word) => {
                    if (word.toUpperCase() === "CXX") word = "C++";

                    return {
                        text: word,
                        value: Math.floor(Math.random() * 100) + 1
                    }
                });

                setWords(finalData);
                console.info(data)
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });

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

                <WordCloudCard style={{
                        gridArea: "wordcloud",
                        height: "100%",
                    }}
                    words={words}
                    loading={loading}
                    fontSizes={[60, 150]}
                />

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