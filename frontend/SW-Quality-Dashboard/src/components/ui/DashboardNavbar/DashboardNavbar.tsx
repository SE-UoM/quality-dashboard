import logo from '../../../assets/dashboard_logo_transparent.png'
import './DashboardNavbar.css'
import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap";
import {useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import DecodedToken from "../../../interfaces/DecodedToken.ts";

function DashboardNavbar({isAuthenticated, isAdmin}) {
    const accessToken = localStorage.getItem('accessToken')
    const [userName, setUserName] = useState('')

    useEffect(() => {
        if (!isAuthenticated || !accessToken)
            return

        console.log(accessToken)

        // Get the user name from the token
        let decoded : DecodedToken = jwtDecode(accessToken)

        if (!decoded) {
            return
        }

        let name = decoded.name;
        setUserName(name)
    }, []);

    let handleLogout = () => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        window.location.href = '/'
    }

    return (
        <>
            <div className="navbar bg-base-100">
                {/* NAVBAR LOGO */}
                <div className="flex-1">
                    <a href="/" className="navbar-brand">
                        <img src={logo}/>
                        <section className="navbar-brand-text">
                            <span>University of Macedonia</span>
                            <h1>Quality Dashboard</h1>
                        </section>
                    </a>
                </div>

                <div className="flex-none">
                        <div className="dropdown dropdown-end">
                            <label className="swap swap-rotate">

                                {/* this hidden checkbox controls the state */}
                                <input type="checkbox" className="theme-controller" value="light" />

                                {/* sun icon */}
                                <svg className="swap-off fill-current w-7 h-7 m-1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.64,17l-.71.71a1,1,0,0,0,0,1.41,1,1,0,0,0,1.41,0l.71-.71A1,1,0,0,0,5.64,17ZM5,12a1,1,0,0,0-1-1H3a1,1,0,0,0,0,2H4A1,1,0,0,0,5,12Zm7-7a1,1,0,0,0,1-1V3a1,1,0,0,0-2,0V4A1,1,0,0,0,12,5ZM5.64,7.05a1,1,0,0,0,.7.29,1,1,0,0,0,.71-.29,1,1,0,0,0,0-1.41l-.71-.71A1,1,0,0,0,4.93,6.34Zm12,.29a1,1,0,0,0,.7-.29l.71-.71a1,1,0,1,0-1.41-1.41L17,5.64a1,1,0,0,0,0,1.41A1,1,0,0,0,17.66,7.34ZM21,11H20a1,1,0,0,0,0,2h1a1,1,0,0,0,0-2Zm-9,8a1,1,0,0,0-1,1v1a1,1,0,0,0,2,0V20A1,1,0,0,0,12,19ZM18.36,17A1,1,0,0,0,17,18.36l.71.71a1,1,0,0,0,1.41,0,1,1,0,0,0,0-1.41ZM12,6.5A5.5,5.5,0,1,0,17.5,12,5.51,5.51,0,0,0,12,6.5Zm0,9A3.5,3.5,0,1,1,15.5,12,3.5,3.5,0,0,1,12,15.5Z"/></svg>

                                {/* moon icon */}
                                <svg className="swap-on fill-current w-7 h-7 m-1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M21.64,13a1,1,0,0,0-1.05-.14,8.05,8.05,0,0,1-3.37.73A8.15,8.15,0,0,1,9.08,5.49a8.59,8.59,0,0,1,.25-2A1,1,0,0,0,8,2.36,10.14,10.14,0,1,0,22,14.05,1,1,0,0,0,21.64,13Zm-9.5,6.69A8.14,8.14,0,0,1,7.08,5.22v.27A10.15,10.15,0,0,0,17.22,15.63a9.79,9.79,0,0,0,2.1-.22A8.11,8.11,0,0,1,12.14,19.73Z"/></svg>

                            </label>
                        </div>

                    {isAuthenticated &&
                        <div className="dropdown dropdown-end">
                            <div tabIndex="0" role="button" className="btn btn-ghost btn-circle avatar">
                                <div className="w-10 rounded-full">
                                    <img alt="Tailwind CSS Navbar component"
                                         src={"https://ui-avatars.com/api/?name=" + userName}/>
                                </div>
                            </div>
                            <ul tabindex="0"
                                class="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52"
                            >
                                <li>
                                    <a href="/dashboard">
                                        <i className="bi bi-speedometer2"> </i>
                                        Go to Dashboard
                                    </a>
                                </li>
                                <li>
                                    <a href="/submit-project">
                                        <i className="bi bi-plus-circle-fill"> </i>
                                        Submit a Project
                                    </a>
                                </li>
                                <li>
                                    <a href="/admin">
                                        <i className="bi bi-gear-fill"> </i>
                                        Admin Panel
                                    </a>
                                </li>

                                <li>
                                    <button
                                        onClick={handleLogout}
                                        className="text-error"
                                    >
                                        <i className="bi bi-power"> </i>
                                        Logout
                                    </button>
                                </li>
                            </ul>
                        </div>
                    }
                </div>
            </div>
        </>
    )
}

export default DashboardNavbar