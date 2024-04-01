import React, { useState } from 'react';
import axios from "axios";
import { Form, Button, Alert } from "react-bootstrap";
import './SubmitProjectForm.css';
import apiUrls from "../../../assets/data/api_urls.json";
import { isProduction, acceptedUserMailDomains } from "../../../assets/data/config.json";
import FloatingFormInput from "../FloatingFormInput/FloatingFormInput.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import useAuthenticationCheck from "../../../hooks/useAuthenticationCheck.ts";
import LoadingBar from "../../ui/LoadingBar/LoadingBar.tsx";

function SubmitProjectForm() {
    const [error, setError] = useState(false);
    const [errorAlertMessage, setErrorAlertMessage] = useState("");

    const [showSimpleAlert, setShowSimpleAlert] = useState(false);
    const [simpleAlertMessage, setSimpleAlertMessage] = useState("");
    const [simpleAlertHeader, setSimpleAlertHeader] = useState("Info");
    const [simpleAlertVariant, setSimpleAlertVariant] = useState("info");
    const [simpleAlertIcon, setSimpleAlertIcon] = useState("bi bi-info-circle-fill");

    const [baseApiUrl, setBaseApiUrl] = useState(isProduction ? apiUrls.productionBackend : apiUrls.developmentBackend);
    const [githubUrl, setGithubUrl] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const [accessToken, setAccessToken] = useLocalStorage("accessToken", "");
    const [refreshToken, setRefreshToken] = useLocalStorage("refreshToken", "");

    const handleSubmitProject = (e) => {
        e.preventDefault();
        console.log("Submit button clicked");

        // Make sure the url is a valid github url
        let githubUrlRegex = new RegExp("^(https:\/\/github.com\/)([a-zA-Z0-9-]+)\/([a-zA-Z0-9-]+)$");
        let isValidGithubUrl = githubUrlRegex.test(githubUrl);

        if (!isValidGithubUrl) {
            setError(true);
            setErrorAlertMessage("Please enter a valid Github URL");
            return;
        }

        let apiUrl = baseApiUrl + apiUrls.routes.startAnalysis + "?github_url=" + githubUrl;

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + accessToken
        }

        setIsLoading(true);
        setShowSimpleAlert(true);
        setSimpleAlertIcon("bi bi-info-circle-fill");
        setSimpleAlertVariant("info");
        setSimpleAlertHeader("Submitting Project");
        setSimpleAlertMessage("Please wait...")

        axios.post(apiUrl, {}, { headers: headers })
            .then((response) => {
                setShowSimpleAlert(true);
                setSimpleAlertIcon("bi bi-check-circle-fill");
                setSimpleAlertVariant("success");

                let backendMessage = response.data.message;
                setSimpleAlertHeader(backendMessage)
                setSimpleAlertMessage("It will take a while to analyze the project depending on the size and the total commits.");

                setIsLoading(false);
            })
            .catch((error) => {
                console.log(error);
                setError(true);
                setIsLoading(false);
                setSimpleAlertMessage("Error submitting project. Please try again.");
            });

        // Hide the alerts after 10 seconds
        setTimeout(() => {
            setError(false);
            setErrorAlertMessage("");

            setShowSimpleAlert(false);
            setSimpleAlertMessage("");
        }, 5000);
    };

    return (
        <>
            <div className="submit-project-form">
                <h1>
                    <i className="bi bi-folder-plus"> </i>
                    Submit a Project
                </h1>

                {error &&
                    <Alert variant="danger">
                        <i className="bi bi-exclamation-triangle-fill"> </i>
                        <strong> {errorAlertMessage} </strong>
                    </Alert>
                }

                {showSimpleAlert &&
                    <Alert variant={simpleAlertVariant}>
                        <Alert.Heading>
                            <i className={simpleAlertIcon}> </i>
                            {simpleAlertHeader}
                        </Alert.Heading>
                        <strong> {simpleAlertMessage} </strong>
                    </Alert>
                }

                {isLoading && (
                    <LoadingBar />
                )}

                <Form className="sign-up-form-content">
                    <Form.Group controlId="formBasicGithubUrl">
                        <FloatingFormInput
                            type="url"
                            id="githubUrlInput"
                            placeholder="https://github.com/username/reponame"
                            isRequired={true}
                            onChange={(e) => setGithubUrl(e.target.value)}
                            icon="bi bi-github"
                            labelText="Github Repo URL"
                        />
                    </Form.Group>

                    <Button className="sign-up-form-submit-btn" type="submit" onClick={handleSubmitProject}>
                        <i className="bi bi-arrow-right-circle-fill"> </i>
                        Submit Project
                    </Button>
                </Form>
            </div>
        </>
    );
}

export default SubmitProjectForm;
