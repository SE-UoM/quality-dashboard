import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import {formatText} from "../../../../../utils/textUtils.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import CountIcon from "../../ui/CountIcon.tsx";

const GH_TOKEN =  import.meta.env.VITE_GITHUB_TOKEN;

function MostActiveDeveloperCard({userUrl, userImg, username, commitsCount, issuesCount, issuesPerContibution, gridArea, loading}) {
    const [userLocation, setUserLocation] = useState("");


    useEffect(() => {
        async function getUserData(username: string) {
            const apiUrl = `https://api.github.com/users/${username}`;
            let headers = {
                'Authorization': `Bearer ${GH_TOKEN}`,
            }

            let res = await axios.get(apiUrl, {headers: headers});

            return await res.data;
        }

        getUserData(username).then((data) => {
            setUserLocation(data.location)
        }).catch((error) => {
        })
    }, [loading]);


    return (
        <>
            {loading ? (
                <SimpleDashboardCard
                    id={"itemActivity"}
                    style={{gridArea: gridArea}}
                    className={"skeleton"}
                />
            ) : (
                <SimpleDashboardCard
                    id={"itemActivity"}
                    style={{
                        gridArea: gridArea,
                        width: "100%",
                        height: "100%",
                        padding: "0",
                        display: "flex",
                        flexDirection: "column",
                    }}
                    className={"card"}
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
                        <i className="bi bi-person-workspace"> </i>
                        Most Active Developer
                    </h3>

                    <div className="card-content"
                         style={{
                             display: "flex",
                             flexDirection: "row",
                             gap: "1em",
                             alignItems: "center",
                             justifyContent: "space-between",
                             width: "100%",
                             height: "80%",
                             padding: "0 1vh 1vh 1vh"
                         }}
                    >

                        <a className="avatar indicator">
                            <span className="indicator-item indicator-top badge badge-base-400 glass"
                                 style={{
                                     width: "4vh",
                                     height: "4vh",
                                     backgroundColor: "var(--hot-lava)",
                                     border: "none",
                                     marginTop: "2vh",
                                     marginRight: "1.5vh",
                                     fontSize: "2.5vh",
                                     color: "whitesmoke",
                                 }}
                            >
                                <i className="bi bi-fire"> </i>
                            </span>
                            <div className="mask mask-squircle"  style={{width: "17vh", height: "17vh"}}>
                                <img src={userImg}/>
                            </div>
                        </a>

                        <div style={{
                            width: "100%",
                        }}>
                            <h3 className={"name"} style={{fontSize: "3.5vh", alignSelf: "flex-start" }}>
                                <a href={userUrl} rel={"noreferrer"} target={"_blank"}>
                                    {username}
                                </a>
                            </h3>
                            <p style={{fontSize: "2.3vh"}}>
                                <i className="bi bi-geo-alt-fill"> </i>
                                {userLocation ? userLocation : "Unknown Location"}
                            </p>

                            <div
                                style={{
                                    display: "flex",
                                    flexDirection: "row",
                                    height: "100%",
                                    width: "100%",
                                    gap: "5vh",
                                    marginTop: "1vh",
                                    marginBottom: "1vh",
                                }}
                            >
                                <CountIcon icon={"bi bi-bezier2"} title={"Commits"} count={commitsCount} />
                                <CountIcon icon={"bi bi-bug"} title={"Issues"} count={issuesCount} />
                                <CountIcon icon={"bi bi-speedometer"} title={"Issues per Commit"} count={issuesPerContibution.toFixed(2)} />
                            </div>
                        </div>
                    </div>
                </SimpleDashboardCard>
            )}
        </>
    );
}

export default MostActiveDeveloperCard;