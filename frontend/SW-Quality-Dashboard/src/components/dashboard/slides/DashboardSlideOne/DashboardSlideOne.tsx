import './DashboardSlideOne.css';
import '../DashboardSlideStyle.css';
import LanguageRankCard from "../../cards/screen1/LanguageRankCard/LanguageRankCard.tsx";

import IconCard from "../../cards/screen1/IconCard/IconCard.tsx";
import FooterCard from "../../cards/general/FooterCard/FooterCard.tsx";
import LanguageDistributionCard from "../../cards/screen1/LanguageDistributionCard/LanguageDistributionCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import apiUrls from "../../../../assets/data/api_urls.json";

import {jwtDecode} from "jwt-decode";
import {formatText} from "../../../../utils/textUtils.ts";
import useAxiosGet from "../../../../hooks/useAxios.ts";
import apiRoutes from "../../../../assets/data/api_urls.json";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideOne({orgID}) {
    const {data: generalStats, loading: generalStatsLoading, error: generalStatsError, errorMessage: generalStatsErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.generalStats.replace(":organizationId", orgID), "");


    const {data: topLanguages, loading: topLanguagesLoading, error: topLanguagesError, errorMessage: topLanguagesErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.topLanguages.replace(":organizationId", orgID), "");

    const {data: languageDistributionData, loading: languageDistributionLoading, error: languageDistributionError, errorMessage: languageDistributionErrorMessage} =
        useAxiosGet(baseApiUrl + apiRoutes.routes.dashboard.languageDistribution.replace(":organizationId", orgID), "");

    return (
        <>
            <div className="dashboard-slide" id="slide1">
                {topLanguages &&
                    <LanguageRankCard
                        topLanguages={topLanguages}
                        topLanguagesLoading={topLanguagesLoading}
                        topLanguagesError={topLanguagesError}
                        topLanguagesErrorMessage={topLanguagesErrorMessage}
                    />
                }

                {generalStats &&
                    <>
                        <IconCard
                            icon="bi bi-laptop"
                            headerText={formatText(generalStats.totalProjects, "k")}
                            caption="Projects"
                            gridAreaName="totalProjects"
                            loading={generalStatsLoading}
                        />

                        <IconCard
                            icon="bi bi-code-slash"
                            headerText={formatText(generalStats.totalLanguages, "k")}
                            caption="Languages"
                            gridAreaName="totalLanguages"
                            loading={generalStatsLoading}
                        />

                        <IconCard
                            icon="bi bi-person"
                            headerText={formatText(generalStats.totalDevs, "k")}
                            caption="Developers"
                            gridAreaName="totalDevelopers"
                            loading={generalStatsLoading}
                        />

                        <IconCard
                            icon="bi bi-file-earmark-binary"
                            headerText={formatText(generalStats.totalFiles, "k")}
                            caption="Files"
                            gridAreaName="totalFiles"
                            loading={generalStatsLoading}
                        />

                        <IconCard
                            icon="bi bi-bezier2"
                            headerText={formatText(generalStats.totalCommits, "k")}
                            caption="Contributions"
                            gridAreaName="totalContributions"
                            loading={generalStatsLoading}
                        />

                        <IconCard
                            icon="bi bi-body-text"
                            headerText={formatText(generalStats.totalLinesOfCode, "k")}
                            caption="Lines of Code"
                            gridAreaName="totalLinesOfCode"
                            loading={generalStatsLoading}
                        />
                    </>
                }

                {languageDistributionData &&
                    <LanguageDistributionCard
                        languageDistributionData={languageDistributionData}
                        languageDistributionLoading={languageDistributionLoading}
                        languageDistributionError={languageDistributionError}
                        languageDistributionErrorMessage={languageDistributionErrorMessage}
                    />
                }

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideOne;
