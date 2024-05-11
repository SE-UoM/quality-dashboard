import './AdminAllUsersPage.css'
import AdminTabContent from "../AdminTabContent/AdminTabContent.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import {useEffect, useState} from "react";
import apiUrls from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import {Alert, Badge, Button, Form, Modal, Table} from "react-bootstrap";
import React from "react";
import AllUsersTable from "../tables/AllUsersTable/AllUsersTable.tsx";
import EditUserModal from "../../../components/modals/EditUserModal/EditUserModal.tsx";
import useAxios from "../../../hooks/useAxios.ts";
import UserTableItem from "../tables/UserTableItem.tsx";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function AdminAllUsersPage() {
    const [accessToken, setAccessToken] = useLocalStorage('accessToken', '')
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [currentItems, setCurrentItems] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    const {data: allUsersData, error: allUsersError, loading: allUsersLoading, errorMessage: allUsersErrorMessage} =
        useAxios(baseUrl + apiUrls.routes.admin.getAllUsersByOrgId.replace(':organizationId', jwtDecode(accessToken).organizationId), accessToken)

    const [updated, setUpdated] = useState(false)

    useEffect(() => {
        if (!allUsersData) return;

        // Logic to get current items based on pagination
        const indexOfLastItem = currentPage * itemsPerPage;
        const indexOfFirstItem = indexOfLastItem - itemsPerPage;
        const currentItems = allUsersData.slice(indexOfFirstItem, indexOfLastItem);

        // Logic to search for items
        if (searchTerm) {
            const filteredItems = allUsersData.filter(item => {
                return item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    item.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    item.roles.toLowerCase().includes(searchTerm.toLowerCase())
            })

            setCurrentItems(filteredItems);
            return;
        }
        setCurrentItems(currentItems);

    }, [allUsersData, currentPage, itemsPerPage, searchTerm]);

    return (
        <AdminTabContent
            icon="bi bi-people-fill"
            title="All Organization Users"
        >

            <div className="card bg-base-content/10"
                 style={{
                     padding: "2vh",
                     marginTop: "2vh",
                     display: "flex",
                     flexDirection: "row",
                     gap: "2vh",
                     justifyContent: "space-between",
                     alignItems: "center"
                 }}
            >
                <label className="form-control" style={{width: "85%"}}>
                    <div className="label">
                        <span className="label-text"><strong>
                            <i className="bi bi-search"> </i>
                            Search
                        </strong></span>
                    </div>
                    <input style={{width: "100%"}} type="text" placeholder="Type here" className="input input-bordered input-xs" onChange={(e) => setSearchTerm(e.target.value)} />
                </label>
                <label className="form-control" style={{width: "auto"}}>
                    <div className="label">
                        <span className="label-text"><strong>
                            <i className="bi bi-filter"> </i>
                            Items per Page
                        </strong></span>
                    </div>
                        <select className="select select-bordered select-xs"  onChange={(e) => setItemsPerPage(parseInt(e.target.value))}>
                            <option disabled>Page Size</option>
                            <option value={5}>5</option>
                            <option value={10}>10</option>
                            <option value={15}>15</option>
                            <option value={20}>20</option>
                            <option value={25}>25</option>
                            <option value={30}>30</option>
                            <option value={35}>35</option>
                            <option value={40}>40</option>
                            <option value={45}>45</option>
                            <option value={50}>50</option>
                        </select>
                </label>

                <label className="form-control tooltip tooltip-bottom" style={{width: "2%"}} data-tip={"Refresh table"}>
                    <button className="btn btn-primary btn-xs" onClick={() => window.location.reload()}>
                        <i className="bi bi-arrow-clockwise"> </i>
                    </button>
                </label>
            </div>

            <div className="overflow-x-auto">
                <table className="table">
                    {/* head */}
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Roles</th>
                        <th>Verified</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {allUsersData && currentItems.map(
                        user => (
                            <UserTableItem
                                userID={user.id}
                                userImg={`https://ui-avatars.com/api/?name=${user.name}&background=random`}
                                userName={user.name}
                                userOrg={user.organizationName}
                                userEmail={user.email}
                                userRoles={
                                    user.roles.split(',').map(role => role.trim())
                                }
                                userVerified={user.verified}
                            />
                        )
                    )}
                    </tbody>
                    {/* foot */}
                    <tfoot>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Roles</th>
                        <th>Verified</th>
                        <th>Actions</th>
                    </tr>
                    </tfoot>
                </table>

                {/* Pagination controls */}
                {allUsersData && allUsersData.length > itemsPerPage && (
                    <div
                        style={{
                            width: "100%",
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                        }}
                    >
                        <div className="join">
                            {Array.from({ length: Math.ceil(allUsersData.length / itemsPerPage) }).map((_, index) => (
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
                )}
            </div>

        </AdminTabContent>
    )
}

export default AdminAllUsersPage
