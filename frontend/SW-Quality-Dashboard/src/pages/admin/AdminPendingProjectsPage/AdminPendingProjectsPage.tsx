import './AdminPendingProjectsPage.css'
import AdminTabContent from "../AdminTabContent/AdminTabContent.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import React, {useEffect} from "react";
import {Alert, Button, Table} from "react-bootstrap";
import apiUrl from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import useAxios from "../../../hooks/useAxios.ts";
import TableControls from "../tables/TableControls.tsx";
import ProjectTableItem from "../tables/ProjectTableItem.tsx";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function AdminPendingProjectsPage({updateCount}) {
    const [accessToken, setAccessToken] = useLocalStorage('accessToken', '')
    const [currentPage, setCurrentPage] = React.useState(1);
    const [itemsPerPage, setItemsPerPage] = React.useState(10);
    const [currentItems, setCurrentItems] = React.useState([]);
    const [searchTerm, setSearchTerm] = React.useState('');

    const {data: pendingProjectsData, error: pendingProjectsError, loading: pendingProjectsLoading, errorMessage: pendingProjectsErrorMessage} =
        useAxios(baseUrl + apiUrl.routes.projects.allByOrgIdAndStatus.replace(':organizationId', jwtDecode(accessToken).organizationId).replace(':status', 'ANALYSIS_TO_BE_REVIEWED'), accessToken)

    useEffect(() => {
        if (!pendingProjectsData) return;

        // Logic to get current items based on pagination
        const indexOfLastItem = currentPage * itemsPerPage;
        const indexOfFirstItem = indexOfLastItem - itemsPerPage;
        const currentItems = pendingProjectsData.slice(indexOfFirstItem, indexOfLastItem);

        // Logic to search for items
        if (searchTerm) {
            const filteredItems = pendingProjectsData.filter(item => {
                return item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    item.repoUrl.toLowerCase().includes(searchTerm.toLowerCase())
            })

            setCurrentItems(filteredItems);
            return;
        }

        setCurrentItems(currentItems);
    }, [pendingProjectsData]);


    return (
        <AdminTabContent
            icon="bi bi-exclamation-circle-fill"
            title="Pending Projects"
        >
            <div role="alert" className="alert">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="stroke-info shrink-0 w-6 h-6"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                <span>
                    Here you can view all the projects that are pending approval. <br/>
                    These projects have more than 50 commits so you need to approve them before they can be analyzed.
                </span>
            </div>
            <TableControls
                setSearchTerm={setSearchTerm}
                setItemsPerPage={setItemsPerPage}
            />
            <div className="overflow-x-auto">
                <table className="table">
                    {/* head */}
                    <thead>
                    <tr>
                        <th>Project Name</th>
                        <th>Github URL</th>
                        <th>Review</th>
                        <th>Analysis Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pendingProjectsData && currentItems.map(
                        project => (
                            <ProjectTableItem project={project} />
                        )
                    )}
                    </tbody>
                </table>
            </div>
        </AdminTabContent>
    )
}

export default AdminPendingProjectsPage