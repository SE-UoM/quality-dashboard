import {useLocation, useNavigate} from "react-router-dom";

export default function AdminPanelSidebar({totalPendingProjectsCount}) {
    const navigation = useNavigate()

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
                                    {totalPendingProjectsCount > 0 &&
                                        <span className="badge badge-error">
                                            {totalPendingProjectsCount > 99 ? "99+" : totalPendingProjectsCount}
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