import './TotalTechDebtCard.css'
import React, {useEffect, useState} from "react";
import useLocalStorage from "../../../../../hooks/useLocalStorage.ts";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import apiUrls from "../../../../../assets/data/api_urls.json";
import axios from "axios";
import {jwtDecode} from "jwt-decode";

let baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function formatText(text) {
    let roundedNum;
    if (!isNaN(text) && parseInt(text) > 1000) {
        const num = parseInt(text);
        roundedNum = Math.round(num / 100) / 10;
        return roundedNum.toFixed(2) + "k";
    }
    return text.toFixed(2);
}

function TotalTechDebtCard() {
    const [totalTechDebt, setTotalTechDebt] = useState(0);
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [error, setError] = React.useState(false);
    const [errorTitle, setErrorTitle] = React.useState("");
    const [errorMessage, setErrorMessage] = React.useState("");
    const [loading, setLoading] = React.useState(true);

    // Call the API to get the total technical debt
    useEffect(() => {
        let url = baseApiUrl + apiUrls.routes.dashboard.getTotalTD;

        // Extract the organization ID from the access token
        let decodedToken = jwtDecode(accessToken);
        let orgId = decodedToken.organizationId;

        // Replace :orgId with the organization ID
        url = url.replace(":orgId", orgId);
        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.get(url, { headers: headers })
            .then(response => {
                console.info("Total Tech Debt API Data: " + response.data);

                // Wait half a second before setting the state
                setTimeout(() => {
                    setLoading(false);
                }, 500);

                setTotalTechDebt(response.data.totalTechDebtCost);
            })
            .catch(error => {
                console.warn("Error fetching total technical debt data: ", error);
                setError(true);
                setErrorTitle("Error fetching total technical debt data");
                setErrorMessage("An error occurred while fetching the total technical debt data of the organization. Please try again later.");
            });
    }, [accessToken]);

    return (
        <>
            {error &&
                <ErrorModal
                    modalTitle={errorTitle}
                    modalAlertMessage={errorMessage}
                />
            }

            <div className={"dashboard-card card bg-base-200 " + (loading ? "loading" : "")}
                 id="totalTD"
            >
                {loading ? (
                        <div className="total-td-skeleton">
                            <div className="skeleton-box"> </div>
                            <div className="skeleton-box"> </div>
                        </div>
                    ):
                    <>
                        <section className="total-td-card-header">
                            <i className="bi bi-cash-coin"> </i>
                            Total Technical Debt
                        </section>

                        <h2 className="td-value">
                            <i className={"bi bi-currency-euro"}> </i>
                            {formatText(totalTechDebt)}
                        </h2>
                    </>
                }
            </div>
        </>
      );
}

export default TotalTechDebtCard;