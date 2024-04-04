import './BestProjectsCard.css'
import DashboardProjectComponent from "../../../ui/DashboardProjectComponent/DashboardProjectComponent.tsx";

function BestProjectsCard() {
    return (
        <div className="dashboard-card"
             id={"bestProjects"}
        >
            <h3>
                <i className="bi bi-bookmark-star"> </i>
                Best Projects
            </h3>

            <div className="best-projects-list">
                {
                //     Display 20 Dummy Projects

                    Array.from({length: 20}, (_, i) => i + 1).map((_, index) => {
                        return (
                            <DashboardProjectComponent
                                projectName={"Project " + (index + 1)}
                                owner={"Owner " + (index + 1)}
                                rank={index + 1}
                            />
                        )
                    })
                }
            </div>
        </div>
    )
}

export default BestProjectsCard