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

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function HomePage() {
    const [accessToken] = useLocalStorage<string>('accessToken', '');

    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const [userRole, setUserRole] = useState<string>('')
    const [loadingAuth, setLoadingAuth] = useState<boolean>(true)

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
                        <div className="max-w-md">
                            <h1 className="mb-5 text-5xl font-bold"
                                style={{color: 'white'}}
                            >
                                {homeText.heroHeader}
                            </h1>
                            <p style={{color: 'white'}}>
                                {homeText.heroText}
                            </p>

                            {!isAuthenticated &&
                                <a href="/login" className="btn">
                                    {homeText.heroButton}
                                </a>
                            }

                            {isAuthenticated &&
                                <a href="/dashboard" className="btn">
                                    {homeText.heroButton}
                                </a>
                            }
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default HomePage;
