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

function LanguageRankCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [firstLanguage, setFirstLanguage] = useState("");
    const [secondLanguage, setSecondLanguage] = useState("");
    const [thirdLanguage, setThirdLanguage] = useState("");
    const [error, setError] = useState(false);

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
                console.log("Top languages fetched successfully");
                let data = response.data;
                setFirstLanguage(data.first);
                setSecondLanguage(data.second);
                setThirdLanguage(data.third);
            })
            .catch((error) => {
                console.log("Error fetching top languages: ", error);
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

            <div className="dashboard-card" id="languageRank">
                <h2>
                    <i className="bi bi-trophy-fill"> </i>
                    Top Languages in UoM
                </h2>

                <div className="language-rank-container">
                    <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            {secondLanguage}
                        </span>
                        <div className="language-rank-line" id={"second"}>
                            <img src={secondMedal} className="medal-icon"/>
                        </div>
                    </div>

                    <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            {firstLanguage}
                        </span>
                        <div className="language-rank-line" id={"first"}>
                            <img src={firstMedal} className="medal-icon"/>
                        </div>
                    </div>

                    <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            {thirdLanguage}
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