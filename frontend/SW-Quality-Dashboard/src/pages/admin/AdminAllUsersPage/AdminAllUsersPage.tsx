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
import {Heading} from "@chakra-ui/react";
import EditUserModal from "../../../components/modals/EditUserModal/EditUserModal.tsx";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

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

    useEffect(() => {
        // Call the api to get all users
        let url = baseUrl + apiUrls.routes.admin.getAllUsersByOrgId;

        // get the organization id from the access token
        let userOrganization = jwtDecode(accessToken).organizationId;
        let userEmail = jwtDecode(accessToken).sub;
        setMyEmail(userEmail);

        // Replace the placeholder with the actual organization id
        url = url.replace(':organizationId', userOrganization);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.get(url, { headers: headers })
            .then(response => {
                setUsers(response.data)
            })
            .catch(error => {
                console.error(error)
                setError(true)
            })
    }, [accessToken]);

    return (
        <AdminTabContent
            icon="bi bi-people-fill"
            title="All Organization Users"
        >
            {error && (
                <Alert variant="danger">
                    Error fetching users
                </Alert>
            )}

            <p>
                The user with <strong>bold</strong> is you.
            </p>

            <EditUserModal
                show={showEditUserModal}
                setShow={setShowEditUserModal}
                handleClose={() => setShowEditUserModal(false)}
                editUser={editUser}
            />

            {/* Delete User Modal */}
            <Modal
                show={showDeleteUserModal}
                onHide={() => setShowDeleteUserModal(false)}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>
                        <i className="bi bi-trash-fill"> </i>
                        Delete User
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to delete the user with the email <i>{deleteUser.email}</i>? <br />
                    <strong>This action cannot be undone.</strong>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteUserModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={() => setShowDeleteUserModal(false)}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>

            <AllUsersTable
                users={users}
                myEmail={myEmail}
                setEditUser={setEditUser}
                setShowEditUserModal={setShowEditUserModal}
                setDeleteUser={setDeleteUser}
                setShowDeleteUserModal={setShowDeleteUserModal}
            />

        </AdminTabContent>
    )
}

export default AdminAllUsersPage