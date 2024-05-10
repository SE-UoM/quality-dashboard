import './AdminPendingProjectsPage.css'
import AdminTabContent from "../AdminTabContent/AdminTabContent.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import React, {useEffect} from "react";
import {Alert, Button, Table} from "react-bootstrap";
import apiUrl from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function AdminPendingProjectsPage({updateCount}) {
    const [accessToken, setAccessToken] = useLocalStorage('accessToken', '')
    const [projects, setProjects] = React.useState([])
    const [alert, setAlert] = React.useState(false)
    const [alertVariant, setAlertVariant] = React.useState('danger')
    const [alertMessage, setAlertMessage] = React.useState('Error fetching projects')

    useEffect(() => {
        let url = baseUrl + apiUrl.routes.projects.allByOrgIdAndStatus;

        let userOrganization = jwtDecode(accessToken).organizationId;

        url = url.replace(':organizationId', userOrganization);
        url = url.replace(':status', 'ANALYSIS_TO_BE_REVIEWED');

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.get(url, { headers: headers })
            .then(response => {
                setProjects(response.data)
                updateCount(projects.length);
            })
            .catch(error => {
                console.error(error)
                setAlert(true)
                setAlertVariant('danger')
                setAlertMessage('Error fetching projects')
            })
    }, []);

    const approveProject = (projectId, projectUrl) => {
        // Show the alert to let the user know something is happening
        setAlert(true)
        setAlertVariant('info')
        setAlertMessage('Approving project...')

        // First Authorize the project for analysis
        let url = baseUrl + apiUrl.routes.admin.authorizeProject;
        url = url.replace(':projectId', projectId);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.put(url, {}, { headers: headers })
            .then(response => {
                setAlert(true)
                setAlertVariant('success')
                setAlertMessage('Project approved successfully. Starting analysis...')

                //Now remove the project from the pending list
                let newProjects = projects.filter(project => project.id !== projectId)
                setProjects(newProjects)

                let analysisUrl = baseUrl + apiUrl.routes.startAnalysis;
                analysisUrl = analysisUrl.replace('${githubUrl}', projectUrl);

                let analysisHeaders = {
                    'Authorization': 'Bearer ' + accessToken,
                    'Content-Type': 'application/json'
                }

                axios.post(analysisUrl, {}, { headers: analysisHeaders })
                    .then(response => {
                        setAlert(true)
                        setAlertVariant('success')
                        setAlertMessage('Analysis completed successfully')
                    })
                    .catch(error => {
                        console.error(error)
                        setAlert(true)
                        setAlertVariant('danger')
                        setAlertMessage('Error starting analysis')
                    })
            })
            .catch(error => {
                console.error(error)
                setAlert(true)
                setAlertVariant('danger')
                setAlertMessage('Error approving project')
            })
    }

    return (
        <AdminTabContent
            icon="bi bi-exclamation-octagon-fill"
            title="Pending Projects"
        >
            <p>
                Here you can view all the projects that are pending approval. <br/>
                These projects have more than 50 commits so you need to approve them before they can be analyzed.
            </p>
        </AdminTabContent>
    )
}

export default AdminPendingProjectsPage