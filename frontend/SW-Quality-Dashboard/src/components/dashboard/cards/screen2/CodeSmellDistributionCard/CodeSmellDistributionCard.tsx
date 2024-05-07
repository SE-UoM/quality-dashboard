import React from 'react';
import './CodeSmellDistributionCard.css';
import apiRoutes from '../../../../../assets/data/api_urls.json';
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import { jwtDecode } from "jwt-decode";
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import DashboardDonutChart from "../../../../charts/DashboardDonutChart.tsx";
import {formatText} from "../../../../../utils/textUtils.ts";
import {fetchCodeSmellDistribution} from "../../../../../utils/backend/backendUtils..ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function CodeSmellDistributionCard({loading, chartData, chartColors, totalCodeSmells}) {
    return (
        <>
                {!loading ? (
                    <>
                        <SimpleDashboardCard id="codeSmellDistribution">
                            <div className="language-distribution-container">
                                <h3>
                                    <i className="bi bi-radioactive"> </i>
                                    Code Smell Distribution
                                </h3>

                                <div className="lang-distribution-chart">
                                    <DashboardDonutChart
                                        data={chartData}
                                        colors={chartColors}
                                        centerLabel={formatText(totalCodeSmells, "k")}
                                    />
                                </div>
                            </div>
                        </SimpleDashboardCard>
                    </>
                ) : (
                    <SimpleDashboardCard
                        className="skeleton"
                        id={"codeSmellDistribution"}
                        style={{height: "100%"}}
                    />
                )}

        </>
    )
}

export default CodeSmellDistributionCard;
