import './AdminAllUsersPage.css'
import AdminTabContent from "../AdminTabContent/AdminTabContent.tsx";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import {useEffect} from "react";
import apiUrls from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";
import {Alert, Badge, Button, Form, Modal, Table} from "react-bootstrap";
import React from "react";
import AllUsersTable from "../tables/AllUsersTable/AllUsersTable.tsx";
import EditUserModal from "../../../components/modals/EditUserModal/EditUserModal.tsx";
import useAxios from "../../../hooks/useAxios.ts";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function TableItem({userImg, userName, userOrg, userEmail, userRoles, userVerified}) {
    return (
        <>
            <tr>
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

                            return <span className="badge badge-primary badge-sm">{role}</span>
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
    const [users, setUsers] = React.useState([])
    const [error, setError] = React.useState(false)
    const [myEmail, setMyEmail] = React.useState('')

    const [showEditUserModal, setShowEditUserModal] = React.useState(false)
    const [editUser, setEditUser] = React.useState({})
    const [editedEmail, setEditedEmail] = React.useState('')
    const [editedName, setEditedName] = React.useState('')
    const [editAlert, setEditAlert] = React.useState(false)
    const [editAlertMessage, setEditAlertMessage] = React.useState('')
    const [editAlertVariant, setEditAlertVariant] = React.useState('')

    const [showDeleteUserModal, setShowDeleteUserModal] = React.useState(false)
    const [deleteUser, setDeleteUser] = React.useState({})
    const [deleteUserButtonDisabled, setDeleteUserButtonDisabled] = React.useState(false)
    const [deleteAlert, setDeleteAlert] = React.useState(false)
    const [deleteAlertMessage, setDeleteAlertMessage] = React.useState('')
    const [deleteAlertVariant, setDeleteAlertVariant] = React.useState('')
    const [deleteAlertIcon, setDeleteAlertIcon] = React.useState('')

    const {data: allUsersData, error: allUsersError, loading: allUsersLoading, errorMessage: allUsersErrorMessage} =
        useAxios(baseUrl + apiUrls.routes.admin.getAllUsersByOrgId.replace(':organizationId', jwtDecode(accessToken).organizationId), accessToken)

    useEffect(() => {
        if (!allUsersData) return;
    }, [allUsersData]);

    function handleUserDelete(user) {
        // Call the api to delete the user
        let url = baseUrl + apiUrls.routes.admin.deleteUser;

        // Replace the placeholder with the actual user id
        url = url.replace(':userId', user.id);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        // Say that the user is being deleted
        setDeleteAlert(true)
        setDeleteAlertMessage('Deleting user...')
        setDeleteAlertVariant('info')
        setDeleteAlertIcon('bi bi-hourglass-split')
        setDeleteUserButtonDisabled(true)

        // Wait one second before calling the api
        setTimeout(() => {
            axios.delete(url, { headers: headers })
                .then(response => {
                    setDeleteAlertMessage('User deleted successfully')
                    setDeleteAlertVariant('success')
                    setDeleteAlertIcon('bi bi-check-circle')

                    // Remove the user from the list
                    let newUsers = users.filter(u => u.id !== user.id)
                    setUsers(newUsers)
                })
                .catch(error => {
                    console.error(error)
                    setDeleteAlertMessage('Error deleting user')
                    setDeleteAlertVariant('danger')
                    setDeleteAlertIcon('bi bi-x-circle')
                    setDeleteUserButtonDisabled(false)
                })
        }, 1000)
    }

    return (
        <AdminTabContent
            icon="bi bi-people-fill"
            title="All Organization Users"
        >
            <div role="alert" className="alert">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="stroke-info shrink-0 w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                <span>
                    The user with <strong>bold</strong> is you.
                </span>
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
                    {allUsersData && allUsersData.map(
                        user => (
                            <TableItem
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
            </div>

            {/*<EditUserModal*/}
            {/*    show={showEditUserModal}*/}
            {/*    setShow={setShowEditUserModal}*/}
            {/*    handleClose={() => setShowEditUserModal(false)}*/}
            {/*    editUser={editUser}*/}
            {/*/>*/}

            {/*/!* Delete User Modal *!/*/}
            {/*<Modal*/}
            {/*    show={showDeleteUserModal}*/}
            {/*    onHide={() => setShowDeleteUserModal(false)}*/}
            {/*    backdrop="static"*/}
            {/*    keyboard={false}*/}
            {/*>*/}
            {/*    <Modal.Header closeButton>*/}
            {/*        <Modal.Title>*/}
            {/*            <i className="bi bi-trash-fill"> </i>*/}
            {/*            Delete User*/}
            {/*        </Modal.Title>*/}
            {/*    </Modal.Header>*/}
            {/*    <Modal.Body>*/}
            {/*        Are you sure you want to delete the user with the email <i>{deleteUser.email}</i>? <br />*/}
            {/*        <strong>This action cannot be undone.</strong>*/}

            {/*        {deleteAlert && (*/}
            {/*            <Alert variant={deleteAlertVariant}>*/}
            {/*                <i className={deleteAlertIcon}> </i>*/}
            {/*                {deleteAlertMessage}*/}
            {/*            </Alert>*/}
            {/*        )}*/}
            {/*    </Modal.Body>*/}
            {/*    <Modal.Footer>*/}
            {/*        <Button variant="secondary" onClick={() => setShowDeleteUserModal(false)}>*/}
            {/*            Cancel*/}
            {/*        </Button>*/}
            {/*        <Button variant="danger" onClick={() => handleUserDelete(deleteUser)}>*/}
            {/*            Delete*/}
            {/*        </Button>*/}
            {/*    </Modal.Footer>*/}
            {/*</Modal>*/}

            {/*<AllUsersTable*/}
            {/*    users={users}*/}
            {/*    myEmail={myEmail}*/}
            {/*    setEditUser={setEditUser}*/}
            {/*    setShowEditUserModal={setShowEditUserModal}*/}
            {/*    setDeleteUser={setDeleteUser}*/}
            {/*    setShowDeleteUserModal={setShowDeleteUserModal}*/}
            {/*    deleteBtnDisabled={deleteUserButtonDisabled}*/}
            {/*/>*/}

        </AdminTabContent>
    )
}

export default AdminAllUsersPage