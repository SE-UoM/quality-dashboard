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
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import useAxiosGet from "../../../../../hooks/useAxios.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

const languageImagesApiUrl = "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/"
const noneImageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Blue_question_mark_icon.svg/640px-Blue_question_mark_icon.svg.png"

function extractLangImageName(langName) {
    if (!langName) return "None";

    if (langName === "CSS") langName = "css3";
    if (langName === "WEB" || langName === "HTML") langName = "html5";
    if (langName === "CXX") langName = "cplusplus";

    return langName.toLowerCase();
}

function resetLangImageName(langName, resetTo) {
    if (!langName) return "None";

    if (langName === "css3") langName = "CSS";
    if (langName === "html5") langName = "HTML";
    if (langName === "cplusplus") langName = "C++";

    return langName;
}

function createLangComponentObject(langName, langImg) {
    if (!langName) {
        return {
            name: "None",
            image: noneImageUrl
        }
    }

    return {
        name: langName,
        image: langImg
    }
}

let defaultTopLanguages = [
    {
        name: "None",
        image: noneImageUrl
    },
    {
        name: "None",
        image: noneImageUrl
    },
    {
        name: "None",
        image: noneImageUrl
    }
]

function LanguageRankCard({topLanguages, topLanguagesLoading, topLanguagesError, topLanguagesErrorMessage}) {
    const [firstLanguage, setFirstLanguage] = useState("");
    const [secondLanguage, setSecondLanguage] = useState("");
    const [thirdLanguage, setThirdLanguage] = useState("");

    // Call the API to get the top languages
    useEffect(() => {
        if (!topLanguages) {
            return
        }

        let firstLanguageName = topLanguages[1] ? topLanguages[1].name : "";
        let secondLanguageName = topLanguages[2] ? topLanguages[2].name : "";
        let thirdLanguageName = topLanguages[3] ? topLanguages[3].name : "";

        let firstLangImg = "";
        let secondLangImg = "";
        let thirdLangImg = "";

        firstLanguageName = extractLangImageName(firstLanguageName);
        secondLanguageName = extractLangImageName(secondLanguageName);
        thirdLanguageName = extractLangImageName(thirdLanguageName);

        firstLangImg = firstLanguageName ? languageImagesApiUrl + firstLanguageName + "/" + firstLanguageName + "-original.svg" : noneImageUrl;
        secondLangImg = secondLanguageName ? languageImagesApiUrl + secondLanguageName + "/" + secondLanguageName + "-original.svg" : noneImageUrl;
        thirdLangImg = thirdLanguageName ? languageImagesApiUrl + thirdLanguageName + "/" + thirdLanguageName + "-original.svg" : noneImageUrl;

        // if the language is cplusplus name it C++
        firstLanguageName = resetLangImageName(firstLanguageName, "C++");
        secondLanguageName = resetLangImageName(secondLanguageName, "C++");
        thirdLanguageName = resetLangImageName(thirdLanguageName, "C++");

        let firstLang = createLangComponentObject(firstLanguageName, firstLangImg);
        let secondLang = createLangComponentObject(secondLanguageName, secondLangImg);
        let thirdLang = createLangComponentObject(thirdLanguageName, thirdLangImg);

        // if the language is none, set the image to noneImageUrl
        if (firstLanguageName === "None") {
            firstLang.image = noneImageUrl;
        }
        if (secondLanguageName === "None") {
            secondLang.image = noneImageUrl;
        }
        if (thirdLanguageName === "None") {
            thirdLang.image = noneImageUrl;
        }

        setFirstLanguage(firstLang);
        setSecondLanguage(secondLang);
        setThirdLanguage(thirdLang);

        console.log(secondLanguage.image)
    }, [topLanguages]);

    return (
        <>
            {topLanguagesLoading ?
                (
                    <SimpleDashboardCard
                        className="skeleton"
                        id={"languageRank"}
                        style={{height: "100%"}}
                    />
                ) :
                (
                    <SimpleDashboardCard
                        id="languageRank"
                    >
                        <h2>
                            <i className="bi bi-trophy-fill"> </i>
                            Top Languages in UoM
                        </h2>

                        <div className="language-rank-container">
                            <div className="language-rank">
                        <span className={"lang-name-rank"}>
                             <>
                                 <img src={secondLanguage.image !== '' ? secondLanguage.image : noneImageUrl}/>
                                 {secondLanguage.name ? secondLanguage.name.toUpperCase() : "None"}
                             </>
                        </span>
                                <div className="language-rank-line glass" id={"second"}>
                                    {/*<img src={secondMedal} className="medal-icon"/>*/}
                                    <h1 className="text-base-200"
                                        style={{fontSize: "10vh"}}
                                    >
                                        <strong>2</strong>
                                    </h1>
                                </div>
                            </div>

                            <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            <>
                                <img src={firstLanguage.image}/>
                                {firstLanguage.name.toUpperCase()}
                            </>
                        </span>
                                <div className="language-rank-line glass" id={"first"}>
                                    {/*<img src={firstMedal} className="medal-icon"/>*/}
                                    <h1 className="text-base-200"
                                        style={{fontSize: "10vh"}}
                                    >
                                        <strong>1</strong>
                                    </h1>
                                </div>
                            </div>

                            <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            <>
                                <img src={thirdLanguage.image}/>
                                {thirdLanguage.name.toUpperCase()}
                            </>
                        </span>
                                <div className="language-rank-line glass" id={"third"}>
                                    {/*<img src={thirdMedal} className="medal-icon"/>*/}
                                    <h1 className="text-base-200"
                                        style={{fontSize: "10vh"}}
                                    >
                                        <strong>3</strong>
                                    </h1>
                                </div>
                            </div>

                        </div>
                    </SimpleDashboardCard>
                )
            }
        </>
    )
}

export default LanguageRankCard