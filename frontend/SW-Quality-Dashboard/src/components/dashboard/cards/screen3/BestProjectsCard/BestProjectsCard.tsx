import DashboardRankedItem from "../../../../ui/DashboardRankedItem/DashboardRankedItem.tsx";
import ScrollableRankCard from "../../ScrollableRankCard/ScrollableRankCard.tsx";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import {useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import apiUrls from "../../../../../assets/data/api_urls.json";
import axios from "axios";
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

export default function BestProjectsCard({truncateString}) {
    const [accessToken] = useLocalStorage("accessToken", "");
    const [error, setError] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loadingTopProjects, setLoadingTopProjects] = useState(true);
    const [bestProjects, setBestProjects] = useState([]);

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
                console.log(data)
            })
            .catch((error) => {
                setError(true);
                setErrorTitle("Error");
                setErrorMessage(error.response.data.message);
            });

    }, [accessToken]);

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
                >
                    {bestProjects.map((item, index) => {
                        let name = item.name;
                        let owner = item.owner;
                        let rank = index + 1;
                        return (
                            <DashboardRankedItem
                                key={index}
                                projectName={truncateString(name, 15)}
                                rank={rank}
                                loading={loadingTopProjects}
                                headerUrl={"https://github.com/" + name}
                            >
                                By:&nbsp;
                                <a
                                    href={"https://github.com/" + owner}
                                    target="_blank"
                                    rel="noreferrer"
                                    style={{
                                        color: "var(--text-secondary)",
                                    }}
                                >
                                    {owner}
                                </a>
                            </DashboardRankedItem>
                        )
                    })}
                </ScrollableRankCard>
            )}
        </>
    );
}