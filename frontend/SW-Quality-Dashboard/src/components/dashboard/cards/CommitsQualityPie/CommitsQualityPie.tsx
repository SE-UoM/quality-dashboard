import DashboardDonutChart from "../../../charts/DashboardDonutChart.tsx";
import SimpleDashboardCard from "../SimpleDashboardCard.tsx";
import React, {useEffect} from "react";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import useAxiosGet from "../../../../hooks/useAxios.ts";
import apiUrls from "../../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";


const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function CommitsQualityPie({gridArea, orgID}) {
    const {data: commitsData, loading: commitsLoading, error: commitsError, errorMessage: commitsErrorMessage} =
        useAxiosGet(baseApiUrl + apiUrls.routes.dashboard.commitDetails.replace(":organizationId", orgID), "");

    const [commitQualityData, setCommitQualityData] = React.useState(null);

    // Effect to transform the commitsQuality data once commitsData is fetched
    useEffect(() => {
        if (commitsData && commitsData.commitsQuality) {
            const qualityData = commitsData.commitsQuality;

            // Transforming the data into the desired structure
            const formattedData = [
                { name: "EXCELLENT", value: qualityData.EXCELLENT, color: '#5fc828' },
                { name: "GOOD", value: qualityData.GOOD, color: "#3bb0d5" },
                { name: "FAIR", value: qualityData.FAIR, color: "#fbe83a" },
                { name: "POOR", value: qualityData.POOR, color: "#fe3839" },
                { name: "UNKNOWN", value: qualityData.UNKNOWN, color: "#a4abb6" },
            ];

            // Set the formatted data to the state
            setCommitQualityData(formattedData);
        }

    }, [commitsData]);

    return (
          <>
              {commitsLoading ? (
                  <SimpleDashboardCard
                      className="skeleton"
                      id={"commitQualityDistribution"}
                      style={{height: "100%", gridArea: gridArea}}
                  />
              ) : (
                  <SimpleDashboardCard
                      className=""
                      id={"commitQualityDistribution"}
                      style={{height: "100%", gridArea: "commitQualityDistribution"}}
                  >
                      <div className="language-distribution-container">
                          <h4 style={{width:"100%"}}>
                              <strong>Commits Quality</strong>
                          </h4>

                          <div
                            className="commmit-quality-distribution-chart"
                            style={{
                                height: "100%",
                                width: "100%",
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center",
                                gap: "1em"
                          }}
                          >
                              <DashboardDonutChart
                                  data={commitQualityData}
                                  colors={['#5fc828', '#3bb0d5', '#fbe83a', '#fe3839', '#a4abb6']}
                                  showLabel={true}
                                  labelStyle={`
                                        text.fill-tremor-content-emphasis {
                                            font-size: 5vh !important;
                                            font-weight: 600 !important;
                                            transform: translateY(+1vh);
                                        }
                                    `}
                              />
                          </div>
                      </div>
                  </SimpleDashboardCard>
              )}
          </>
      );
}

export default CommitsQualityPie;