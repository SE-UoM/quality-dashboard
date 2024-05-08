import DashboardRankedItem from "../../../../ui/DashboardRankedItem/DashboardRankedItem.tsx";
import ScrollableRankCard from "../../general/ScrollableRankCard/ScrollableRankCard.tsx";
import React from "react";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import DashboardMedal from "../../ui/DashboardMedal.tsx";
import {truncateString} from "../../../../../utils/textUtils.ts";

export default function TopContibutorsCard({topContributors, loadingTopContributors}) {
    return (
        <>
        {loadingTopContributors ? (
                <SimpleDashboardCard
                    className="skeleton"
                    id={"scrollableRankCard"}
                    style={{height: "100%", gridArea: "topCommiters"}}
                />
            ) : (
                <ScrollableRankCard
                    title="Top Contributors"
                    icon="bi bi-person"
                    cardId="scrollableRankCard"
                    gridArea={"topCommiters"}
                >
                    {/*{topContributors.map((item, index) => {*/}
                    {/*    let name = item.name;*/}
                    {/*    let totalCommits = item.totalCommits;*/}
                    {/*    let rank = index + 1;*/}
                    {/*    return (*/}
                    {/*        <DashboardRankedItem*/}
                    {/*            key={index}*/}
                    {/*            projectName={name}*/}
                    {/*            rank={rank}*/}
                    {/*            loading={loadingTopContributors}*/}
                    {/*        >*/}
                    {/*            {"Contributions: " + totalCommits}*/}
                    {/*        </DashboardRankedItem>*/}
                    {/*    )*/}
                    {/*})}*/}

                    <div id="Scrolling">
                        <table className="table table-zebra">
                            {/* head */}
                            <thead></thead>
                            <tbody>

                            {topContributors.map((contributor, index) => (
                                <tr key={index}>
                                    <td
                                        style={{
                                            display: "flex",
                                            alignItems: "center",
                                            justifyContent: "center",
                                        }}
                                    >
                                        <a href={"https://github.com/" + contributor.name} className="avatar link shadow-sm">
                                            <div className="mask mask-squircle"
                                                    style={{
                                                        width: "8vh",
                                                        height: "8vh",
                                                    }}
                                            >
                                                <img src={`https://avatars.githubusercontent.com/${contributor.name}`} alt="Avatar" />
                                            </div>
                                        </a>
                                    </td>
                                    <td>
                                            <a
                                                href={"https:://github.com/" + contributor.name}
                                                className="tooltip tooltip-bottom"
                                                data-tip={contributor.name}
                                                style={{
                                                    fontSize: "3vh",
                                                    fontWeight: "bold",
                                                }}
                                            >
                                                {contributor.name ? contributor.name : "None"}
                                            </a>

                                        <div style={{
                                            display: "flex",
                                            gap: "1vh",
                                            fontSize: "2vh",
                                        }}>
                                            <p className="pt-1">
                                                Contributions: {contributor.totalCommits ? contributor.totalCommits : "None"}
                                            </p>
                                        </div>
                                    </td>
                                    {/*<td style={{*/}
                                    {/*    textAlign: "center",*/}
                                    {/*}}>*/}
                                    {/*    {contributor.totalCommits ? contributor.totalCommits : "None"}*/}
                                    {/*</td>*/}
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