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

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function TableItem({userImg, userName, userOrg, userEmail, userRoles, userVerified, key}) {
    return (
        <>
            <tr key={key}>
                <td>
                    <div className="flex items-center gap-3">
                        <div className="avatar">
                            <div className="mask mask-squircle w-12 h-12">
                                <img src={userImg} alt="Avatar Tailwind CSS Component" />
                            </div>
                        </div>
                        <div>
                            <div className="font-bold">{userName}</div>
                            <div className="text-sm opacity-50">{userOrg}</div>
                        </div>
                    </div>
                </td>
                <td>
                    <a className="link link-info" href={`mailto:${userEmail}`}>
                        {userEmail}
                    </a>
                </td>
                <td>
                    <div
                        style={{
                            display: "flex",
                            gap: "1vh"
                        }}
                    >
                        {userRoles.map(role => {
                            if (role === 'PRIVILEGED' || role === 'ADMIN') {
                                return <span className="badge badge-warning badge-sm mr-2">{role}</span>
                            }

                            return <span className="badge badge-info  badge-sm">{role}</span>
                        })}
                    </div>
                </td>
                <th>
                    {userVerified ? (
                        <i className="bi bi-patch-check text-success" style={{fontSize: "4vh"}}></i>
                    ) :(
                        <i className="bi bi-x-circle text-error"  style={{fontSize: "4vh"}}> </i>
                    )}
                </th>

                <td>
                    <div className="flex items-center gap-3">
                        <button className="btn btn-sm btn-primary">
                            <i className="bi bi-pencil-fill"> </i>
                            Edit
                        </button>
                        <button className="btn btn-sm btn-error">
                            <i className="bi bi-trash-fill"> </i>
                            Delete
                        </button>
                    </div>
                </td>
            </tr>
        </>
    )
}

function AdminAllUsersPage() {
    const [accessToken, setAccessToken] = useLocalStorage('accessToken', '')
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [currentItems, setCurrentItems] = useState([]);

    const {data: allUsersData, error: allUsersError, loading: allUsersLoading, errorMessage: allUsersErrorMessage} =
        useAxios(baseUrl + apiUrls.routes.admin.getAllUsersByOrgId.replace(':organizationId', jwtDecode(accessToken).organizationId), accessToken)

    useEffect(() => {
        if (!allUsersData) return;

        // Logic to get current items based on pagination
        const indexOfLastItem = currentPage * itemsPerPage;
        const indexOfFirstItem = indexOfLastItem - itemsPerPage;
        const currentItems = allUsersData.slice(indexOfFirstItem, indexOfLastItem);

        setCurrentItems(currentItems);

    }, [allUsersData, currentPage, itemsPerPage]);

    return (
        <AdminTabContent
            icon="bi bi-people-fill"
            title="All Organization Users"
        >

            <div className="card bg-base-content/10"
                 style={{
                     padding: "2vh",
                     marginTop: "2vh"
                 }}
            >
                <div>
                    <div className="tooltip tooltip-top" data-tip={"Page Size"}>
                        <i className="bi bi-list-ol mr-2" ></i>
                        <select className="select select-bordered select-xs max-w-xs" style={{width: "5vw"}} onChange={(e) => setItemsPerPage(parseInt(e.target.value))}>
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
                    </div>
                </div>
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
                            <TableItem
                                key={user.id}
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
