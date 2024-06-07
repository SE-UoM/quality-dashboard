import {formatText} from "../../../../utils/textUtils.ts";
import SimpleDashboardCard from "../SimpleDashboardCard.tsx";

export default function DeveloperInfoCard({ developer, developerLoading }) {
    return (
        <>
            {developerLoading ? (
                <SimpleDashboardCard
                    className={"skeleton"}
                    style={{ gridArea: "developersSlides" }}
                />
            ) :(
                <SimpleDashboardCard
                    id={"developers"}
                    style={{
                        gridArea: "developersSlides",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        flexDirection: "row",
                        gap: "2vw"
                    }}
                >
                    <div
                        style={{
                            display: "flex",
                            flexDirection: "column",
                            gap: "1vh",
                            width: "100%",
                            padding: "2vh",
                            alignItems: "center",
                            justifyContent: "center"
                        }}
                    >
                        <div className="avatar">
                            <div className="mask mask-squircle" style={{height: "30vh"}}>
                                <img src={developer.avatarUrl ? developer.avatarUrl : "https://avatars.githubusercontent.com/u/77233507?v=4"}/>
                            </div>
                        </div>
                        <div className="dev-info">
                            <h5
                                style={{
                                    fontSize: "5vh",
                                    fontWeight: "bold",
                                    textAlign: "center",
                                    fontStyle: "italic"
                                }}
                            >
                                @{developer.name ? developer.name : "ArchontisKostis"}
                            </h5>

                            {/*<p style={{fontStyle: "italic", textAlign: "center"}}>*/}
                            {/*    <i className="bi bi-geo-alt-fill"> </i>*/}
                            {/*    {developer.location}*/}
                            {/*    /!*Hi there stranger! My name is Archontis Kostis. I am a Computer Science student at University of Macedonia - Applied Informatics.*!/*/}
                            {/*</p>*/}

                            {/*<div*/}
                            {/*    style={{*/}
                            {/*        display: "flex",*/}
                            {/*        flexDirection: "row",*/}
                            {/*        gap: "1vw",*/}
                            {/*        width: "100%",*/}
                            {/*    }}*/}
                            {/*>*/}
                            {/*    <div className="card bg-base-100 mt-5" style={{padding: "2vh 4vh"}}>*/}
                            {/*        <div className="card-body p-0"*/}
                            {/*             style={{*/}
                            {/*                 display: "flex",*/}
                            {/*                 justifyContent: "center",*/}
                            {/*                 alignItems: "center"*/}
                            {/*             }}*/}
                            {/*        >*/}
                            {/*                        <span style={{*/}
                            {/*                            fontSize: "5vh"*/}
                            {/*                        }}>*/}
                            {/*                            {formatText(1000, "k")}*/}
                            {/*                        </span>*/}

                            {/*            <span style={{*/}
                            {/*                fontWeight: "bold",*/}
                            {/*            }}>*/}
                            {/*                            Contributions*/}
                            {/*                        </span>*/}
                            {/*        </div>*/}
                            {/*    </div>*/}

                            {/*    <div className="card bg-base-100 mt-5" style={{padding: "2vh 4vh"}}>*/}
                            {/*        <div className="card-body p-0"*/}
                            {/*             style={{*/}
                            {/*                 display: "flex",*/}
                            {/*                 justifyContent: "center",*/}
                            {/*                 alignItems: "center"*/}
                            {/*             }}*/}
                            {/*        >*/}
                            {/*                        <span style={{*/}
                            {/*                            fontSize: "5vh"*/}
                            {/*                        }}>*/}
                            {/*                            {formatText(1000, "k")}*/}
                            {/*                        </span>*/}

                            {/*            <span style={{*/}
                            {/*                fontWeight: "bold",*/}
                            {/*            }}>*/}
                            {/*                            Contributions*/}
                            {/*                        </span>*/}
                            {/*        </div>*/}
                            {/*    </div>*/}

                            {/*    <div className="card bg-base-100 mt-5" style={{padding: "2vh 4vh"}}>*/}
                            {/*        <div className="card-body p-0"*/}
                            {/*             style={{*/}
                            {/*                 display: "flex",*/}
                            {/*                 justifyContent: "center",*/}
                            {/*                 alignItems: "center"*/}
                            {/*             }}*/}
                            {/*        >*/}
                            {/*                        <span style={{*/}
                            {/*                            fontSize: "5vh"*/}
                            {/*                        }}>*/}
                            {/*                            {formatText(1000, "k")}*/}
                            {/*                        </span>*/}

                            {/*            <span style={{*/}
                            {/*                fontWeight: "bold",*/}
                            {/*            }}>*/}
                            {/*                            Contributions*/}
                            {/*                        </span>*/}
                            {/*        </div>*/}
                            {/*    </div>*/}
                            {/*</div>*/}

                        </div>
                    </div>
                </SimpleDashboardCard>
            )}
        </>
    )
}