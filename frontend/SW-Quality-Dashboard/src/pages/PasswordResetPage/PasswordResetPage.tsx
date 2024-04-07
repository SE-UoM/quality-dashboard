import './PasswordResetPage.css'
import {useLocation} from "react-router-dom";
import {Alert, Button, Form, Modal} from "react-bootstrap";
import FloatingFormInput from "../../components/forms/FloatingFormInput/FloatingFormInput.tsx";
import dashboardLogo from "../../assets/dashboard_logo_transparent.png";
import apiUrls from "../../assets/data/api_urls.json";
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
                console.log(response)
                setShowModal(true)
            })
            .catch((error) => {
                console.error(error.response)
                setError(true)
                setErrorMessage(error.response.data.message)
            })
    }

    const handleClose = () => {
        setShowModal(false)
    }

    return (
        <div className="password-reset-page">
            <h1>
                <img src={dashboardLogo} alt="Dashboard Logo" className="dashboard-logo"/>
                <i className="bi bi-shield-lock-fill"> Password Reset</i>
            </h1>

            {error &&
                <Alert variant="danger">
                    <i className="bi bi-exclamation-triangle-fill"> </i>
                    <strong> {errorMessage} </strong>
                </Alert>
            }

            <Form className="sign-up-form-content">
                <Form.Group controlId="formBasicPassword">
                    <div className="form-pass-container">
                        <FloatingFormInput
                            type={showPassword ? "text" : "password"}
                            id="passInput"
                            placeholder="something safe"
                            isRequired={true}
                            onChange={(e) => setPassword(e.target.value)}
                            icon="bi bi-shield-lock-fill"
                            labelText="Password"
                        />

                        <Button
                            variant="outline-secondary"
                            className="show-password-btn"
                            onClick={() => setShowPassword(!showPassword)}>
                            {showPassword ? <i className="bi bi-eye-slash-fill"> </i> : <i className="bi bi-eye-fill"> </i>}
                        </Button>
                    </div>

                </Form.Group>

                <Form.Group controlId="formBasicRepeatPassword">
                    <div className="form-pass-container">
                        <FloatingFormInput
                            type={showRepeatPassword ? "text" : "password"}
                            id="repeatPassInput"
                            placeholder="repeat your password"
                            isRequired={true}
                            onChange={(e) => setRepeatPassword(e.target.value)}
                            icon="bi bi-shield-lock-fill"
                            labelText="Repeat Password"
                        />

                        <Button
                            variant="outline-secondary"
                            className="show-password-btn"
                            onClick={() => setShowRepeatPassword(!showRepeatPassword)}>
                            {showRepeatPassword ? <i className="bi bi-eye-slash-fill"> </i> : <i className="bi bi-eye-fill"> </i>}
                        </Button>
                    </div>
                </Form.Group>

                <Form.Group className="m-3 robot-check" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="I am not a 🤖 (robot). " required={true} onChange={() => setIsChecked(!isChecked)} />
                </Form.Group>

                <Button className="reset-pass-form-submit-btn" variant="light" type="submit" onClick={handlePasswordReset}>
                    <i className="bi bi-arrow-right-circle-fill"> </i>
                    Change Password
                </Button>
            </Form>

            <Modal
                show={showModal}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>
                        <i className="bi bi-person-fill-check"> </i>
                        Password Reset Complete!
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Alert variant="info">
                        <i className="bi bi-info-circle"> </i>
                        <strong> Password Reset Completed! </strong>
                    </Alert>

                    Your password has been successfully reset. You can now login with your new password.

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}


export default PasswordResetPage;