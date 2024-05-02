import React, { useEffect, useState } from 'react';
import {Button, Image} from 'react-bootstrap';
import useLocalStorage from '../../../hooks/useLocalStorage.ts';
import openSourceLogo from '../../../assets/img/OS_UoM_Logo.png';
import sdeLabLogo from '../../../assets/img/sde-banner.png';
import './HomePage.css';
import useAuthenticationCheck from "../../../hooks/useAuthenticationCheck.ts";
import {Link} from "react-router-dom";

function HomePage() {
    const [accessToken] = useLocalStorage<string>('accessToken', '');
    const [isAuthenticated, setIsAuthenticated] = useAuthenticationCheck(accessToken)

    return (

        <div className="home-page">
            <button className="btn">Button</button>

            <h1>Welcome to UoM Dashboard</h1>
            <p>Here you can find all the information you need about the software quality of University of Macedonia</p>

            <section className="homepage-action-buttons">
                {/* These Will show only if the token we have on local storage works */}
                {isAuthenticated && <Button href="/submit-project" variant="outline-light">Add Project</Button>}
                {isAuthenticated && <Button href="/dashboard" variant="outline-light">Dashboard</Button>}
                {isAuthenticated && <Button href="/" variant="outline-light">View your Projects</Button>}

                {isAuthenticated !== null && !isAuthenticated && <Button href="/login" variant="outline-light">Login</Button>}
                {isAuthenticated !== null && !isAuthenticated && <Button href="/register" variant="outline-light">Register</Button>}
            </section>

            <div className="homepage-images">
                <Link className="home-page-img-link" to="https://opensource.uom.gr">
                    <Image className="img" height="60vh" src={openSourceLogo} alt="OpenSource UoM Logo" rounded/>
                </Link>

                <Link className="home-page-img-link" to={"https://sde.uom.gr"}>
                    <Image className="img" height="60vh" src={"https://sde.uom.gr/wp-content/uploads/2016/10/sde-banner.png"} alt="Software and Data Engineering Lab Logo" rounded/>
                </Link>

                <Link className="home-page-img-link" to={"https://www.uom.gr/site/en"}>
                    <Image className="img" id="uom" height="60vh" src="https://www.uom.gr/site/images/logos/UOMLOGOEN.png" alt="University of Macedonia Logo" rounded/>
                </Link>
            </div>
        </div>
    );
}

export default HomePage;
