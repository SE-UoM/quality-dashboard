import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import useLocalStorage from '../../hooks/useLocalStorage.ts';
import axios from 'axios';
import './HomePage.css';

function HomePage() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [accessToken, setAccessToken] = useLocalStorage<string>('accessToken', '');

    // Call a random API to check if the user is authenticated
    useEffect(() => {
        const fetchData = async () => {
            try {
                if (!accessToken) {
                    setIsAuthenticated(false);
                    return;
                }

                const headers = {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                };

                const response = await axios.get('http://localhost:8080/api/organizations', { headers });

                if (response.status === 200) {
                    setIsAuthenticated(true);
                } else {
                    setIsAuthenticated(false);
                }
            } catch (error) {
                setIsAuthenticated(false);
                console.error('Error:', error);
            }
        };

        fetchData();
    }, [accessToken]);

    return (
        <div className="home-page">
            <h1>Welcome to UoM Dashboard</h1>
            <p>Here you can find all the information you need about the software quality of University of Macedonia</p>

            <section className="homepage-action-buttons">
                {/* These Will show only if the token we have on local storage works */}
                {isAuthenticated && <Button href="/submit-project" variant="outline-light">Add Project</Button>}
                {isAuthenticated && <Button href="/dashboard" variant="outline-light">Dashboard</Button>}
                {isAuthenticated && <Button href="/" variant="outline-light">View your Projects</Button>}

                {!isAuthenticated && <Button href="/login" variant="outline-light">Login</Button>}
                {!isAuthenticated && <Button href="/register" variant="outline-light">Register</Button>}
                {!isAuthenticated && <Button href="/dashboard" variant="outline-light">Dashboard</Button>}
            </section>
        </div>
    );
}

export default HomePage;
