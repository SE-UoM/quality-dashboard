import './PasswordResetPage.css'
import {useLocation} from "react-router-dom";
import {Alert, Button, Form, Modal} from "react-bootstrap";
import FloatingFormInput from "../../../components/forms/FloatingFormInput/FloatingFormInput.tsx";
import dashboardLogo from "../../../assets/dashboard_logo_transparent.png";
import apiUrls from "../../../assets/data/api_urls.json";
import React, {useEffect, useState} from "react";
import axios from "axios";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function PasswordResetPage() {
    const location = useLocation()

    const [showModal, setShowModal] = useState(false);
    const [isChecked, setIsChecked] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showRepeatPassword, setShowRepeatPassword] = useState(false);

    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");

    const handlePasswordReset = (e) => {
        e.preventDefault();

        // Make sure the user actually added details
        if (password === "" || repeatPassword === "") {
            setError(true);
            setErrorMessage("Please fill in all the fields");
            return;
        }

        // Check if the passwords match
        if (password !== repeatPassword) {
            setError(true);
            setErrorMessage("Passwords do not match.");
            return;
        }

        // Make sure the password is more than 6 characters
        if (password.length < 6) {
            setError(true);
            setErrorMessage("Password must be at least 6 characters long.");
            return;
        }

        // Make sure the user checks the checkbox
        if (!isChecked) {
            setError(true);
            setErrorMessage("Please check the checkbox.");
            return;
        }

        setLoading(true);

        // Call the API to reset the password
        let params = new URLSearchParams(location.search)
        let token = encodeURIComponent(params.get('token'))
        let uid = params.get('uid')

        let url = baseApiUrl + apiUrls.routes.resetPassword;

        // Add request body
        const data = {
            password: password,
            token: token,
            uid: uid
        }


        const headers = {
            'Content-Type': 'application/json'
        }

        axios.put(url, data, { headers: headers })
            .then((response) => {
                setLoading(false)
                setShowModal(true)
            })
            .catch((error) => {
                console.error(error.response)
                setError(true)
                setLoading(false)
                setErrorMessage(error.response.data.message)
            })
    }

    // Effect to open the modal when showModal becomes true
    useEffect(() => {
        if (showModal) {
            const modal = document.getElementById("pass_reset_modal");
            modal.showModal();
        }
    }, [showModal]);

    return (
        <div className="password-reset-page">
            <h1 className="text-lg-center">
                <img src={dashboardLogo} alt="Dashboard Logo" className="dashboard-logo"/>
                <i className="bi bi-shield-lock text-white text-lg-center"
                    style={{fontSize: "1.5em", fontWeight: "bold"}}
                > Reset Password</i>

                <p className="forgot-pass-page-text">
                    Enter your new password below.
                </p>
            </h1>

            <div className="card glass w-96 bg-primary text-primary-content shrink-0 max-w-sm">
                {error &&
                    <div role="alert" className="alert alert-error w-50">
                        <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                        <span>{errorMessage}</span>
                    </div>
                }

                <form className="card-body w-100">
                    <div className="form-control">
                        <div className="flex justify-content-evenly text-neutral-content">
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
                    </div>

                    <div className="form-control">
                        <div className="flex justify-content-evenly text-neutral-content">
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

                                <input
                                    type={showRepeatPassword ? "text" : "password"}
                                    className="grow"
                                    placeholder="repeat password"
                                    onChange={(e) => setRepeatPassword(e.target.value)}
                                />

                                <div
                                    className={"btn btn-ghost"}
                                    onClick={(e) => {
                                        setShowRepeatPassword(!showRepeatPassword);
                                    }}

                                    style={{
                                        padding: '0.5rem',
                                    }}
                                >
                                    {showRepeatPassword ? <i className="bi bi-eye-slash-fill"> </i> : <i className="bi bi-eye-fill"> </i>}
                                </div>
                            </label>
                        </div>
                    </div>

                    <div className="form-control">
                        <label className="label bg-base-100 cursor-pointer rounded">
                            <span style={{color: "#949ba8"}} className="label-text">
                                I am not a <i style={{fontSize: "1em", color: "#949ba8"}} className={"bi bi-robot"}> </i> (robot).
                            </span>
                            <input
                                type="checkbox"
                                className="checkbox "
                                onChange={() => setIsChecked(!isChecked)}
                            />
                        </label>
                    </div>

                    <button className="btn btn-dark w-full" onClick={handlePasswordReset}>
                        {!loading && <i style={{fontSize: "1em"}} className="bi bi-arrow-right-circle-fill"> </i>}
                        {loading && <span className="loading loading-dots loading-xs"></span>}
                        Reset Password
                    </button>
                </form>
            </div>

            <dialog id="pass_reset_modal" className="modal modal-bottom sm:modal-middle">
                <div className="modal-box">
                    <form method="dialog">
                        <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                    </form>

                    <h3 className="font-bold text-lg">
                        <i className="bi bi-shield-lock-fill"> </i>
                        Password Reset Success
                    </h3>

                    <p className="py-4">
                        <div role="alert" className="alert alert-success">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="stroke-current shrink-0 w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                            <span>
                                    Password reset successful!
                            </span>
                        </div>
                        <br/>

                        You password has been reset successfully. You can now login with your new password. <br/><br/>

                        <a href={"/"} className="link">
                            Back to Home Page
                        </a>
                    </p>
                </div>
            </dialog>
        </div>
    );
}


export default PasswordResetPage;