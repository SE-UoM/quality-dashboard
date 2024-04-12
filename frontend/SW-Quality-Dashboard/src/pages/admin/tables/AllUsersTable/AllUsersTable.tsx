import './AllUsersTable.css'
import {Button, Table} from "react-bootstrap";
import React from "react";

const roleColors = {
    'SIMPLE': '#4b9845',
    'PRIVILEGED': '#e53700'
}

const verifiedColors = {
    'YES': '#4b9845',
    'NO': '#e53700'
}

function AllUsersTable({users, myEmail, setEditUser, setShowEditUserModal, setShowDeleteUserModal, setDeleteUser, deleteBtnDisabled}) {
  return (
      <Table striped bordered hover>
          <thead>
          <tr>
              <th>Index</th>
              <th>UID</th>
              <th>Username</th>
              <th>Email</th>
              <th>Roles</th>
              <th>Verified</th>
              <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          {users.map(user => (
              <tr key={user.id}>
                  <td>{user.email === myEmail ? <b>{users.indexOf(user) + 1}</b> : users.indexOf(user) + 1}</td>
                  <td>{user.email === myEmail ? <b>{user.id}</b> : user.id}</td>
                  <td>{user.email === myEmail ? <b>{user.name}</b> : user.name}</td>
                  <td>
                      {user.email === myEmail ? <b><a href={"mailto:" + user.email}>{user.email}</a></b> : <a href={"mailto:" + user.email}>user.email</a>}
                  </td>
                  <td className="admin-users-roles">
                      {// Roles are separated by commas, so we split them and display them as badges
                          user.roles.split(',').map(role => (
                              <div
                                  className={"dashboard-pill"}
                                  style={{
                                      backgroundColor: roleColors[role]
                                  }}
                              >
                                  {role}
                              </div>
                          ))
                      }
                  </td>
                  <td>
                      {
                          <div
                              className="dashboard-pill"
                              style={{
                                  backgroundColor: verifiedColors[user.verified ? 'YES' : 'NO']
                              }}
                          >
                              {user.verified ? 'Yes' : 'No'}
                          </div>
                      }
                  </td>
                  <td className="admin-users-actions">
                      <Button variant="danger" onClick={() => {
                            setDeleteUser(user)
                            setShowDeleteUserModal(true)
                      }} disabled={deleteBtnDisabled}>
                          <i className="bi bi-trash-fill"> </i>
                      </Button>

                      <Button variant="secondary" onClick={() => {
                          setEditUser(user)
                          setShowEditUserModal(true)
                      }}>
                          <i className="bi bi-pencil-fill"> </i>
                      </Button>
                  </td>
              </tr>
          ))}
          </tbody>
      </Table>
  );
}

export default AllUsersTable;