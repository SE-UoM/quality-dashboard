import useLocalStorage from "../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import backendRoutes from "../assets/data/api_urls.json"
import axios from "axios";
import {Navigate, Route} from "react-router-dom";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

export default function ProtectedRoute({loadingAuth, setLoadingAuth}) {
    const [accessToken] = useLocalStorage<string>('accessToken', '');

    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false)
    const [userRole, setUserRole] = useState<string>('')

    useEffect(() => {
        setLoadingAuth(true)

        if (accessToken === "" || accessToken === null) {
            window.location.href = '/login';
        }

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
        }

        const apiURL = `${baseUrl}${backendRoutes.routes.checkAuth}`

        axios.get(apiURL, {headers: headers})
            .then((response) => {
                setIsAuthenticated(true)

                const rolesStr = response.data.roles;

                let isAdmin = rolesStr.includes('PRIVILEGED')
                if (isAdmin) setUserRole('admin')
                else setUserRole('user')

                // Wait one second before setting the loadingAuth to false
                setTimeout(() => {
                    setLoadingAuth(false)
                }, 500);

            })
            .catch((error) => {
                setIsAuthenticated(false)
                // Wait one second before setting the loadingAuth to false
                setTimeout(() => {
                    window.location.href = '/login';
                }, 1000);

                setUserRole('')
            })
    }, []);

    return (
        <>
        </>
    );
}