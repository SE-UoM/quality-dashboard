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
import useAxiosGet from "../../../../hooks/useAxios.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideThree({orgID}) {
    const [words, setWords] = useState([]);

    const {data: topContributorsData, error: topContributorsError, loading: topContributorsLoading, errorMessage: topContributorsErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.topContributors.replace(":organizationId", orgID), "");

    const {data: langNamesData, error: langNamesError, loading: langNamesLoading, errorMessage: langNamesErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.languageNames.replace(":organizationId", orgID), "");

    const {data: bestProjectsData, error: bestProjectsError, loading: bestProjectsLoading, errorMessage: bestProjectsErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.topProjects.replace(":organizationId", orgID), "");

    useEffect(() => {
        if (!langNamesData) return;

        let wordcloudData = langNamesData.map((word) => {
            if (word.toUpperCase() === "CXX") word = "C++";

            return {
                text: word,
                value: Math.floor(Math.random() * 100) + 1
            }
        });

        setWords(wordcloudData);
    }, [langNamesData]);

    return (
        <>
            <div className="dashboard-slide" id="slide3">
                <BestProjectsCard bestProjectsData={bestProjectsData} loading={bestProjectsLoading}/>

                <WordCloudCard style={{gridArea: "wordcloud"}}
                    words={words}
                    loading={langNamesLoading}
                    fontSizes={[60, 150]}
                />

                <TopContibutorsCard
                    topContributorsData={topContributorsData}
                    loadingTopContributors={topContributorsLoading}
                />

                <SubmittedProjectsCard orgID={orgID} />

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideThree