import './AdminAllProjectsPage.css'
import AdminTabContent from "../AdminTabContent/AdminTabContent.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import {Alert, Badge, Button, OverlayTrigger, Table, Tooltip} from "react-bootstrap";
import React from "react";
import TableControls from "../tables/TableControls.tsx";
import useAxios from "../../../hooks/useAxios.ts";
import UserTableItem from "../tables/UserTableItem.tsx";
import {truncateString} from "../../../utils/textUtils.ts";
import ProjectTableItem from "../tables/ProjectTableItem.tsx";

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
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const [currentItems, setCurrentItems] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    const {data: allProjectsData, error: allProjectsError, loading: allProjectsLoading, errorMessage: allProjectsErrorMessage} =
        useAxios(baseUrl + apiUrls.routes.projects.allByOrgId.replace(':organizationId', jwtDecode(accessToken).organizationId), accessToken)

    useEffect(() => {
        if (!allProjectsData) return;

        // Logic to get current items based on pagination
        // Logic to get current items based on pagination
        const indexOfLastItem = currentPage * itemsPerPage;
        const indexOfFirstItem = indexOfLastItem - itemsPerPage;
        const currentItems = allProjectsData.slice(indexOfFirstItem, indexOfLastItem);

        // Logic to search for items
        if (searchTerm) {
            const filteredItems = allProjectsData.filter(item => {
                return item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    item.organizationName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    item.repoUrl.toLowerCase().includes(searchTerm.toLowerCase())
            })

            setCurrentItems(filteredItems);
            return;
        }
        setCurrentItems(currentItems);

    }, [allProjectsData, currentPage, itemsPerPage, searchTerm]);

    return (
        <AdminTabContent
            icon="bi bi-layer-forward"
            title="Submitted Projects"
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
                    {projects && currentItems.map(
                        project => (
                            <ProjectTableItem project={project} />
                        )
                    )}
                    </tbody>
                </table>

                {/* Pagination controls */}
                {allProjectsData && allProjectsData.length > itemsPerPage &&
                    <div
                        style={{
                            width: "100%",
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            marginTop: "1vh"
                        }}
                    >
                        <div className="join">
                            {Array.from({ length: Math.ceil(allProjectsData.length / itemsPerPage) }).map((_, index) => (
                                <button className={
                                    index === currentPage - 1
                                        ? "join-item btn btn-primary"
                                        : "join-item btn"
                                }
                                        onClick={() => setCurrentPage(index + 1)}
                                >
                                    {index + 1}
                                </button>
                            ))}
                        </div>
                    </div>
                }

            </div>

        </AdminTabContent>
    )
}

export default AdminAllProjectsPage