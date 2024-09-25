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

        <div className="home-page">
            {loadingAuth ? (
                <span className="loading loading-infinity loading-lg"></span>
            ) : (
                <div
                    className="hero min-h-screen"
                    style={{
                        backgroundImage: `url(${bgImg})`

                    }}>
                    <div className="hero-overlay bg-opacity-85"></div>
                    <div className="hero-content text-center text-neutral-content">
                        <div className="">
                            {orgNames &&
                                <div style={{textAlign: "left"}} className="orgs">
                                    {orgNames.map((org: any) => (
                                        <div className="hero min-h-screen">
                                            <div className="hero-content flex-col lg:flex-row">
                                                <img
                                                    src={org.imgURL}
                                                    className="max-w-sm rounded-lg shadow-2xl bg-neutral-content"/>
                                                <div >
                                                    <h1 className="text-5xl font-bold">{org.name}</h1>
                                                    <p style={{textAlign: "left"}} className="py-6">

                                                    </p>
                                                    <button className="btn btn-primary">
                                                        <Link to={`/dashboard?orgID=${org.id}`}>View Organization's Dashboard</Link>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            }

                            {/*{isAuthenticated &&*/}
                            {/*    <a href="/dashboard" className="btn">*/}
                            {/*        {homeText.heroButton}*/}
                            {/*    </a>*/}
                            {/*}*/}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default HomePage;
