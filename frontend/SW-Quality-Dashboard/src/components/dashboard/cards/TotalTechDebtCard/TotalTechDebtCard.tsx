import './TotalTechDebtCard.css'
import React, {useEffect, useState} from "react";
import useLocalStorage from "../../../../hooks/useLocalStorage.ts";
import ErrorModal from "../../../modals/ErrorModal/ErrorModal.tsx";
import apiUrls from "../../../../assets/data/api_urls.json";
import axios from "axios";
import {jwtDecode} from "jwt-decode";

let baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function TotalTechDebtCard() {
    const [totalTechDebt, setTotalTechDebt] = useState(0);
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [error, setError] = React.useState(false);
    const [errorTitle, setErrorTitle] = React.useState("");
    const [errorMessage, setErrorMessage] = React.useState("");

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
                console.log(response.data);
                setTotalTechDebt(response.data.totalTechDebtCost);
            })
            .catch(error => {
                console.log("Error fetching total technical debt data: ", error);
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

            <div className="dashboard-card"
                 id="totalTD"
            >
                <section className="total-td-card-header">
                    <i className="bi bi-cash-coin"> </i>
                    Total Technical Debt
                </section>

                <h2 className="td-value">
                    <i className={"bi bi-currency-euro"}> </i>
                    {totalTechDebt}
                </h2>
            </div>
        </>
      );
}

export default TotalTechDebtCard;