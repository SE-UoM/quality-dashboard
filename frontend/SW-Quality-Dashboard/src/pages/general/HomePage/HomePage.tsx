import React, { useEffect, useState } from 'react';
import {Button, Image} from 'react-bootstrap';
import useLocalStorage from '../../../hooks/useLocalStorage.ts';
import openSourceLogo from '../../../assets/img/OS_UoM_Logo.png';
import homeText from '../../../../content/pages/home.json'
import './HomePage.css';
import useAuthenticationCheck from "../../../hooks/useAuthenticationCheck.ts";
import {Link} from "react-router-dom";
import bgImg from '../../../assets/img/screen-mockup.png';
import axios from "axios";
import backendRoutes from "../../../assets/data/api_urls.json"
import useAxiosGet from "../../../hooks/useAxios.ts";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function HomePage() {
    const [accessToken] = useLocalStorage<string>('accessToken', '');

    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const [userRole, setUserRole] = useState<string>('')
    const [loadingAuth, setLoadingAuth] = useState<boolean>(true)

    const {data: orgNames, loading: orgNamesLoading, error: orgNamesError, errorMessage: orgNamesErrorMessage} =
        useAxiosGet(baseUrl + backendRoutes.routes.getAllOrganizationNames, "");

    useEffect(() => {
        setLoadingAuth(true)

        const url = `${baseUrl}${backendRoutes.routes.checkAuth}`

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
        }
        
        axios.get(url, {headers: headers})
            .then((response) => {
                setIsAuthenticated(true)

                const rolesStr = response.data.roles;

                let isAdmin = rolesStr.includes('PRIVILEGED')
                if (isAdmin) setUserRole('admin')
                else setUserRole('user')

                // Wait one second before setting the loadingAuth to false
                setTimeout(() => {
                    setLoadingAuth(false)
                }, 1000);

            })
            .catch((error) => {
                setIsAuthenticated(false)
                setLoadingAuth(false)
                setUserRole('')
            })
    }, []);

    return (

        <div className="home-page bg-base-300 p-4">
            {loadingAuth ? (
                <span className="loading loading-infinity loading-lg"></span>
            ) : (
                <div className="bg-base-300" style={{width: '100%'}}>
                    <h1 className="text-5xl font-bold text-center text-base-content">
                        Welcome to the Software Quality Dashboard
                    </h1>

                    <p style={{fontSize: "1.2em"}} className={"text-base-content"}>
                        To view a dashboard, select a University from the list below, and click on the button.
                    </p>

                    {orgNames &&
                        <div style={{textAlign: "left", paddingTop: '1em'}} className="orgs text-base-content">
                            {orgNames.map((org: any) => (
                                <>
                                    <div className="card card-side bg-base-100 org-card">
                                        <figure style={{height: '30vh'}}>
                                            <img
                                                src={org.imgURL}
                                                style={{height: '30vh', backgroundColor: 'white'}}
                                                alt="Organization Logo"/>
                                        </figure>
                                        <div className="card-body">
                                            <h2 className="card-title">{org.name}</h2>

                                            <p style={{textAlign: "left", fontSize: "1em"}}>
                                                <ul>
                                                    <li>
                                                        <i className="bi bi-geo-alt-fill"></i> {org.location}
                                                    </li>

                                                    <li>
                                                        <i className="bi bi-person-fill"></i> {org.totalDevelopers} Developers
                                                    </li>

                                                    <li>
                                                        <i className="bi bi-cup-hot-fill"></i> {org.totalProjects} Projects
                                                    </li>
                                                </ul>
                                            </p>

                                            <div className="card-actions justify-end">
                                                <button className="btn btn-primary"
                                                    onClick={() => window.location.href = `/dashboard?orgID=${org.id}`}>
                                                    <i className={"bi bi-arrow-right"}> </i>
                                                    View Dashboard
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </>
                            ))}
                        </div>
                    }
                </div>
            )}
        </div>
    );
}

export default HomePage;
