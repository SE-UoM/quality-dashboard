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
    const [errorMessage, setErrorMessage] = useState("");
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
            setErrorMessage("Please enter a valid Github URL");
            return;
        }

        let apiUrl = baseApiUrl + apiUrls.routes.startAnalysis + "?github_url=" + githubUrl;

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + accessToken
        }

        setIsLoading(true);

        axios.post(apiUrl, {}, { headers: headers })
            .then((response) => {
                console.log(response);
                console.log("Project submitted successfully");
                setIsLoading(false);
            })
            .catch((error) => {
                console.log(error);
                setError(true);
                setIsLoading(false);
                setErrorMessage("Error submitting project. Please try again.");
            });

        // Hide the error message after 5 seconds
        setTimeout(() => {
            setError(false);
            setErrorMessage("");
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
                        <strong> {errorMessage} </strong>
                    </Alert>
                }

                {isLoading && (
                    <div className="analyzing-progress">
                        <Alert variant="info">
                            <i className="bi bi-info-circle-fill"> </i>
                            <strong> Analyzing Project... This might take a while! </strong>
                        </Alert>
                        <LoadingBar />
                    </div>
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
