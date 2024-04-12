import './ProjectDetailsIcon.css'

function ProjectDetailsIcon({icon, title, caption}) {
    return (
        <div className="project-details-icon">
            <div className="project-details-icon-img">
                <img src={icon}/>
            </div>

            <div className="project-details-icon-details">
                <h4>{title}</h4>
                <p>{caption}</p>
            </div>
        </div>
    );
}

export default ProjectDetailsIcon;