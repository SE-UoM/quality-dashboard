import './AdminAllProjectsPage.css'
import AdminTabContent from "../AdminTabContent/AdminTabContent.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import {useEffect} from "react";
import apiUrls from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import {Alert, Badge, Button, OverlayTrigger, Table, Tooltip} from "react-bootstrap";
import React from "react";
import TableControls from "../tables/TableControls.tsx";

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

    const [searchTerm, setSearchTerm] = React.useState('');
    const [itemsPerPage, setItemsPerPage] = React.useState(10);

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
        <AdminTabContent
            icon="bi bi-journal-code"
            title="All Projects"
        >
            <div role="alert" className="alert">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="stroke-info shrink-0 w-6 h-6"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                <span>
                    Here you can see all the projects that have been submitted for analysis.
                </span>
            </div>

            <TableControls
                setSearchTerm={setSearchTerm}
                setItemsPerPage={setItemsPerPage}
            />

        </AdminTabContent>
    )
}

export default AdminAllProjectsPage