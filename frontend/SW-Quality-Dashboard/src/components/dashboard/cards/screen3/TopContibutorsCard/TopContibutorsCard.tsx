import DashboardRankedItem from "../../../../ui/DashboardRankedItem/DashboardRankedItem.tsx";
import ScrollableRankCard from "../../general/ScrollableRankCard/ScrollableRankCard.tsx";
import React from "react";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

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
                    {topContributors.map((item, index) => {
                        let name = item.name;
                        let totalCommits = item.totalCommits;
                        let rank = index + 1;
                        return (
                            <DashboardRankedItem
                                key={index}
                                projectName={name}
                                rank={rank}
                                loading={loadingTopContributors}
                            >
                                {"Contributions: " + totalCommits}
                            </DashboardRankedItem>
                        )
                    })}
                </ScrollableRankCard>
            )}
    </>
    );
}