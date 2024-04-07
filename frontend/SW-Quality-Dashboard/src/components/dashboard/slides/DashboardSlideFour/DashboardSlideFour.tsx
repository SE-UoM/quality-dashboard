import './DashboardSlideFour.css'
import '../DashboardSlideStyle.css'
import FooterCard from "../../cards/FooterCard/FooterCard.tsx";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import contributionsIcon from "../../../../assets/svg/dashboardIcons/contributions_icon.svg";
import githubIcon from "../../../../assets/svg/dashboardIcons/github_icon.svg";
import starIcon from "../../../../assets/svg/dashboardIcons/github_stars_icon.svg";
import ItemActivityCard from "../../cards/ItemActivityCard/ItemActivityCard.tsx";
import {jwtDecode} from "jwt-decode";
import ProjectCard from "../../cards/screen4/ProjectCard/ProjectCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function DashboardSlideFour() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const [mostActiveDevImage, setMostActiveDevImage] = useState("");
    const [mostActiveDevName, setMostActiveDevName] = useState("");
    const [mostActiveDevCommits, setMostActiveDevCommits] = useState(0);

    const [mostActiveProjImage, setMostActiveProjImage] = useState("");
    const [mostActiveProjName, setMostActiveProjName] = useState("");
    const [mostActiveProjCommits, setMostActiveProjCommits] = useState(0);

    const [mostStarredProjName, setMostStarredProjName] = useState("Test");
    const [mostStarredProjFiles, setMostStarredProjFiles] = useState(0);
    const [mostStarredProjLines, setMostStarredProjLines] = useState(0);
    const [mostStarredProjSmells, setMostStarredProjSmells] = useState(0);
    const [mostStarredProjDebt, setMostStarredProjDebt] = useState(0);
    const [mostStarredProjDevName, setMostStarredProjDevName] = useState("Test Dev");


    const [mostForkedProjName, setMostForkedProjName] = useState("Test");
    const [mostForkedProjFiles, setMostForkedProjFiles] = useState(0);
    const [mostForkedProjLines, setMostForkedProjLines] = useState(0);
    const [mostForkedProjSmells, setMostForkedProjSmells] = useState(0);
    const [mostForkedProjDebt, setMostForkedProjDebt] = useState(0);
    const [mostForkedProjDevName, setMostForkedProjDevName] = useState("Test Dev");


    // Call the API to get the most active developer
    useEffect(() => {
        let mostActiveDevUrl = baseApiUrl + apiUrls.routes.dashboard.mostActiveDeveloper
        // Replace the organization id in the URL
        mostActiveDevUrl = mostActiveDevUrl.replace(":organizationId", jwtDecode(accessToken).organizationId);

        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        axios.get(mostActiveDevUrl, {headers: headers})
            .then((response) => {
                let data = response.data;

                setMostActiveDevImage(data.avatarUrl);
                setMostActiveDevName(data.name);
                setMostActiveDevCommits(data.totalCommits);
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });


    }, [accessToken]);

    // Call the API to get the most active project
    useEffect(() => {
        let mostActiveProjUrl = baseApiUrl + apiUrls.routes.dashboard.mostActiveProject
        // Replace the organization id in the URL
        mostActiveProjUrl = mostActiveProjUrl.replace(":organizationId", jwtDecode(accessToken).organizationId);

        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        axios.get(mostActiveProjUrl, {headers: headers})
            .then((response) => {
                let data = response.data;

                setMostActiveProjImage(githubIcon);
                setMostActiveProjName(data.name);
                setMostActiveProjCommits(data.totalCommits);
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });
    }, [accessToken]);

    // Call the API to get the most starred project
    useEffect(() => {
        let mostStarredProjUrl = baseApiUrl + apiUrls.routes.dashboard.mostStarredProject

        // Replace the organization id in the URL
        mostStarredProjUrl = mostStarredProjUrl.replace(":organizationId", jwtDecode(accessToken).organizationId);

        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        axios.get(mostStarredProjUrl, {headers: headers})
            .then((response) => {
                let data = response.data;

                setMostStarredProjName(data.name);
                setMostStarredProjFiles(data.files);
                setMostStarredProjLines(data.loc);
                setMostStarredProjSmells(data.totalCodeSmells);
                setMostStarredProjDebt(data.techDebt);
                setMostStarredProjDevName(data.owner);
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });
    }, [accessToken]);

    // Call the API to get the most forked project
    useEffect(() => {
        let mostForkedProjUrl = baseApiUrl + apiUrls.routes.dashboard.mostForkedProject

        // Replace the organization id in the URL
        mostForkedProjUrl = mostForkedProjUrl.replace(":organizationId", jwtDecode(accessToken).organizationId);

        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        axios.get(mostForkedProjUrl, {headers: headers})
            .then((response) => {
                let data = response.data;

                setMostForkedProjName(data.name);
                setMostForkedProjFiles(data.files);
                setMostForkedProjLines(data.loc);
                setMostForkedProjSmells(data.totalCodeSmells);
                setMostForkedProjDebt(data.techDebt);
                setMostForkedProjDevName(data.owner);
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
            <div className="dashboard-slide" id="slide4">
                <ItemActivityCard
                    cardTitle={"Most Active Developer"}
                    cardTitleIcon={"bi bi-person-workspace"}
                    cardImage={mostActiveDevImage}
                    cardIcon={contributionsIcon}
                    countTitle={mostActiveDevName}
                    count={mostActiveDevCommits}
                    countCaption={"Commits"}
                    gridArea={"mostActiveDev"}
                />

                <ItemActivityCard
                    cardTitle={"Most Active Project"}
                    cardTitleIcon={"bi bi-fire"}
                    cardImage={mostActiveProjImage}
                    cardIcon={contributionsIcon}
                    countTitle={mostActiveProjName}
                    count={mostActiveProjCommits}
                    countCaption={"Commits"}
                    gridArea={"mostActiveProj"}
                />

                <ProjectCard
                    cardHeader={"Most Starred Project"}
                    cardHeaderIcon={"bi bi-star-fill"}
                    id={"mostStarredProj"}
                    contentImage={starIcon}
                    projectName={mostStarredProjName}
                    nameSubText={"By: " + mostStarredProjDevName}
                    totalFiles={mostStarredProjFiles}
                    totalLines={mostStarredProjLines}
                    totalDebt={mostStarredProjDebt}
                    totalCodeSmells={mostStarredProjSmells}
                />

                <ProjectCard
                    cardHeader={"Most Forked Project"}
                    cardHeaderIcon={"bi bi-git"}
                    id={"mostForked"}
                    contentImage={githubIcon}
                    projectName={mostForkedProjName}
                    nameSubText={"By: " + mostForkedProjDevName}
                    totalFiles={mostForkedProjFiles}
                    totalLines={mostForkedProjLines}
                    totalDebt={mostForkedProjDebt}
                    totalCodeSmells={mostForkedProjSmells}
                />

                <div className="dashboard-card"
                     style={{gridArea: "commitGraph"}}
                >
                    <h1> </h1>
                    <i className="bi bi-cone-striped"><strong> Coming Soon...</strong></i>
                </div>

                <div className="dashboard-card"
                     style={{gridArea: "developersSlides"}}
                >
                    <h1> </h1>
                    <i className="bi bi-cone-striped"><strong> Coming Soon...</strong></i>
                </div>

                <FooterCard
                    gridAreaName="footerCard"
                />
            </div>
        </>
    )
}

export default DashboardSlideFour