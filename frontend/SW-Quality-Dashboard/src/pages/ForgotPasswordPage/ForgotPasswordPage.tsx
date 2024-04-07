import './ForgotPasswordPage.css'
import dashboardLogo from "../../assets/dashboard_logo_transparent.png";
import React, {useEffect, useState} from "react";
import {Alert, Button, Form, Modal} from "react-bootstrap";
import apiUrls from "../../assets/data/api_urls.json";
import axios from "axios";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function ForgotPasswordPage() {
    const [showModal, setShowModal] = useState(false);
    const [email, setEmail] = useState("");
    const [isChecked, setIsChecked] = useState(false);

    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

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

        // Call the API to send the password reset link
        let url = baseApiUrl + apiUrls.routes.resetPasswordRequest + "?userEmail=" + email;

        console.log(url)
        axios.post(url)
            .then((response) => {
                setShowModal(true)
            })
            .catch((error) => {
                setError(true)
                setErrorMessage(error.response.data.message)
            })

    }

    const handleClose = () => {
        setShowModal(false)
    }

    return (
        <div className="forgot-pass-page">
            <h1>
                <img src={dashboardLogo} alt="Dashboard Logo" className="dashboard-logo"/>
                <i className="bi bi-shield-lock"> Forgot Password</i>
            </h1>

            <p className="forgot-pass-page-text">
                Enter your email address and we will send you a link to reset your password.
            </p>

            {error &&
                <Alert variant="danger">
                    <i className="bi bi-exclamation-triangle-fill"> </i>
                    <strong> {errorMessage} </strong>
                </Alert>
            }

            <Form className="sign-up-form-content">
                <Form.Group controlId="formBasicEmail">
                    <div className="form-floating mb-3">
                        <input
                            type="email"
                            className="form-control"
                            id="emailInput"
                            placeholder="name@example.com"
                            required={true}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <label htmlFor="floatingInput">
                            <i className="bi bi-at"> </i>
                            Email
                        </label>
                    </div>

                </Form.Group>

                <Form.Group className="m-3 robot-check" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="I am not a 🤖 (robot). " required={true} onChange={() => setIsChecked(!isChecked)}/>
                </Form.Group>

                <Button className="reset-pass-form-submit-btn" variant="light" type="submit" onClick={handleSubmit}>
                    <i className="bi bi-arrow-right-circle-fill"> </i>
                    Send Password Reset Link
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
                        Password Reset
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Alert variant="info">
                        <i className="bi bi-info-circle"> </i>
                        <strong> Password Reset Mail Sent </strong>
                    </Alert>

                    We sent you an email to reset your password. Please check your inbox and follow the instructions.

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}

export default ForgotPasswordPage