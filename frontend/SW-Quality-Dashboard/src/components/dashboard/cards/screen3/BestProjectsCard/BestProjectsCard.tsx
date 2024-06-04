import ScrollableRankCard from "../../general/ScrollableRankCard/ScrollableRankCard.tsx";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import {useEffect, useState} from "react";
import DashboardMedal from "../../ui/DashboardMedal.tsx";
import {truncateString} from "../../../../../utils/textUtils.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

export default function BestProjectsCard({bestProjectsData, loading}) {
    const [bestProjects, setBestProjects] = useState([]);
    const [autoScrollBestProjects, setAutoScrollBestProjects] = useState(true);

    useEffect(() => {
        if (!bestProjectsData) return;
        bestProjectsData.sort((a, b) => {
            return a.techDebtPerLoc - b.techDebtPerLoc;
        });

        setBestProjects(bestProjectsData);
    }, [bestProjectsData]);

    return (
        <>
        {loading ? (
                <SimpleDashboardCard
                    className="skeleton"
                    id={"scrollableRankCard"}
                    style={{height: "100%", gridArea: "bestProjects"}}
                />
            ) : (
                <ScrollableRankCard
                    title="Best Projects"
                    icon="bi bi-bookmark-star"
                    cardId="scrollableRankCard"
                    gridArea={"bestProjects"}
                    autoscroll={autoScrollBestProjects}
                    setAutoScroll={setAutoScrollBestProjects}
                >
                    <div id="Scrolling">
                        <table className="table">
                            {/* head */}
                            <thead>
                            <tr>
                                <th style={{textAlign: "center"}}>Rank</th>
                                <th>
                                    Project
                                </th>
                                <th>
                                    Owner
                                </th>
                            </tr>
                            </thead>
                            <tbody>

                            {bestProjects.map((project, index) => (
                                <tr key={index}>
                                    <td>
                                        <DashboardMedal
                                            rank={index + 1}
                                            medalClass={index === 0 ? "medal--gold" : index === 1 ? "medal--silver" : index === 2 ? "medal--bronze" : "medal--none"}
                                        />
                                    </td>
                                    <td>
                                        <div>
                                            <a style={{fontSize: "2.5vh", fontWeight: "bold"}} href={`https://github.com/${project.owner}/${project.name}`} className="tooltip tooltip-top" data-tip={project.owner + "/" + project.name}>
                                                {truncateString(project.name, 11)}
                                            </a>
                                            <div className="badge badge-accent" style={{margin: "1vh 1vh", fontSize: "1.5vh"}} >{project.githubData ? project.githubData.language : "No language"}</div>

                                            {/*<p>{project.githubData && project.githubData.description  ? truncateString(project.githubData.description, 50) : "No description"}</p>*/}
                                        </div>
                                    </td>
                                    <td style={{
                                        display: "flex",
                                        alignItems: "center",
                                    }}>
                                        <a href={"https://github.com/" + project.owner} className="avatar link shadow-sm">
                                            <div className="mask mask-squircle w-12 h-12">
                                                <img src={`https://avatars.githubusercontent.com/${project.owner}`} alt="Avatar" />
                                            </div>
                                        </a>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </ScrollableRankCard>
            )}
        </>
    );
}