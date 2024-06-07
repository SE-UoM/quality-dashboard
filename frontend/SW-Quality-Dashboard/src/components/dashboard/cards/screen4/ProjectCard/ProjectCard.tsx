import './ProjectCard.css'
import filesIcon from '../../../../../assets/svg/dashboardIcons/files_general_icon.svg'
import linesIcon from '../../../../../assets/svg/dashboardIcons/loc_icon.svg'
import codeSmellsIcon from '../../../../../assets/svg/dashboardIcons/code_smells_icon.svg'
import debtIcon from '../../../../../assets/svg/dashboardIcons/timer_icon.svg'
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import {truncateString} from "../../../../../utils/textUtils.ts";

function formatText(text) {
    let roundedNum;
    if (!isNaN(text) && parseInt(text) >= 1000) {
        const num = parseInt(text);
        roundedNum = Math.round(num / 100) / 10;
        return parseInt(roundedNum) + "k";
    }
    return text;
}

function ProjectCard({cardHeader, cardHeaderIcon, contentImage, id, projectName, nameSubText, totalFiles, totalLines, totalDebt, totalCodeSmells, totalStars, loading}) {
    return (
        <>
            {loading ? (
                <SimpleDashboardCard
                    id={id}
                    className={"skeleton"}
                />
                ) : (
                <SimpleDashboardCard
                    id={id}
                    style={{
                        paddingBottom: "2vh",
                    }}
                >
                    <h3 className="header"
                        style={{
                            width: "100%",
                            gap: "2vh",
                            padding: "1vh 2vh",
                            fontSize: "2vh",
                            fontWeight: "bold",
                            margin: "0"
                        }}
                    >
                        <i className={cardHeaderIcon}> </i>
                        {cardHeader}
                    </h3>

                    <div className="project-card-content"
                         style={{
                                display: "flex",
                                flexDirection: "row",
                                gap: "1vh",
                                alignItems: "center",
                                justifyContent: "space-around",
                                width: "100%",
                                height: "80%",
                         }}
                    >
                        <div style={{
                            backgroundImage: `url(${contentImage})`,
                            backgroundSize: "17vh 17vh",
                            backgroundPosition: "center",
                            backgroundRepeat: "no-repeat",
                            width: "17vh",
                            height: "17vh",
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            fontSize: "9vh",
                        }}>
                            <span className="text text-neutral" style={{paddingTop: "1vh", paddingRight: "0.5vh", fontWeight: "bold", opacity: "0.9"}}>
                                {totalStars}
                            </span>
                        </div>

                        <div className="project-card-content-details" style={{width: "70%"}}>
                            <h3>
                                <div className="project-info"
                                     style={{
                                         display: "flex",
                                         flexDirection: "row",
                                         gap: "1vh",
                                         alignItems: "center",
                                     }}
                                >
                                    <div className="project-details">
                                        <h4
                                            style={{
                                                fontSize: "2.5vh",
                                                fontWeight: "bold",
                                                margin: "0"
                                            }}
                                        >
                                            {
                                                truncateString(`${nameSubText}/${projectName}`, 30)
                                            }
                                            <div className="badge badge-neutral"
                                                 style={{
                                                     fontSize: "1.5vh",
                                                     padding: "0.5vh 1vh",
                                                     marginLeft: "1.5vh",
                                                 }}
                                            >
                                                <i className="bi bi-unlock" style={{marginRight: "0.5vh"}}></i>
                                                Public
                                            </div>
                                        </h4>
                                        <p
                                            style={{fontSize: "2vh"}}
                                        >
                                            {true ?
                                                truncateString("@" + nameSubText, 130) :
                                                "N/A"
                                            }
                                        </p>
                                    </div>
                                </div>
                            </h3>

                            <div className="project-card-content-details-icons">
                                <div className="project-card-content-details-icon">
                                    <i className="bi bi-file-earmark-binary" style={{fontSize: "5vh"}}> </i>
                                    <p>
                                        <span>{formatText(totalFiles) + " "} </span>
                                        Files
                                    </p>
                                </div>

                                <div className="project-card-content-details-icon">
                                    <i className="bi bi-text-left" style={{fontSize: "5vh"}}> </i>
                                    <p>
                                        <span>{formatText(totalLines) + " "}</span>
                                        Lines
                                    </p>
                                </div>

                                <div className="project-card-content-details-icon">
                                    <i className="bi bi-stopwatch" style={{fontSize: "5vh"}}> </i>
                                    <p>
                                        <span>{formatText(totalDebt)} </span>
                                        Debt
                                    </p>
                                </div>

                                <div className="project-card-content-details-icon">
                                    <i className="bi bi-bug" style={{fontSize: "5vh"}}> </i>
                                    <p>
                                        <span>{formatText(totalCodeSmells, "k")} </span>
                                        Code Smells
                                    </p>
                                </div>

                            </div>
                        </div>

                    </div>

                </SimpleDashboardCard>
            )}
        </>
    )
}

export default ProjectCard