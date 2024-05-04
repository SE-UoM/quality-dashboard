import React, { useState } from 'react';
import axios from "axios";
import { Form, Button, Alert } from "react-bootstrap";
import './SubmitProjectForm.css';
import apiUrls from "../../../assets/data/api_urls.json";
import FloatingFormInput from "../FloatingFormInput/FloatingFormInput.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import useAuthenticationCheck from "../../../hooks/useAuthenticationCheck.ts";
import LoadingBar from "../../ui/LoadingBar/LoadingBar.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function SubmitProjectForm() {
    const [showSimpleAlert, setShowSimpleAlert] = useState(false);
    const [simpleAlertMessage, setSimpleAlertMessage] = useState("");
    const [simpleAlertHeader, setSimpleAlertHeader] = useState("Info");
    const [simpleAlertVariant, setSimpleAlertVariant] = useState("info");
    const [simpleAlertIcon, setSimpleAlertIcon] = useState("bi bi-info-circle-fill");

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);

    const [githubUrl, setGithubUrl] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [refreshToken, setRefreshToken] = useLocalStorage("refreshToken", "");

    const handleSubmitProject = (e) => {
        e.preventDefault();

        // Make sure the url is a valid github url
        let githubUrlRegex = new RegExp("^(https:\/\/github.com\/)([a-zA-Z0-9-]+)\/([a-zA-Z0-9-]+)$");
        let isValidGithubUrl = githubUrlRegex.test(githubUrl);

        if (!isValidGithubUrl) {
            setShowSimpleAlert(true);
            setSimpleAlertIcon("bi bi-exclamation-triangle-fill");
            setSimpleAlertVariant("danger");
            setSimpleAlertHeader("Invalid Github URL");
            setSimpleAlertMessage("Please enter a valid Github URL");
            return;
        }

        let apiUrl = baseApiUrl + apiUrls.routes.startAnalysis;
        // Replace the github url with the actual url
        apiUrl = apiUrl.replace("${githubUrl}", githubUrl);

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + accessToken
        }

        setIsLoading(true);
        setShowSimpleAlert(true);
        setSimpleAlertIcon("bi bi-info-circle-fill");
        setSimpleAlertVariant("info");

        let alertMessage = "It will take a while to analyze the project depending on the size and the total commits. You can close this page, grab a coffee sit back and relax! Don't worry, we will send you an email once the analysis is complete.";
        setSimpleAlertHeader("Analyzing Project");
        setSimpleAlertMessage(alertMessage)

        axios.post(apiUrl, {}, { headers: headers })
            .then((response) => {
                setShowSimpleAlert(true);
                setSimpleAlertIcon("bi bi-check-circle-fill");
                setSimpleAlertVariant("success");

                let backendMessage = response.data.message;
                setSimpleAlertHeader(backendMessage)
                setSimpleAlertMessage("");

                setIsLoading(false);
            })
            .catch((error) => {
                console.warn("Error submitting project " + error);

                setShowSimpleAlert(true);
                setIsLoading(false);
                setSimpleAlertIcon("bi bi-exclamation-triangle-fill");
                setSimpleAlertVariant("danger");
                setSimpleAlertHeader(error.response.data.message);
                setSimpleAlertMessage(error.response.data.exceptionMessage);

                setError(true);

                // Hide the alerts after 10 seconds
                setTimeout(() => {
                    setShowSimpleAlert(false);
                    setSimpleAlertMessage("");
                }, 5000);
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
                            <div role="alert" className="alert alert-error">
                                <i className="bi bi-exclamation-triangle-fill"> </i>
                                <span> {errorMessage} </span>
                            </div>
                        }

                        {/*{loginSuccess &&*/}
                        {/*    <div role="alert" className="alert alert-success">*/}
                        {/*        <i className="bi bi-check-circle-fill"> </i>*/}
                        {/*        <span>*/}
                        {/*        Login successful! Redirecting...*/}
                        {/*    </span>*/}
                        {/*    </div>*/}
                        {/*}*/}


                        <div className="form-control">
                            <label className="input input-bordered flex items-center gap-2">
                                <i className="bi bi-envelope-fill"> </i>

                                <input
                                    type="text"
                                    className="grow"
                                    placeholder="Email"
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
                                        className="grow"
                                        placeholder="password"
                                    />

                                    <div
                                        className={"btn btn-ghost"}

                                        style={{
                                            padding: '0.5rem',
                                        }}
                                    >
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
                                />
                            </label>
                        </div>

                        <div className="form-control mt-6">
                            <button
                                className="btn btn-primary"
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

                {/*    <h1>*/}
                {/*    <i className="bi bi-folder-plus"> </i>*/}
                {/*    Submit a Project*/}
                {/*</h1>*/}

                {/*{showSimpleAlert &&*/}
                {/*    <Alert variant={simpleAlertVariant}>*/}
                {/*        <Alert.Heading>*/}
                {/*            <i className={simpleAlertIcon}> </i>*/}
                {/*            {simpleAlertHeader}*/}
                {/*        </Alert.Heading>*/}
                {/*        <strong> {simpleAlertMessage} </strong>*/}
                {/*    </Alert>*/}
                {/*}*/}

                {/*{isLoading && (*/}
                {/*    <LoadingBar />*/}
                {/*)}*/}

                {/*<Form className="sign-up-form-content">*/}
                {/*    <Form.Group controlId="formBasicGithubUrl">*/}
                {/*        <FloatingFormInput*/}
                {/*            type="url"*/}
                {/*            id="githubUrlInput"*/}
                {/*            placeholder="https://github.com/username/reponame"*/}
                {/*            isRequired={true}*/}
                {/*            onChange={(e) => setGithubUrl(e.target.value)}*/}
                {/*            icon="bi bi-github"*/}
                {/*            labelText="Github Repo URL"*/}
                {/*        />*/}
                {/*    </Form.Group>*/}

                {/*    <Button className="sign-up-form-submit-btn" type="submit" onClick={handleSubmitProject}>*/}
                {/*        <i className="bi bi-arrow-right-circle-fill"> </i>*/}
                {/*        Submit Project*/}
                {/*    </Button>*/}
                {/*</Form>*/}
        </>
    );
}

export default SubmitProjectForm;
