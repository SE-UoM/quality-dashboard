import logo from '../../../assets/dashboard_logo_transparent.png'
import './DashboardNavbar.css'
import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap";

function DashboardNavbar({isAuthenticated, isAdmin}) {
    let handleLogout = () => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        window.location.href = '/'
    }

    return (
        <>
            <Navbar expand="lg" className="bg-body-tertiary" style={{borderBottom: "2px solid var(--org-color-primary)"}}>
                <Container>
                    <Navbar.Brand href="/" className="navbar-brand">
                        <img src={logo}/>
                        <section className="navbar-brand-text">
                            <span>University of Macedonia</span>
                            <h1>Quality Dashboard</h1>
                        </section>
                    </Navbar.Brand>

                    <Navbar.Toggle aria-controls="basic-navbar-nav" />

                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            {!isAuthenticated && (
                                <>
                                    <Nav.Link href="/register">
                                        <i className="bi bi-person-fill-add"> </i>
                                        Sign Up
                                    </Nav.Link>

                                    <Nav.Link href="/login">
                                        <i className="bi bi-person-fill"> </i>
                                        Login
                                    </Nav.Link>
                                </>
                            )}

                            {isAuthenticated && (
                                <>
                                    <Nav.Link href="/dashboard?p=1">
                                        <i className="bi bi-speedometer2"> </i>
                                        View Dashboard
                                    </Nav.Link>

                                    <NavDropdown title={<i className="bi bi-rocket-takeoff-fill" style={{fontStyle: "initial"}}> My Actions</i>} id="actions-dropdown">
                                        {isAdmin &&
                                            <>
                                                <NavDropdown.Item href="/register-organisation" color={"var(--org-color-primary)"}>
                                                    <i className="bi bi-building-fill-add"> </i>
                                                    Create an Organization
                                                </NavDropdown.Item>

                                                <NavDropdown.Item href="/admin" color={"var(--org-color-primary)"}>
                                                    <i className="bi bi-gear-fill"> </i>
                                                    Admin Panel
                                                </NavDropdown.Item>
                                            </>
                                        }

                                        <NavDropdown.Item href="/submit-project" color={"var(--org-color-primary)"}>
                                            <i className="bi bi-plus-circle-fill"> </i>
                                            Submit a Project
                                        </NavDropdown.Item>

                                        <NavDropdown.Item href="/submit-project" color={"var(--org-color-primary)"}>
                                            <i className="bi bi-folder-fill"> </i>
                                            View Submitted Projects
                                        </NavDropdown.Item>

                                        <NavDropdown.Divider id={"nav-dropdown-logout-divider"} />

                                        <Nav.Item
                                            href="/logout"
                                            id={"nav-logout-link"}
                                            style={{}}

                                            onClick={handleLogout}
                                        >
                                            <i className="bi bi-power"> </i>
                                            Logout
                                        </Nav.Item>
                                    </NavDropdown>
                                </>
                            )}
                        </Nav>
                    </Navbar.Collapse>

                    <Navbar.Collapse className="justify-content-end">
                        <a href="https://github.com/SE-UoM/quality-dashboard" id="github-repo-icon">
                            <i className="bi bi-github"></i>
                        </a>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </>
    )
}

export default DashboardNavbar