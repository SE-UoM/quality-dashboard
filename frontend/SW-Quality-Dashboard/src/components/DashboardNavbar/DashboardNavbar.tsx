import logo from '../../assets/dashboard_logo_transparent.png'
import './DashboardNavbar.css'
import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap";

function DashboardNavbar() {
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
                            <Nav.Link href="/register">
                                <i className="bi bi-person-fill-add"> </i>
                                Sign Up
                            </Nav.Link>

                            <Nav.Link href="/login">
                                <i className="bi bi-person-fill"> </i>
                                Login
                            </Nav.Link>

                            <NavDropdown title={<i className="bi bi-rocket-takeoff-fill" style={{fontStyle: "initial"}}> Actions</i>} id="actions-dropdown">
                                <NavDropdown.Item href="/register-organisation">
                                    <i className="bi bi-building-fill-add"> </i>
                                    Create an Organization
                                </NavDropdown.Item>
                                <NavDropdown.Item href="/submit-project">
                                    <i className="bi bi-plus-circle-fill"> </i>
                                    Submit a Project
                                </NavDropdown.Item>
                                <NavDropdown.Divider />
                                <Nav.Link href="/dashboard" >
                                    <i className="bi bi-speedometer2"> </i>
                                    View Dashboard
                                </Nav.Link>
                            </NavDropdown>
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