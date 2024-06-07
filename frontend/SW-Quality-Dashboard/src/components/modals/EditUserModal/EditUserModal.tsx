import './EditUserModal.css'
import {Alert, Button, Form, Modal} from "react-bootstrap";
import React, {useEffect} from "react";
import apiUrls from "../../../assets/data/api_urls.json";
import axios from "axios";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function EditUserModal({ show, setShow, handleClose, editUser }) {
    const [accessToken] = useLocalStorage('accessToken', '')
    const [editAlert, setEditAlert] = React.useState(false)
    const [editAlertVariant, setEditAlertVariant] = React.useState('success')
    const [editAlertMessage, setEditAlertMessage] = React.useState('')

    const [editedName, setEditedName] = React.useState(editUser.name)
    const [editedEmail, setEditedEmail] = React.useState(editUser.email)
    const [userRoles, setUserRoles] = React.useState([])
    const [selectedNewRole, setSelectedNewRole] = React.useState('')

    const handleEditSubmit = () => {
        // Check if there are any changes
        let noChanges = editedName === editUser.name && editedEmail === editUser.email;
        let emptyInput = editedName === '' && editedEmail === '';
        if (noChanges || emptyInput) {
            setEditAlertVariant('warning')
            setEditAlertMessage('No changes were made')
            setEditAlert(true)
            return
        }

        // Make a map only with the provided values
        let editedUser = {}
        if (editedName !== '') {
            editedUser['name'] = editedName
        }
        if (editedEmail !== '') {
            editedUser['email'] = editedEmail
        }

        // Call the api to update the user
        let url = baseUrl + apiUrls.routes.admin.updateUser;
        url = url.replace(':userId', editUser.id);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        axios.put(url, editedUser, { headers: headers })
            .then(response => {
                setEditAlertVariant('success')
                setEditAlertMessage('User updated successfully')
                setEditAlert(true)

                // Wait 5 seconds before closing the modal
                setTimeout(() => {
                    setShow(false)
                }, 5000)
            })
            .catch(error => {
                console.error(error)
                setEditAlertVariant('danger')
                setEditAlertMessage('Error updating user')
                setEditAlert(true)
            })
    }

    return (
        <Modal
            show={show}
            onHide={handleClose}
            backdrop="static"
            keyboard={false}
        >
            <Modal.Header closeButton>
                <Modal.Title>
                    <i className="bi bi-person-fill-gear"> </i>
                    Edit User
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {editAlert && (
                    <Alert variant={editAlertVariant}>
                        <Alert.Heading>
                            <i className={editAlertVariant === 'danger' || editAlertVariant === 'warning' ? 'bi bi-exclamation-triangle-fill' : 'bi bi-check-circle-fill'}> </i>
                            {editAlertMessage}
                        </Alert.Heading>
                    </Alert>
                )}
                <Form>
                    <h5>
                        <i className="bi bi-person-fill"> </i>
                        Profile Details
                    </h5>
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>
                            <i className="bi bi-person-fill"> </i>
                            Username
                        </Form.Label>
                        <Form.Control type='text' placeholder={editUser.name} onChange={(e) => setEditedName(e.target.value)} />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicPassword">
                        <Form.Label>
                            <i className="bi bi-envelope-fill"> </i>
                            Email
                        </Form.Label>
                        <Form.Control type="email" placeholder={editUser.email} onChange={(e) => setEditedEmail(e.target.value)} />
                    </Form.Group>
                    <hr />
                    <h5>
                        <i className="bi bi-shield-lock-fill"> </i>
                        User Roles
                    </h5>
                    <Form.Group className="mb-3" controlId="formBasicPassword">

                        <div className="user-roles-edit">

                        {editUser.roles && editUser.roles.split(',').map((role, index) => {
                                return (
                                    <Form.Control type="text" placeholder={role}  readOnly />
                                )
                            })
                        }
                        </div>

                    </Form.Group>
                </Form>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleEditSubmit}>
                    Save
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default EditUserModal