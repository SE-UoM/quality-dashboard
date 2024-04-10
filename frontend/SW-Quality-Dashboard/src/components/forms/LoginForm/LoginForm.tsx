import React, { useState } from 'react';
import axios from "axios";
import { Form, Button, Alert } from "react-bootstrap";
import './LoginForm.css';
import apiUrls from "../../../assets/data/api_urls.json";
import FloatingFormInput from "../FloatingFormInput/FloatingFormInput.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function LoginForm() {
    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [loginSuccess, setLoginSuccess] = useState(false);

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
                setLoginSuccess(true);
                setErrorMessage("");
                setAccessToken(data.accessToken);
                setRefreshToken(data.refreshToken);

                // Save the tokens to local storage
                localStorage.setItem("accessToken", data.accessToken);
                localStorage.setItem("refreshToken", data.refreshToken);

                // Redirect to the dashboard after 2 seconds
                setTimeout(() => {
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
            <div className="sign-up-form">
                <h1>
                    <i className="bi bi-door-open-fill"> </i>
                    Login
                </h1>
                {error &&
                    <Alert variant="danger">
                        <i className="bi bi-exclamation-triangle-fill"> </i>
                        <strong> {errorMessage} </strong>
                    </Alert>
                }

                {loginSuccess &&
                    <Alert variant="success">
                        <i className="bi bi-check-circle-fill"> </i>
                        <strong>
                            Login successful! Redirecting...
                        </strong>
                    </Alert>
                }

                <Form className="sign-up-form-content">
                    <Form.Group controlId="formBasicEmail">
                        <FloatingFormInput
                            type="email"
                            id="emailInput"
                            placeholder="icsXXXX@uom.edu.gr"
                            isRequired={true}
                            onChange={(e) => setEmail(e.target.value)}
                            icon="bi bi-at"
                            labelText="Email"
                        />
                    </Form.Group>

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
                        <Form.Text className="text-muted">
                            <a href="/forgot-password" className="forgot-password-link">Forgot password?</a>
                        </Form.Text>
                    </Form.Group>

                    <Form.Group className="m-3" controlId="formBasicCheckbox">
                        <Form.Check
                            type="checkbox"
                            label="I am not a 🤖 (robot)."
                            required={true}
                            onChange={(e) => setIsChecked(e.target.checked)} // Update isChecked state
                        />
                    </Form.Group>

                    <Button className="sign-up-form-submit-btn " type="submit" onClick={handleLogin}>
                        <i className="bi bi-arrow-right-circle-fill"> </i>
                        Login
                    </Button>

                    <Divider />

                    <Form.Group className="m-0" controlId="formBasicRegisterUrl">
                        <Form.Label className="already-registered">
                            <i className="bi bi-link-45deg"> </i>Don't have an account? <a href="/register" className={"sign-up-link-login-form"}> Sign up here.</a>
                        </Form.Label>
                    </Form.Group>


                </Form>
            </div>
        </>
    );
}

export default LoginForm;
