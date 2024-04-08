import SignUpForm from "../../components/forms/SignUpForm/SignUpForm.tsx";
import './RegisterPage.css';
import useLocalStorage from "../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiRoutes from "../../assets/data/api_urls.json";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function RegisterPage() {
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
        <>
            <div className="register-page">
                {!isAuthenticated && <SignUpForm/>}
            </div>
        </>
    );
}

export default RegisterPage;
