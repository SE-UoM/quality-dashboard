import './AdminAllProjectsPage.css'
import AdminPage from "../AdminPage/AdminPage.tsx";
import useLocalStorage from "../../hooks/useLocalStorage.ts";
import {useEffect} from "react";
import apiUrls from "../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import {Alert, Badge, Table} from "react-bootstrap";
import React from "react";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const statusColors = {
    'ANALYSIS_IN_PROGRESS': '#00bbff',
    'ANALYSIS_COMPLETED': '#4cc902',
    'ANALYSIS_FAILED': '#FF0000',
    'ANALYSIS_SKIPPED': '#646464',
    'ANALYSIS_TO_BE_REVIEWED': '#FFA500',
    'ANALYSIS_NOT_STARTED': '#383737',
    'ANALYSIS_STARTED': '#4e418f',
    'ANALYSIS_READY': '#008000',
}

function AdminAllProjectsPage() {
    const [accessToken, setAccessToken] = useLocalStorage('accessToken', '')
    const [projects, setProjects] = React.useState([])
    const [error, setError] = React.useState(false)

    useEffect(() => {
        // Call the api to get all projects
        let url = baseUrl + apiUrls.routes.projects.allByOrgId;

        // get the organization id from the access token
        let userOrganization = jwtDecode(accessToken).organizationId;

        // Replace the placeholder with the actual organization id
        url = url.replace(':organizationId', userOrganization);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.get(url, { headers: headers })
            .then(response => {
                setProjects(response.data)
            })
            .catch(error => {
                console.error(error)
                setError(true)
            })
    }, [accessToken]);

    return (
        <AdminPage
            icon="bi bi-journal-code"
            title="All Projects"
        >
            {error && (
                <Alert variant="danger">
                    Error fetching projects
                </Alert>
            )}

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Index</th>
                    <th>PID</th>
                    <th>Name</th>
                    <th>Repository URL</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                {projects.map(project => (
                    <tr key={project.id}>
                        <td>{projects.indexOf(project) + 1}</td>
                        <td>{project.id}</td>
                        <td>{project.name}</td>
                        <td>
                            <a href={project.repoUrl}>{project.repoUrl}</a>
                        </td>
                        <td className={"status " + project.status.toLowerCase()}>
                            <div
                                className="dashboard-pill"
                                style={{backgroundColor: statusColors[project.status]}}
                            >
                                {project.status.replace(/_/g, ' ')}
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

        </AdminPage>
    )
}

export default AdminAllProjectsPage