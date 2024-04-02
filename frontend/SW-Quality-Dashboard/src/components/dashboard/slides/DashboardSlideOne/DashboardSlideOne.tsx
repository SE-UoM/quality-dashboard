import './DashboardSlideOne.css'
import '../DashboardSlideStyle.css'
import LanguageRankCard from "../../cards/LanguageRankCard/LanguageRankCard.tsx";
import totalProjectsIcon from "../../../../assets/svg/dashboardIcons/total_projects_icon.svg";
import totalLanguagesIcon from "../../../../assets/svg/dashboardIcons/languages_icon.svg";
import totalDevelopersIcon from "../../../../assets/svg/dashboardIcons/developers_icon.svg";
import totalFilesIcon from "../../../../assets/svg/dashboardIcons/files_general_icon.svg";
import totalLocIcon from "../../../../assets/svg/dashboardIcons/loc_icon.svg";
import totalContributionsIcon from "../../../../assets/svg/dashboardIcons/contributions_icon.svg";

import IconCard from "../../cards/IconCard/IconCard.tsx";
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";

function DashboardSlideOne() {
    return (
        <>
            <div className="dashboard-slide" id="slide1">
                <LanguageRankCard />

                <IconCard
                    icon={totalProjectsIcon}
                    headerText="50"
                    caption="Projects"
                    gridAreaName="totalProjects"
                />

                <IconCard
                    icon={totalLanguagesIcon}
                    headerText="50"
                    caption="Languages"
                    gridAreaName="totalLanguages"
                />

                <IconCard
                    icon={totalDevelopersIcon}
                    headerText="50"
                    caption="Developers"
                    gridAreaName="totalDevelopers"
                />

                <IconCard
                    icon={totalFilesIcon}
                    headerText="50"
                    caption="Files"
                    gridAreaName="totalFiles"
                />

                <IconCard
                    icon={totalContributionsIcon}
                    headerText="5000"
                    caption="Contributions"
                    gridAreaName="totalContributions"
                />

                <IconCard
                    icon={totalLocIcon}
                    headerText="5000"
                    caption="Lines of Code"
                    gridAreaName="totalLinesOfCode"
                />

                <div className="dashboard-card" style={{gridArea: "space"}}>
                    A Graph should go here
                </div>

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideOne