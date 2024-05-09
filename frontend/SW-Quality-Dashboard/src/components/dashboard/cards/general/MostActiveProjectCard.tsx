import SimpleDashboardCard from "../SimpleDashboardCard.tsx";
import {truncateString} from "../../../../utils/textUtils.ts";
import CountIcon from "../ui/CountIcon.tsx";

export default function MostActiveProjectCard() {
    return (
        <>
            <SimpleDashboardCard
                id={"mostActiveProj"}
                style={{
                    gridArea: "mostActiveProj",
                    padding: "0",
                    width: "100%",
                    height: "100%",
                    display: "flex",
                    flexDirection: "column",
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
                    <i className="bi bi-rocket-takeoff"> </i>
                    Most Active Project
                </h3>
                <div className="card-content"
                     style={{
                         display: "flex",
                         flexDirection: "column",
                         gap: "1em",
                         alignItems: "center",
                         justifyContent: "space-between",
                         width: "100%",
                         height: "80%",
                         padding: "0 1vh 1vh 1vh"
                     }}
                >
                    <div className="project-info"
                         style={{
                             display: "flex",
                             flexDirection: "row",
                             gap: "1em",
                             alignItems: "center",
                         }}
                    >
                        <a className="avatar indicator">
                            <div className="mask mask-squircle"  style={{width: "10vh", height: "10vh"}}>
                                <img src="https://avatars.githubusercontent.com/u/77233507?v=4"/>
                            </div>
                        </a>

                        <div className="project-details">
                            <h4
                                style={{
                                    fontSize: "2.5vh",
                                    fontWeight: "bold",
                                    margin: "0"
                                }}
                            >
                                JohnDoe/ProjectName &nbsp;
                                <div className="badge badge-outline"
                                     style={{
                                         fontSize: "1.5vh",
                                         padding: "0.5vh 1vh",
                                     }}
                                >
                                    <i className="bi bi-github"> </i> &nbsp; master
                                </div>
                            </h4>
                            <p
                                style={{fontSize: "2vh"}}
                            >
                                {truncateString("lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum ", 110)}
                            </p>
                        </div>
                    </div>
                    <div className="project-stats"
                         style={{
                             display: "flex",
                             flexDirection: "row",
                             height: "100%",
                             width: "100%",
                             gap: "5vh",
                             marginBottom: "5vh",
                             justifyContent: "space-around",
                         }}
                    >
                        <CountIcon icon={"bi bi-star"} title={"Stars"} count={10} />
                        <CountIcon icon={"bi bi-record-circle"} title={"Issues"} count={10} />
                        <CountIcon icon={"bi bi-bezier2"} title={"Commits"} count={10} />
                        <CountIcon icon={"bi bi-eye"} title={"Watchers"} count={10} />
                    </div>
                </div>
            </SimpleDashboardCard>
        </>
    )
}