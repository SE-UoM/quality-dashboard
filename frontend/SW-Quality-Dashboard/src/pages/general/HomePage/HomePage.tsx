import React, { useEffect, useState } from 'react';
import {Button, Image} from 'react-bootstrap';
import useLocalStorage from '../../../hooks/useLocalStorage.ts';
import openSourceLogo from '../../../assets/img/OS_UoM_Logo.png';
import homeText from '../../../../content/pages/home.json'
import './HomePage.css';
import useAuthenticationCheck from "../../../hooks/useAuthenticationCheck.ts";
import {Link} from "react-router-dom";
import bgImg from '../../../assets/img/screen-mockup.png';

function HomePage() {
    const [accessToken] = useLocalStorage<string>('accessToken', '');
    const [isAuthenticated, setIsAuthenticated] = useAuthenticationCheck(accessToken)

    return (

        <div className="home-page">
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
                        <a
                            href="/login"
                            className="btn"
                        >
                            {homeText.heroButton}
                        </a>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomePage;
