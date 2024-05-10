import './AdminPanel.css';
import {useEffect, useState} from "react";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import {jwtDecode} from "jwt-decode";
import DecodedToken from "../../../interfaces/DecodedToken.ts";
import ProtectedRoute from "../../../routes/ProtectedRoute.tsx";
import {useLocation, useNavigate} from "react-router-dom";
import AdminPanelSidebar from "../../../components/ui/AdminPanelSidebar.tsx";

function AdminPanel() {
    const [accessToken] = useLocalStorage<string>('accessToken', '');
    const [isAdmin, setIsAdmin] = useState<boolean>(false)
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const location = useLocation()
    const queryParams = new URLSearchParams(location.search)
    const page = queryParams.get('page')
    const [currentPage, setCurrentPage] = useState<string>(page ? page : 'home')
    const history = useNavigate()

    const [pendingProjectsCount, setPendingProjectsCount] = useState<number>(0)
    const [loadingAuth, setLoadingAuth] = useState<boolean>(true)

    // Update currentPage when the URL changes
    useEffect(() => {
        setCurrentPage(page);
    }, [page]);

    // Redirect to home page if no current page is specified
    useEffect(() => {
        if (!currentPage) {
            history('/admin?page=home')
        }
    }, [currentPage, history]);

    useEffect(() => {
        // Call a random API to check if the token is still valid

        // Decode the token to check if the user is an admin
        let decoded : DecodedToken = jwtDecode(accessToken)
        if (!decoded) {
            setIsAdmin(false)
            window.location.href = '/login'
            return;
        }

        let isAdmin = decoded.roles.includes('PRIVILEGED')

        if (!isAdmin) {
            setIsAdmin(false)
            window.location.href = '/'
            return;
        }

        setIsAdmin(isAdmin)
    }, [isAuthenticated, accessToken])

    return (
        <>
            <ProtectedRoute loadingAuth={loadingAuth} setLoadingAuth={setLoadingAuth} />

            {loadingAuth ? (
                <div
                    className="loading-item"
                    style={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        height: "100vh"
                    }}
                >
                    <span className="loading loading-infinity loading-lg"></span>
                </div>
            ) : (
                <>
                    {isAdmin &&
                        <div className="admin-panel-page"
                                style={{
                                    display: "flex",
                                    flexDirection: "row",
                                    height: "100vh",
                                    width: "100vw",
                                }}
                        >
                            <AdminPanelSidebar totalPendingProjectsCount={666} />

                            <div
                                style={{
                                    display: "flex",
                                    flexDirection: "column",
                                    width: "85vw",
                                    padding: "5vh"
                                }}
                                className="bg-base-300"
                            >
                                {currentPage === "home" && <h1>Home</h1>}
                                {currentPage === "dashboardUsers" && <h1>DashboardUsers</h1>}
                                {currentPage === "submittedProjects" && <h1>Submitted Projects</h1>}
                                {currentPage === "pendingProjects" && <h1>Pending Projects</h1>}
                            </div>
                        </div>
                    }
                </>
            )}
        </>
    );
}

export default AdminPanel;