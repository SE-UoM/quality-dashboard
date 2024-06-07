import './TotalTechDebtCard.css'
import React, {useEffect, useState} from "react";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import {formatText} from "../../../../../utils/textUtils.ts";

function TotalTechDebtCard({totalTechDebt, loading, tdPerLine, tdPerProject}) {

    return (
        <>
            {loading ? (
                    <SimpleDashboardCard
                        className="skeleton"
                        id={"totalTD"}
                        style={{height: "100%"}}
                    />
                ):
                <>
                    <div
                        className="stats shadow bg-base-200"
                        id="totalTD"
                        style={{
                            gridArea: "techDebt",
                            overflow: "hidden",
                        }}
                    >

                        <div className="stat place-items-center">
                            <div className="stat-title">
                                {/*<i className="bi bi-cash-coin"> </i>*/}
                                Total Tech Debt
                            </div>
                            <div className="stat-value">&euro; {formatText(totalTechDebt, "k")}</div>
                            <div className="stat-desc pt-1">
                                Since Last Analysis
                            </div>
                        </div>

                        <div className="stat place-items-center">
                            <div className="stat-title">Tech Debt per Line</div>
                            <div className="stat-value">{tdPerLine.toFixed(2)}'</div>
                            <div className="stat-desc pt-1">
                                Average in TD per Line of Code
                            </div>
                        </div>

                        <div className="stat place-items-center">
                            <div className="stat-title">Tech Debt per Project</div>
                            <div className="stat-value">{tdPerProject.toFixed(2)}'</div>
                            <div className="stat-desc">
                                Average in TD per Project
                            </div>
                        </div>

                    </div>
                </>
            }
        </>
      );
}

export default TotalTechDebtCard;