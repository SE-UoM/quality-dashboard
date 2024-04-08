import {Col, Nav, Row, Tab} from 'react-bootstrap';
import './AdminPanel.css';
import {useEffect, useState} from "react";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import useAuthenticationCheck from "../../../hooks/useAuthenticationCheck.ts";
import {jwtDecode} from "jwt-decode";
import DecodedToken from "../../../interfaces/DecodedToken.ts";
import AdminAllProjectsPage from "../AdminAllProjectsPage/AdminAllProjectsPage.tsx";
import AdminPendingProjectsPage from "../AdminPendingProjectsPage/AdminPendingProjectsPage.tsx";
import AdminAllUsersPage from "../AdminAllUsersPage/AdminAllUsersPage.tsx";



function AdminPanel() {
    const [accessToken] = useLocalStorage<string>('accessToken', '');
    const [isAdmin, setIsAdmin] = useState<boolean>(false)
    const [isAuthenticated, setIsAuthenticated] = useState(false)

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
            {isAdmin &&
                <div className="admin-panel-page">

                <Tab.Container id="admin-tabs" defaultActiveKey="first">
                    <div className="tabs-content">
                        {/*  ADMIN TAB NAVBAR  */}
                        <div className="nav-tabs">
                            <Nav  className="flex-column">
                                <Nav.Item
                                    className="admin-navbar-items"
                                    id="item1"
                                >
                                    <Nav.Link className="admin-nav-link"
                                              eventKey="first"
                                              style={{
                                                  borderRadius: "0"
                                              }}
                                    >
                                        <i className="bi bi-journal-code"> </i>
                                        All Projects
                                    </Nav.Link>
                                </Nav.Item>

                                <Nav.Item
                                    className="admin-navbar-items"
                                    id="item2"
                                >
                                    <Nav.Link
                                        className="admin-nav-link"
                                        eventKey="second"
                                        style={{
                                            borderRadius: "0"
                                        }}
                                    >
                                        <i className="bi bi-exclamation-octagon"> </i>
                                        Pending Projects
                                    </Nav.Link>
                                </Nav.Item>

                                <Nav.Item
                                    className="admin-navbar-items"
                                    id="item3"
                                >
                                    <Nav.Link
                                        className="admin-nav-link"
                                        eventKey="three"
                                        style={{
                                            borderRadius: "0"
                                        }}
                                    >
                                        <i className="bi bi-people"> </i>
                                        All Users
                                    </Nav.Link>
                                </Nav.Item>
                            </Nav>
                        </div>


                        <div className="nav-tabs-content">
                            <Tab.Content>
                                <Tab.Pane eventKey="first">
                                    <AdminAllProjectsPage/>
                                </Tab.Pane>

                                <Tab.Pane eventKey="second">
                                    <AdminPendingProjectsPage/>
                                </Tab.Pane>

                                <Tab.Pane eventKey="three">
                                    <AdminAllUsersPage />
                                </Tab.Pane>
                            </Tab.Content>
                        </div>
                    </div>
                </Tab.Container>

            </div>}
        </>
    );
}

export default AdminPanel;