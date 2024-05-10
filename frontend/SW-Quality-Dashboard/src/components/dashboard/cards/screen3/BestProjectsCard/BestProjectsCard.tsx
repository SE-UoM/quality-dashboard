import DashboardRankedItem from "../../../../ui/DashboardRankedItem/DashboardRankedItem.tsx";
import ScrollableRankCard from "../../general/ScrollableRankCard/ScrollableRankCard.tsx";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import {useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import apiUrls from "../../../../../assets/data/api_urls.json";
import axios from "axios";
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import medalIcon from "../../../../../assets/svg/dashboardIcons/simple_medal_icon.svg";
import DashboardMedal from "../../ui/DashboardMedal.tsx";
import getRepoFromGithubAPI from "../../../../../utils/api/github.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

export default function BestProjectsCard({truncateString}) {
    const [accessToken] = useLocalStorage("accessToken", "");
    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loadingTopProjects, setLoadingTopProjects] = useState(true);
    const [bestProjects, setBestProjects] = useState([]);
    const [autoScrollBestProjects, setAutoScrollBestProjects] = useState(false);

    const [githubResponse, setGithubResponse] = useState(null);
    const [githubError, setGithubError] = useState(false);
    const [githubErrorMessage, setGithubErrorMessage] = useState("");
    const [loadingGithub, setLoadingGithub] = useState(true);

    useEffect(() => {
        // Extract the organization id from the access token
        const organizationId = jwtDecode(accessToken).organizationId;

        let url = baseApiUrl + apiUrls.routes.dashboard.topProjects;

        // Replace the organization id in the URL
        url = url.replace(":organizationId", organizationId);
        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        axios.get(url, {headers: headers})
            .then((response) => {
                let data = response.data;

                // Sort based on techDebtPerLoc
                data.sort((a, b) => {
                    return a.techDebtPerLoc - b.techDebtPerLoc;
                });

                // Wait half a second before setting the state
                setTimeout(() => {
                    setLoadingTopProjects(false);
                }, 1000);

                setBestProjects(data);
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });

    }, [accessToken]);

    useEffect(() => {
        // Function to fetch additional data from GitHub API for each project
        const fetchGithubData = async () => {
            const projectsWithGithubData = await Promise.all(
                bestProjects.map(async (project) => {
                    try {
                        const githubResponse = await getRepoFromGithubAPI(project.owner, project.name);
                        return { ...project, githubData: githubResponse.data };
                    } catch (error) {
                        console.error("Error fetching GitHub data:", error);
                        return project;
                    }
                })
            );
            setBestProjects(projectsWithGithubData);
        };

        // Fetch GitHub data only when bestProjects array changes
        if (bestProjects.length > 0) {
            fetchGithubData();
        }
    }, [bestProjects]);

    // Make the component auto scroll
    useEffect(() => {
        const scrollableRankCard = document.getElementById("Scrolling");
        const interval = setInterval(() => {
            if (scrollableRankCard && autoScrollBestProjects) {
                scrollableRankCard.scrollTop += 1;
            }

            // // If we scrolled to the bottom, reset the scroll
            // if (scrollableRankCard && scrollableRankCard.scrollHeight - scrollableRankCard.scrollTop === scrollableRankCard.clientHeight) {
            //     scrollableRankCard.scrollTop = 0;
            // }
        }, 50);

        return () => clearInterval(interval);
    }, [loadingGithub, autoScrollBestProjects]);

    return (
        <>
        {loadingTopProjects ? (
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
                                                {truncateString(project.name, 10)}
                                            </a>
                                            <div className="badge badge-accent" style={{margin: "1vh 1vh", fontSize: "1.5vh"}} >{project.githubData ? project.githubData.language : "No language"}</div>

                                            <p>{project.githubData && project.githubData.description  ? truncateString(project.githubData.description, 50) : "No description"}</p>
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