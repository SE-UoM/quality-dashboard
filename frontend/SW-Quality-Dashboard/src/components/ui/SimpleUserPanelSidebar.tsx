import {useLocation, useNavigate} from "react-router-dom";
import useAxios from "../../hooks/useAxios.ts";
import useAxiosGet from "../../hooks/useAxios.ts";
import useLocalStorage from "../../hooks/useLocalStorage.ts";
import apiUrls from '../../assets/data/api_urls.json'
import {jwtDecode} from "jwt-decode";
import {useEffect} from "react";
import {Simulate} from "react-dom/test-utils";
import error = Simulate.error;

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

export default function AdminPanelSidebar() {
    const navigation = useNavigate()
    const [accessToken] = useLocalStorage('accessToken', '')

    const {data: totalPendingData, error: totalPendingError, loading: totalPendingLoading, errorMessage: totalPendingErrorMsg} =
        useAxiosGet(baseApiUrl + apiUrls.routes.projects.getTotalPending.replace(':organizationId', jwtDecode(accessToken).organizationId), accessToken);

    const handleItemClick = (page) => {
        navigation(`/admin?page=${page}`)
    };

    return (
        <>
            <ul className="menu bg-base-200"
                style={{
                    display: "flex",
                    flexDirection: "column",
                    width: "15vw"
                }}
            >
                <li>
                    <button onClick={() => handleItemClick("home")}>
                        <i className="bi bi-house"></i>
                        Home
                    </button>
                </li>
                <li>
                    <button onClick={() => handleItemClick("dashboardUsers")}>
                        <i className="bi bi-people"></i>
                        Dashboard Users
                    </button>
                </li>
                <li>
                    <details>
                        <summary>
                            <i className="bi bi-journal-code"></i>
                            Projects
                        </summary>
                        <ul>
                            <li>
                                <button onClick={() => handleItemClick("submittedProjects")}>
                                    <i className="bi bi-layer-forward"></i>
                                    Submitted Projects
                                </button>
                            </li>

                            <li>
                                <button onClick={() => handleItemClick("pendingProjects")}>
                                    <i className="bi bi-exclamation-circle"></i>
                                    Pending
                                    {totalPendingData && totalPendingData.totalPending > 0 &&
                                        <span className="badge badge-error">
                                            {totalPendingData.totalPending > 99 ? "99+" : totalPendingData.totalPending}
                                        </span>
                                    }
                                </button>
                            </li>
                        </ul>
                    </details>
                </li>
            </ul>
        </>
    )
}