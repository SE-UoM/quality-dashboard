import React, { useState, useEffect } from 'react';
import axios from "axios";
import {Form, Button, Alert} from "react-bootstrap";
import './SignUpForm.css';
import apiUrls from "../../../assets/data/api_urls.json";
import {isProduction, acceptedUserMailDomains} from "../../../assets/data/config.json";
import AccountVerificationModal from "../../modals/AccountVerificationModal/AccountVerificationModal.tsx";
import FloatingFormInput from "../FloatingFormInput/FloatingFormInput.tsx";

interface Organization {
    id: string;
    name: string;
}

function SignUpForm() {
    const [showModal, setShowModal] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showRepeatPassword, setShowRepeatPassword] = useState(false);

    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [organizations, setOrganizations] = useState<Organization[]>([]);
    const [baseApiUrl, setBaseApiUrl] = useState(isProduction ? apiUrls.productionBackend : apiUrls.developmentBackend);
    const [createdUserId, setCreatedUserId] = useState("");

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [selectedOrganization, setSelectedOrganization] = useState("");

    // Call API to get organizations for the dropdown
    useEffect(() => {
        // Call API to get organizations
        let url = baseApiUrl + apiUrls.routes.getAllOrganizationNames;;

        axios.get(url)
            .then((response) => {
                setOrganizations(response.data);
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);


    const handleSignUp = (e) => {
        e.preventDefault()
        console.log('Sign Up Clicked');

        // Make sure the user has entered all the required fields
        if (!email || !password || !selectedOrganization || !name) {
            setError(true);
            setErrorMessage("Please fill out all the fields");
            return;
        }

        // Make sure the email is from an accepted domain
        let domain = email.split('@')[1];
        console.log(domain)

        let domainIsAccepted = !acceptedUserMailDomains.includes(domain);
        console.log(domainIsAccepted)

        if (domainIsAccepted) {
            setError(true);
            setErrorMessage("Please use your Organization email address (" + acceptedUserMailDomains[0] + ")");
            return;
        }

        if (password.length < 6) {
            setError(true);
            setErrorMessage("Password must be at least 6 characters long");
            return;
        }

        // Make sure the passwords match
        if (password !== repeatPassword) {
            setError(true);
            setErrorMessage("Passwords do not match");
            return;
        }

        // Call the API to sign up the user
        let url = baseApiUrl + apiUrls.routes.createUser;
        let data = {
            name: name,
            email: email,
            password: password,
            organizationId: selectedOrganization
        }

        axios.post(url, data)
            .then((response) => {
                let data = response.data;
                setCreatedUserId(data.id);

                setError(false);
                setShowModal(true)
            })
            .catch((error) => {
                console.log(error);
                setError(true);
                setErrorMessage(error.response.data.message);
            });
    };

    return (
        <>
            <div className="sign-up-form">
                <AccountVerificationModal show={showModal} setShow={setShowModal} uid={createdUserId}/>

                <h1>
                    <i className="bi bi-person-fill-add"> </i>
                    Sign Up
                </h1>
                {error &&
                    <Alert variant="danger">
                        <i className="bi bi-exclamation-triangle-fill"> </i>
                        <strong> {errorMessage} </strong>
                    </Alert>
                }

                <Form className="sign-up-form-content">
                    <Form.Group controlId="formBasicName">
                        <FloatingFormInput
                            type="text"
                            id="nameInput"
                            placeholder="Jane Doe"
                            isRequired={true}
                            onChange={(e) => setName(e.target.value)}
                            icon="bi bi-person-circle"
                            labelText="Name"
                        />
                    </Form.Group>

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

                    <Form.Group controlId="formBasicOrganization">
                        <div className="form-floating">
                            <select
                                className="form-select"
                                id="floatingSelect"
                                aria-label="Floating label select example"
                                required={true}
                                onChange={(e) => setSelectedOrganization(e.target.value)}
                            >
                                <option selected>Select Organization</option>
                                {organizations.map((organization) => {
                                    return <option key={organization.id} value={organization.id}>{organization.name}</option>
                                })}
                            </select>
                            <label htmlFor="floatingSelect">
                                <i className="bi bi-building-fill"> </i>
                                Organization
                            </label>
                        </div>
                    </Form.Group>

                    <Form.Group className="m-3" controlId="formBasicCheckbox">
                        <Form.Check type="checkbox" label="I am not a 🤖 (robot). " required={true}/>
                    </Form.Group>

                    <Button className="sign-up-form-submit-btn" type="submit" onClick={handleSignUp}>
                        <i className="bi bi-arrow-right-circle-fill"> </i>
                        Sign Up
                    </Button>
                </Form>
            </div>
        </>
    );
}

export default SignUpForm;
