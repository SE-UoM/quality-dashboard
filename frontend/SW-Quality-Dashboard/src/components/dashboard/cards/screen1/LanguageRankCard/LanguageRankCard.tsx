import './LanguageRankCard.css'
import '../../DashboardCardStyle.css'
import firstMedal from '../../../../../assets/svg/dashboardIcons/first_medal_icon.svg'
import secondMedal from '../../../../../assets/svg/dashboardIcons/second_medal_icon.svg'
import thirdMedal from '../../../../../assets/svg/dashboardIcons/third_medal_icon.svg'
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import {jwtDecode} from "jwt-decode";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

const languageImagesApiUrl = "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/"
const noneImageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Blue_question_mark_icon.svg/640px-Blue_question_mark_icon.svg.png"

function LanguageRankCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [firstLanguage, setFirstLanguage] = useState("");
    const [firstLanguageImage, setFirstLanguageImage] = useState("");
    const [secondLanguage, setSecondLanguage] = useState("");
    const [secondLanguageImage, setSecondLanguageImage] = useState("");
    const [thirdLanguage, setThirdLanguage] = useState("");
    const [thirdLanguageImage, setThirdLanguageImage] = useState("");

    const [error, setError] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    // Call the API to get the top languages
    useEffect(() => {
        let url = baseApiUrl + apiUrls.routes.dashboard.topLanguages;

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
                console.info("Top Languages API response: ", response.data);

                // Get the values from the 1, 2 and 3 keys of the response data (if they exist dont exist, set them to empty string)
                let first = data[1] ? data[1] : {name: ""};
                let second = data[2] ? data[2] : {name: ""};
                let third = data[3] ? data[3] : {name: ""};

                // CSS and HTML on the devicons API are named css3 and html5 respectively, so we need to change them
                // If we don't do this, the images will not be displayed
                if (first.name.toUpperCase() === "CSS") first.name = "css3";
                if (second.name.toUpperCase() === "CSS") second.name = "css3";
                if (third.name.toUpperCase() === "CSS") third.name = "css3";

                if (first.name.toUpperCase() === "WEB" || first.name.toUpperCase() === "HTML") first.name = "html5";
                if (second.name.toUpperCase() === "WEB" || second.name.toUpperCase() === "HTML") second.name = "html5";
                if (third.name.toUpperCase() === "WEB" || third.name.toUpperCase() === "HTML") third.name = "html5";

                // CXX is C++ in sonarqube, so we need to change it to cplusplus
                if (first.name.toUpperCase() === "CXX") first.name = "cplusplus";
                if (second.name.toUpperCase() === "CXX") second.name = "cplusplus";
                if (third.name.toUpperCase() === "CXX") third.name = "cplusplus";

                // make the language images using the api (if the language does not exist, set the image to a question mark)
                let firstImageUrl = first.name ? languageImagesApiUrl + first.name.toLowerCase() + "/" + first.name.toLowerCase() + "-original.svg" : noneImageUrl;
                let secondImageUrl = second.name ? languageImagesApiUrl + second.name.toLowerCase() + "/" + second.name.toLowerCase() + "-original.svg" : noneImageUrl;
                let thirdImageUrl = third.name ? languageImagesApiUrl + third.name.toLowerCase() + "/" + third.name.toLowerCase() + "-original.svg" : noneImageUrl;

                // if the language is cplusplus name it C++
                if (first.name === "cplusplus") first.name = "C++";
                if (second.name === "cplusplus") second.name = "C++";
                if (third.name === "cplusplus") third.name = "C++";

                // Wait half a second before setting the state to prevent the loading spinner from flashing
                setTimeout(() => {
                    setIsLoading(false);
                }, 500);

                setFirstLanguage(first.name);
                setSecondLanguage(second.name);
                setThirdLanguage(third.name);

                setThirdLanguageImage(thirdImageUrl);
                setSecondLanguageImage(secondImageUrl);
                setFirstLanguageImage(firstImageUrl);
            })
            .catch((error) => {
                console.warn("Error fetching top languages: ", error);
                setError(true);
            });
    }, [accessToken]);

    return (
        <>
            {error &&
                <ErrorModal
                    modalTitle={"Error fetching top languages"}
                    modalAlertMessage={"An error occurred while fetching the top languages. Please try again later."}
                />
            }

            <div className={"dashboard-card card bg-base-200"} id="languageRank">
                <h2>
                    <i className="bi bi-trophy-fill"> </i>
                    Top Languages in UoM
                </h2>

                <div className="language-rank-container">
                    <div className="language-rank">
                        <span className={"lang-name-rank"}>

                            {
                                isLoading ?
                                    <>
                                        <div className="skeleton-lang-img"> </div>
                                        <div className="skeleton-lang-name"> </div>
                                    </>
                                    :
                                    <>
                                        <img src={secondLanguageImage}/>
                                        {secondLanguage}
                                    </>
                            }
                        </span>
                        <div className="language-rank-line" id={"second"}>
                            <img src={secondMedal} className="medal-icon"/>
                        </div>
                    </div>

                    <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            {
                                isLoading ?
                                    <>
                                        <div className="skeleton-lang-img"> </div>
                                        <div className="skeleton-lang-name"> </div>
                                    </>
                                    :
                                    <>
                                        <img src={firstLanguageImage}/>
                                        {firstLanguage}
                                    </>
                            }
                        </span>
                        <div className="language-rank-line" id={"first"}>
                            <img src={firstMedal} className="medal-icon"/>
                        </div>
                    </div>

                    <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            {
                                isLoading ?
                                    <>
                                        <div className="skeleton-lang-img"> </div>
                                        <div className="skeleton-lang-name"> </div>
                                    </>
                                    :
                                    <>
                                        <img src={thirdLanguageImage}/>
                                        {thirdLanguage}
                                    </>
                            }
                        </span>
                        <div className="language-rank-line" id={"third"}>
                            <img src={thirdMedal} className="medal-icon"/>
                        </div>
                    </div>

                </div>
            </div>
        </>
    )
}

export default LanguageRankCard