import './ForgotPasswordPage.css'
import dashboardLogo from "../../../assets/dashboard_logo_transparent.png";
import React, {useEffect, useState} from "react";
import {Alert, Button, Form, Modal} from "react-bootstrap";
import apiUrls from "../../../assets/data/api_urls.json";
import axios from "axios";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function ForgotPasswordPage() {
    const [showModal, setShowModal] = useState(false);
    const [email, setEmail] = useState("");
    const [isChecked, setIsChecked] = useState(false);

    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");


    const handleSubmit = (e: any) => {
        // Make sure the form doesn't reload the page
        e.preventDefault()

        // Make sure the user has entered data
        if (email === "") {
            setError(true)
            setErrorMessage("Please enter your email address.")
            return
        }

        // Make sure the user checked the checkbox
        if (!isChecked) {
            setError(true)
            setErrorMessage("Please check the checkbox.")
            return
        }

        setLoading(true)

        // Call the API to send the password reset link
        let url = baseApiUrl + apiUrls.routes.resetPasswordRequest + "?userEmail=" + email;

        console.log(url)
        axios.post(url)
            .then((response) => {
                setShowModal(true)
                setLoading(false)
            })
            .catch((error) => {
                setError(true)
                setErrorMessage(error.response.data.message)
                setLoading(false)
            })

    }

    // Effect to open the modal when showModal becomes true
    useEffect(() => {
        if (showModal) {
            const modal = document.getElementById("forgot_pass_modal");
            modal.showModal();
        }
    }, [showModal]);

    return (
        <div className="forgot-pass-page">
            <h1 className="forgot-pass-page-title">
                <img src={dashboardLogo} alt="Dashboard Logo" className="dashboard-logo"/>
                <i className="bi bi-shield-lock text-white"> Forgot Password</i>

                <p className="forgot-pass-page-text">
                    Enter your email address and we will send you a link to reset your password.
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
                        <label
                            className="input input-bordered flex items-center gap-2 text-neutral-content bg-base-100 rounded-lg"

                        >
                            <i className="bi bi-at"> </i>
                            <input
                                type="text"
                                className="grow"
                                placeholder="Email"
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </label>
                    </div>

                    <div className="form-control">
                        <label className="label bg-base-100 cursor-pointer rounded">
                                <span style={{color: "#949ba8"}} className="label-text">
                                    I am not a <i style={{fontSize: "1em", color: "#949ba8"}} className={"bi bi-robot"}> </i> (robot).
                                </span>
                            <input
                                type="checkbox"
                                className="checkbox "
                                onChange={(e) => setIsChecked(e.target.checked)}
                            />
                        </label>
                    </div>

                    <button className="btn btn-dark w-full" onClick={handleSubmit}>
                        {!loading && <i style={{fontSize: "1em"}} className="bi bi-envelope-fill"> </i>}
                        {loading && <span className="loading loading-dots loading-xs"></span>}
                        Send Password Reset Link
                    </button>
                </form>
            </div>

            <dialog id="forgot_pass_modal" className="modal modal-bottom sm:modal-middle">
                <div className="modal-box">
                    <form method="dialog">
                        <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                    </form>

                    <h3 className="font-bold text-lg">
                        <i className="bi bi-shield-lock-fill"> </i>
                        Password Reset
                    </h3>

                    <p className="py-4">
                        <div role="alert" className="alert alert-info">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="stroke-current shrink-0 w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                            <span>
                                    Password reset link sent!
                            </span>
                        </div>
                        <br/>

                        We sent you an email to reset your password. Please check your inbox and follow the instructions. <br/> <br/>
                        <strong>
                            The link will expire in 5 minutes.
                        </strong>
                    </p>
                </div>
            </dialog>
        </div>
    )
}

export default ForgotPasswordPage