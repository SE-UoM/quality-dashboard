import './TechDebtStatsCard.css'
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiRoutes from '../../../../../assets/data/api_urls.json';
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function TechDebtStatsCard({loading, minTechDebt, maxTechDebt}) {

    return (
        <>
                {loading ? (
                    <>
                        <SimpleDashboardCard
                            className="skeleton"
                            id={"techDebtStats"}
                            style={{height: "100%"}}
                        />
                    </>
                    ):
                    <>
                        <div className="stats shadow bg-base-200" id={"techDebtStats"}
                             style={{
                                 gridArea: "techDebtStats",
                                 overflow: "hidden",
                             }}
                        >
                            <div className="stat">
                                <div className="stat-figure text-secondary">
                                    <div className="avatar online">
                                        <div className="w-16 rounded-full">
                                            <img src="https://img.daisyui.com/images/stock/photo-1534528741775-53994a69daeb.jpg" />
                                        </div>
                                    </div>
                                </div>
                                <div className="stat-value">86%</div>
                                <div className="stat-title">Tasks done</div>
                                <div className="stat-desc text-secondary">31 tasks remaining</div>
                            </div>

                            <div className="stat">
                                <div className="stat-figure text-primary">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-8 h-8 stroke-current"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path></svg>
                                </div>
                                <div className="stat-title">Total Likes</div>
                                <div className="stat-value text-primary">25.6K</div>
                                <div className="stat-desc">21% more than last month</div>
                            </div>

                            <div className="stat">
                                <div className="stat-figure text-secondary">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-8 h-8 stroke-current"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
                                </div>
                                <div className="stat-title">Page Views</div>
                                <div className="stat-value text-secondary">2.6M</div>
                                <div className="stat-desc">21% more than last month</div>
                            </div>

                        </div>
                        {/*<SimpleDashboardCard id="techDebtStats">*/}
                        {/*<h3>*/}
                        {/*    <i className={"bi bi-bar-chart"}> </i>*/}
                        {/*    Technical Debt Statistics*/}
                        {/*</h3>*/}

                        {/*<section className="td-stats-container">*/}
                        {/*    <p className={"td-stat-item"}>*/}
                        {/*        <strong>*/}
                        {/*            <i className="bi bi-stopwatch"> </i>*/}
                        {/*            Average Debt:*/}
                        {/*        </strong>*/}
                        {/*        {" " + parseFloat(averageProjectTechDebt.toFixed(2))} '*/}
                        {/*    </p>*/}

                        {/*    <p className={"td-stat-item"}>*/}
                        {/*        <strong>*/}
                        {/*            <i className="bi bi-piggy-bank"> </i>*/}
                        {/*            Max Debt per Line:*/}
                        {/*        </strong>*/}
                        {/*        {" " +  parseFloat(maxTechDebt.toFixed(2))} <i className={"bi bi-currency-euro"}> </i>*/}
                        {/*    </p>*/}

                        {/*    <p className={"td-stat-item"}>*/}
                        {/*        <strong>*/}
                        {/*            <i className="bi bi-stopwatch"> </i>*/}
                        {/*            Debt per Line:*/}
                        {/*        </strong>*/}
                        {/*        {" " + parseFloat(averageTechDebtPerLineOfCode.toFixed(2))} '*/}
                        {/*    </p>*/}

                        {/*    <p className={"td-stat-item"}>*/}
                        {/*        <strong>*/}
                        {/*            <i className="bi bi-piggy-bank"> </i>*/}
                        {/*            Min Debt per Line:*/}
                        {/*        </strong>*/}
                        {/*        {" " + parseFloat(minTechDebt.toFixed(2))} <i className={"bi bi-currency-euro"}> </i>*/}
                        {/*    </p>*/}
                        {/*</section>*/}
                        {/*</SimpleDashboardCard>*/}
                    </>
            }
        </>
    );
}

export default TechDebtStatsCard;