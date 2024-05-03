import React, { useState, useEffect } from 'react';
import axios from "axios";
import {Form, Button, Alert} from "react-bootstrap";
import './SignUpForm.css';
import apiUrls from "../../../assets/data/api_urls.json";
import {acceptedUserMailDomains} from "../../../assets/data/config.json";
import AccountVerificationModal from "../../modals/AccountVerificationModal/AccountVerificationModal.tsx";
import FloatingFormInput from "../FloatingFormInput/FloatingFormInput.tsx";
import uomLogo from "../../../assets/img/uom_logo.png";

interface Organization {
    id: string;
    name: string;
}

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function SignUpForm() {
    const [showModal, setShowModal] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showRepeatPassword, setShowRepeatPassword] = useState(false);

    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const [organizations, setOrganizations] = useState<Organization[]>([]);
    const [createdUserId, setCreatedUserId] = useState("");

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [selectedOrganization, setSelectedOrganization] = useState("");
    const [imNotARobot, setImNotARobot] = useState(false);

    // Call API to get organizations for the dropdown
    useEffect(() => {
        // Call API to get organizations
        let url = baseApiUrl + apiUrls.routes.getAllOrganizationNames;

        axios.get(url)
            .then((response) => {
                setOrganizations(response.data);
            })
            .catch((error) => {
                console.warn("Sign Up API Error: " + error);
            });
    }, []);


    const handleSignUp = (e) => {
        e.preventDefault()

        // Make sure the user has entered all the required fields
        if (!email || !password || !selectedOrganization || !name) {
            setError(true);
            setErrorMessage("Please fill out all the fields");
            return;
        }

        // Make sure the email is from an accepted domain
        let domain = email.split('@')[1];

        let domainIsAccepted = !acceptedUserMailDomains.includes(domain);

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

        // Make sure the user is not a robot
        if (!imNotARobot) {
            setError(true);
            setErrorMessage("Please check the checkbox.");
            return;
        }

        setLoading(true);

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
                setLoading(false);
            })
            .catch((error) => {
                console.warn(error);
                setError(true);
                setLoading(false);
                setShowModal(false);
                setErrorMessage(error.response.data.message);
            });
    };

    return (
        <>
            <AccountVerificationModal show={showModal} setShow={setShowModal} uid={createdUserId}/>

            <div className="card shrink-0 w-full max-w-sm bg-base-100 w-100">
                <h1
                    className="text-5xl font-bold m-2"
                    style={{textAlign: 'center'}}
                >
                    <i className="bi bi-person-fill-add"> </i> Sign Up
                </h1>

                <form className="card-body w-100">
                    {error &&
                        <div role="alert" className="alert alert-error">                            <i className="bi bi-exclamation-triangle-fill"> </i>
                            <span> {errorMessage} </span>
                        </div>
                    }

                    <div className="form-control">
                        <label className="input input-bordered flex items-center gap-2">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4 opacity-70"><path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z" /></svg>
                            <input
                                type="text"
                                className="grow"
                                placeholder="Name"
                                onChange={(e) => setName(e.target.value)}
                            />
                        </label>
                    </div>

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
                                <input
                                    type={showPassword ? "text" : "password"}
                                    className="grow"
                                    placeholder="Password"
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
                                <input
                                    type={showRepeatPassword ? "text" : "password"}
                                    className="grow"
                                    placeholder="Repeat Password"
                                    onChange={(e) => setRepeatPassword(e.target.value)}
                                />

                                <div
                                    className={"btn btn-ghost"}
                                    onClick={(e) => {
                                        setShowRepeatPassword(!showRepeatPassword)
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
                        <select className="select select-bordered w-full max-w-xs" onChange={(e) => setSelectedOrganization(e.target.value)}>
                            <option disabled selected>Select Organization</option>

                            {organizations.map((organization) => {
                                return (
                                    <option key={organization.id} value={organization.id}>
                                        {organization.name}
                                    </option>
                                )
                            })}
                        </select>
                    </div>

                    <div className="form-control">
                        <label className="label cursor-pointer">
                                <span className="label-text">
                                    I am not a <i className={"bi bi-robot"}> </i> (robot).
                                </span>
                            <input
                                type="checkbox"
                                className="checkbox checkbox-primary"
                                onChange={(e) => setImNotARobot(e.target.checked)}
                            />
                        </label>
                    </div>

                    <div className="form-control mt-6">
                        <button
                            className="btn btn-primary"
                            onClick={handleSignUp}
                        >
                            {!loading && <i className="bi bi-door-open-fill"> </i>}
                            {loading && <span className="loading loading-dots loading-xs"></span>}
                            Sign Up
                        </button>
                    </div>

                    <div className="form-control mt-6">
                        Don't have an account? <a href="/register" className="link">Sign up here.</a>
                    </div>
                </form>
            </div>
            {/*<div className="sign-up-form">*/}
            {/*    <AccountVerificationModal show={showModal} setShow={setShowModal} uid={createdUserId}/>*/}

            {/*    <h1>*/}
            {/*        <i className="bi bi-person-fill-add"> </i>*/}
            {/*        Sign Up*/}
            {/*    </h1>*/}
            {/*    {error &&*/}
            {/*        <Alert variant="danger">*/}
            {/*            <i className="bi bi-exclamation-triangle-fill"> </i>*/}
            {/*            <strong> {errorMessage} </strong>*/}
            {/*        </Alert>*/}
            {/*    }*/}

            {/*    <Form className="sign-up-form-content">*/}
            {/*        <Form.Group controlId="formBasicName">*/}
            {/*            <FloatingFormInput*/}
            {/*                type="text"*/}
            {/*                id="nameInput"*/}
            {/*                placeholder="Jane Doe"*/}
            {/*                isRequired={true}*/}
            {/*                onChange={(e) => setName(e.target.value)}*/}
            {/*                icon="bi bi-person-circle"*/}
            {/*                labelText="Name"*/}
            {/*            />*/}
            {/*        </Form.Group>*/}

            {/*        <Form.Group controlId="formBasicEmail">*/}
            {/*            <div className="form-floating mb-3">*/}
            {/*                <input*/}
            {/*                    type="email"*/}
            {/*                    className="form-control"*/}
            {/*                    id="emailInput"*/}
            {/*                    placeholder="name@example.com"*/}
            {/*                    required={true}*/}
            {/*                    onChange={(e) => setEmail(e.target.value)}*/}
            {/*                />*/}
            {/*                <label htmlFor="floatingInput">*/}
            {/*                    <i className="bi bi-at"> </i>*/}
            {/*                    Email*/}
            {/*                </label>*/}
            {/*            </div>*/}

            {/*        </Form.Group>*/}

            {/*        <Form.Group controlId="formBasicPassword">*/}
            {/*            <div className="form-pass-container">*/}
            {/*                <FloatingFormInput*/}
            {/*                    type={showPassword ? "text" : "password"}*/}
            {/*                    id="passInput"*/}
            {/*                    placeholder="something safe"*/}
            {/*                    isRequired={true}*/}
            {/*                    onChange={(e) => setPassword(e.target.value)}*/}
            {/*                    icon="bi bi-shield-lock-fill"*/}
            {/*                    labelText="Password"*/}
            {/*                />*/}

            {/*                <Button*/}
            {/*                    variant="outline-secondary"*/}
            {/*                    className="show-password-btn"*/}
            {/*                    onClick={() => setShowPassword(!showPassword)}>*/}
            {/*                    {showPassword ? <i className="bi bi-eye-slash-fill"> </i> : <i className="bi bi-eye-fill"> </i>}*/}
            {/*                </Button>*/}
            {/*            </div>*/}

            {/*        </Form.Group>*/}

            {/*        <Form.Group controlId="formBasicRepeatPassword">*/}
            {/*            <div className="form-pass-container">*/}
            {/*                <FloatingFormInput*/}
            {/*                    type={showRepeatPassword ? "text" : "password"}*/}
            {/*                    id="repeatPassInput"*/}
            {/*                    placeholder="repeat your password"*/}
            {/*                    isRequired={true}*/}
            {/*                    onChange={(e) => setRepeatPassword(e.target.value)}*/}
            {/*                    icon="bi bi-shield-lock-fill"*/}
            {/*                    labelText="Repeat Password"*/}
            {/*                />*/}

            {/*                <Button*/}
            {/*                    variant="outline-secondary"*/}
            {/*                    className="show-password-btn"*/}
            {/*                    onClick={() => setShowRepeatPassword(!showRepeatPassword)}>*/}
            {/*                    {showRepeatPassword ? <i className="bi bi-eye-slash-fill"> </i> : <i className="bi bi-eye-fill"> </i>}*/}
            {/*                </Button>*/}
            {/*            </div>*/}
            {/*        </Form.Group>*/}

            {/*        <Form.Group controlId="formBasicOrganization">*/}
            {/*            <div className="form-floating">*/}
            {/*                <select*/}
            {/*                    className="form-select"*/}
            {/*                    id="floatingSelect"*/}
            {/*                    aria-label="Floating label select example"*/}
            {/*                    required={true}*/}
            {/*                    onChange={(e) => setSelectedOrganization(e.target.value)}*/}
            {/*                >*/}
            {/*                    <option selected>Select Organization</option>*/}
            {/*                    {organizations.map((organization) => {*/}
            {/*                        return <option key={organization.id} value={organization.id}>*/}
            {/*                                    <img src={uomLogo}/>*/}
            {/*                                    {organization.name}*/}
            {/*                               </option>*/}
            {/*                    })}*/}
            {/*                </select>*/}
            {/*                <label htmlFor="floatingSelect">*/}
            {/*                    <i className="bi bi-building-fill"> </i>*/}
            {/*                    Organization*/}
            {/*                </label>*/}
            {/*            </div>*/}
            {/*        </Form.Group>*/}

            {/*        <Form.Group className="m-3" controlId="formBasicCheckbox">*/}
            {/*            <Form.Check type="checkbox" label="I am not a 🤖 (robot). " required={true}/>*/}
            {/*        </Form.Group>*/}

            {/*        <Button className="sign-up-form-submit-btn" type="submit" onClick={handleSignUp}>*/}
            {/*            <i className="bi bi-arrow-right-circle-fill"> </i>*/}
            {/*            Sign Up*/}
            {/*        </Button>*/}

            {/*        <Divider/>*/}

            {/*        <Form.Group className="m-0" controlId="formBasicRegisterUrl">*/}
            {/*            <Form.Label className="already-registered">*/}
            {/*                <i className="bi bi-link-45deg"> </i>Already have an account? <a href="/login" className={"sign-up-link-login-form"}> Login Instead.</a>*/}
            {/*            </Form.Label>*/}
            {/*        </Form.Group>*/}
            {/*    </Form>*/}
            {/*</div>*/}
        </>
    );
}

export default SignUpForm;
