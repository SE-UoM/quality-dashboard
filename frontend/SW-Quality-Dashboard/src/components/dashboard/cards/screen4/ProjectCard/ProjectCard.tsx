import './ProjectCard.css'
import filesIcon from '../../../../../assets/svg/dashboardIcons/files_general_icon.svg'
import linesIcon from '../../../../../assets/svg/dashboardIcons/loc_icon.svg'
import codeSmellsIcon from '../../../../../assets/svg/dashboardIcons/code_smells_icon.svg'
import debtIcon from '../../../../../assets/svg/dashboardIcons/timer_icon.svg'

function formatText(text) {
    let roundedNum;
    if (!isNaN(text) && parseInt(text) >= 1000) {
        const num = parseInt(text);
        roundedNum = Math.round(num / 100) / 10;
        return parseInt(roundedNum) + "k";
    }
    return text;
}

function ProjectCard({cardHeader, cardHeaderIcon, contentImage, id, projectName, nameSubText, totalFiles, totalLines, totalDebt, totalCodeSmells}) {
    return (
        <div className="dashboard-card project-card"
             id={id}
        >
            <h3 className="header">
                <i className={cardHeaderIcon}> </i>
                {cardHeader}
            </h3>

            <div className="project-card-content">
                <img src={contentImage}/>

                <div className="project-card-content-details">
                    <h3>
                        {projectName}
                        <span>
                            {nameSubText}
                        </span>
                    </h3>

                    <div className="project-card-content-details-icons">
                        <div className="project-card-content-details-icon">
                            <img src={filesIcon} />
                            <p>
                                <span>{formatText(totalFiles) + " "} </span>
                                Files
                            </p>
                        </div>

                        <div className="project-card-content-details-icon">
                            <img src={linesIcon} />
                            <p>
                                <span>{formatText(totalLines) + " "}</span>
                                Lines
                            </p>
                        </div>

                        <div className="project-card-content-details-icon">
                            <img src={debtIcon} />
                            <p>
                                <span>{formatText(totalDebt) + " \'"} </span>
                                Debt
                            </p>
                        </div>

                        <div className="project-card-content-details-icon">
                            <img src={formatText(codeSmellsIcon)} />
                            <p>
                                <span>{totalCodeSmells + " "} </span>
                                Code Smells
                            </p>
                        </div>

                    </div>
                </div>

            </div>

        </div>
    )
}

export default ProjectCard