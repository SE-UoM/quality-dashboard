import {useEffect, useState} from 'react'
import { Box, Flex, Button, FormControl, FormLabel, Input, Grid } from '@chakra-ui/react';
import LoginForm from "../../../components/forms/LoginForm/LoginForm.tsx";
import './LoginPage.css';
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import apiRoutes from "../../../assets/data/api_urls.json";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function LoginPage() {
    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");

    // Call a random API to check if the user is authenticated
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        if (accessToken === "")
            setIsAuthenticated(false)

        let apiUrl = baseApiUrl + apiRoutes.routes.dashboard.getTotalTD;
        const headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        fetch(apiUrl, { headers: headers })
            .then(response => {
                if (response.ok) {
                    setIsAuthenticated(true)
                } else {
                    setIsAuthenticated(false)
                }
            })
            .catch(error => {
                setIsAuthenticated(false)
            })
    }, [accessToken])

    useEffect(() => {
        if (isAuthenticated) {
            window.location.href = '/'
        }
    }, [isAuthenticated]);


    return (
        <div className={'login-page'}>
            {!isAuthenticated &&
                <LoginForm/>
            }
        </div>
    )
}

export default LoginPage