import React, { useState } from 'react';
import axios from "axios";
import { Form, Button, Alert } from "react-bootstrap";
import './LoginForm.css';
import apiUrls from "../../../assets/data/api_urls.json";
import FloatingFormInput from "../FloatingFormInput/FloatingFormInput.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";

interface Organization {
    id: string;
    name: string;
}

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function LoginForm() {
    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("This is an error message.");
    const [loginSuccess, setLoginSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isChecked, setIsChecked] = useState(false); // State to track checkbox status
    const [showPassword, setShowPassword] = useState(false); // State to track password visibility

    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [refreshToken, setRefreshToken] = useLocalStorage("refreshToken", "");


    const handleLogin = (e) => {
        e.preventDefault();

        // Check if the checkbox is checked
        if (!isChecked) {
            setError(true);
            setErrorMessage("Please check the checkbox.");
            return;
        }

        // Make sure the user actually added details
        if (email === "" || password === "") {
            setError(true);
            setErrorMessage("Please fill in all the fields");
            return;
        }

        setLoading(true)

        const data = {
            email: email,
            password: password,
        }

        const headers = {
            'Content-Type': 'application/x-www-form-urlencoded',
        }

        let apiUrl = baseApiUrl + apiUrls.routes.login;

        axios.post(apiUrl, data, { headers: headers })
            .then((response) => {
                let data = response.data;

                setError(false);
                setErrorMessage("");
                setAccessToken(data.accessToken);
                setRefreshToken(data.refreshToken);

                // Save the tokens to local storage
                localStorage.setItem("accessToken", data.accessToken);
                localStorage.setItem("refreshToken", data.refreshToken);

                // Redirect to the dashboard after 2 seconds
                setTimeout(() => {
                    setLoading(false)
                    setLoginSuccess(true);
                    window.location.href = "/";
                }, 2000);

            })
            .catch((error) => {
                console.warn("Auth Error " + error);
                setError(true);
                setLoginSuccess(false);
                setErrorMessage("Invalid email or password.");
            });
    };

    return (
        <>
            <div className="card shrink-0 w-full max-w-sm bg-base-100 w-100">
                <h1
                    className="text-5xl font-bold m-2"
                    style={{textAlign: 'center'}}
                >
                    <i className="bi bi-door-open-fill"> </i> Login
                </h1>

                <form className="card-body w-100">
                    {error &&
                        <div role="alert" className="alert alert-error">                            <i className="bi bi-exclamation-triangle-fill"> </i>
                            <span> {errorMessage} </span>
                        </div>
                    }

                    {loginSuccess &&
                        <div role="alert" className="alert alert-success">
                            <i className="bi bi-check-circle-fill"> </i>
                            <span>
                                Login successful! Redirecting...
                            </span>
                        </div>
                    }


                    <div className="form-control">
                        <label className="input input-bordered flex items-center gap-2">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4 opacity-70"><path d="M2.5 3A1.5 1.5 0 0 0 1 4.5v.793c.026.009.051.02.076.032L7.674 8.51c.206.1.446.1.652 0l6.598-3.185A.755.755 0 0 1 15 5.293V4.5A1.5 1.5 0 0 0 13.5 3h-11Z" /><path d="M15 6.954 8.978 9.86a2.25 2.25 0 0 1-1.956 0L1 6.954V11.5A1.5 1.5 0 0 0 2.5 13h11a1.5 1.5 0 0 0 1.5-1.5V6.954Z" /></svg>
                            <input
                                type="text"
                                className="grow"
                                placeholder="Email"
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </label>
                    </div>
                    <div className="form-control">
                        <div className="flex justify-content-evenly">
                            <label className="input input-bordered flex justify-content-between gap-2"
                                   style={{width: '100%', margin: 0, padding: 0}}
                            >
                                <i
                                    className={"bi bi-shield-lock-fill"}
                                    style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        paddingLeft: '0.5rem',
                                    }}
                                > </i>
                                {/*<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4 opacity-70"><path fill-rule="evenodd" d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z" clip-rule="evenodd" /></svg>*/}
                                <input
                                    type={showPassword ? "text" : "password"}
                                    className="grow"
                                    placeholder="password"
                                    onChange={(e) => setPassword(e.target.value)}
                                />

                                <div
                                    className={"btn btn-ghost"}
                                    onClick={(e) => {
                                        setShowPassword(!showPassword);
                                    }}

                                    style={{
                                        padding: '0.5rem',
                                    }}
                                >
                                    {showPassword ? <i className="bi bi-eye-slash-fill"> </i> : <i className="bi bi-eye-fill"> </i>}
                                </div>
                            </label>


                        </div>

                        <label className="label">
                            <a href="/forgot-password" className="label-text-alt link link-hover">Forgot password?</a>
                        </label>
                    </div>

                    <div className="form-control">
                        <label className="label cursor-pointer">
                                <span className="label-text">
                                    I am not a <i className={"bi bi-robot"}> </i> (robot).
                                </span>
                            <input
                                type="checkbox"
                                className="checkbox checkbox-primary"
                                onChange={(e) => setIsChecked(e.target.checked)}
                            />
                        </label>
                    </div>

                    <div className="form-control mt-6">
                        <button
                            className="btn btn-primary"
                            onClick={handleLogin}
                        >
                            {!loading && <i className="bi bi-door-open-fill"> </i>}
                            {loading && <span className="loading loading-dots loading-xs"></span>}
                            Login
                        </button>
                    </div>

                    <div className="form-control mt-6">
                        Don't have an account? <a href="/register" className="link">Sign up here.</a>
                    </div>
                </form>

            </div>
        </>
    );
}

export default LoginForm;
