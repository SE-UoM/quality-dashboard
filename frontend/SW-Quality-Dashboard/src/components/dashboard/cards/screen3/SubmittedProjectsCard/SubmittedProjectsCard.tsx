import './SubmittedProjectsCard.css'
import ProjectDetailsIcon from "../../../../ui/ProjectDetailsIcon/ProjectDetailsIcon.tsx";
import starIcon from "../../../../../assets/svg/dashboardIcons/github_stars_icon.svg";
import contributionsIcon from "../../../../../assets/svg/dashboardIcons/contributions_icon.svg";
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import { useEffect, useState } from "react";
import apiUrls from "../../../../../assets/data/api_urls.json";
import { jwtDecode } from "jwt-decode";
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function SubmittedProjectsCard() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const [submittedProjects, setSubmittedProjects] = useState([]);
    const [selectedProjectName, setSelectedProjectName] = useState("");
    const [selectedProjectOwner, setSelectedProjectOwner] = useState("");
    const [selectedProjectStars, setSelectedProjectStars] = useState(0);
    const [selectedProjectForks, setSelectedProjectForks] = useState(0);
    const [selectedProjectContributions, setSelectedProjectContributions] = useState(0);

    useEffect(() => {
        // Extract the organization id from the access token
        const organizationId = jwtDecode(accessToken).organizationId;
        let url = baseApiUrl + apiUrls.routes.dashboard.projectsInfo;

        // Replace the organization id in the URL
        url = url.replace(":organizationId", organizationId);
        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        };

        axios.get(url, {headers: headers})
            .then((response) => {
                let data = response.data;
                setSubmittedProjects(data);
                getRandomProject(data);
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });
    }, [accessToken]);

    function getRandomProject(data) {
        const randomIndex = Math.floor(Math.random() * data.length);
        const randomProject = data[randomIndex];
        setSelectedProjectName(randomProject.name);
        setSelectedProjectOwner(randomProject.owner);
        setSelectedProjectStars(randomProject.stars);
        setSelectedProjectForks(randomProject.forks);
        setSelectedProjectContributions(randomProject.totalContributions);
    }

    // Every 10 seconds, get a new random project
    useEffect(() => {
        const interval = setInterval(() => {
            getRandomProject(submittedProjects);
        }, 10000);

        return () => clearInterval(interval);
    }, []);


    return (
        <div className="dashboard-card"
             id={"submittedProjects"}
             style={{gridArea: "submittedProjects"}}
        >
            {error &&
                <ErrorModal
                    modalTitle={errorTitle}
                    modalAlertMessage={errorMessage}
                />
            }

            <h3>
                <i className={"bi bi-journal-text"}> </i>
                Submitted Projects
            </h3>
            <div className="submitted-project-content">
                <div className="submitted-project-img">
                    <i className="bi bi-github"> </i>
                </div>
                <div className="submitted-project-details">
                    <h4>{selectedProjectOwner + "/" + selectedProjectName}</h4>
                    <section className="submitted-project-details-icons">
                        <ProjectDetailsIcon
                            icon={starIcon}
                            title={selectedProjectStars}
                            caption="Stars"
                        />

                        <ProjectDetailsIcon
                            icon={contributionsIcon}
                            title={selectedProjectForks}
                            caption="Forks"
                        />

                        <ProjectDetailsIcon
                            icon={contributionsIcon}
                            title={selectedProjectContributions}
                            caption="Contributions"
                        />
                    </section>
                </div>
            </div>
        </div>
    )
}

export default SubmittedProjectsCard;
