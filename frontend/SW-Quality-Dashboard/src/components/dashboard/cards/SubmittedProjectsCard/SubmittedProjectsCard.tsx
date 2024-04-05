import './SubmittedProjectsCard.css'
import ProjectDetailsIcon from "../../../ui/ProjectDetailsIcon/ProjectDetailsIcon.tsx";
import starIcon from "../../../../assets/svg/dashboardIcons/github_stars_icon.svg";
import contributionsIcon from "../../../../assets/svg/dashboardIcons/contributions_icon.svg";


function SubmittedProjectsCard() {
    return (
        <div className="dashboard-card"
             id={"submittedProjects"}
             style={{gridArea: "submittedProjects"}}
        >
            <h3>
                <i className={"bi bi-journal-text"}> </i>
                Submitted Projects
            </h3>
            <div className="submitted-project-content">
                <div className="submitted-project-img">
                    <i className="bi bi-github"> </i>
                </div>
                <div className="submitted-project-details">
                    <h4>Project Name</h4>
                    <section className="submitted-project-details-icons">
                        <ProjectDetailsIcon
                            icon={starIcon}
                            title={100}
                            caption="Stars"
                        />

                        <ProjectDetailsIcon
                            icon={contributionsIcon}
                            title={100}
                            caption="Forks"
                        />

                        <ProjectDetailsIcon
                            icon={contributionsIcon}
                            title={100}
                            caption="Contributions"
                        />
                    </section>
                </div>
            </div>
        </div>
    )
}

export default SubmittedProjectsCard