import React, {useEffect} from "react";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import {jwtDecode} from "jwt-decode";
import apiUrls from "../../../assets/data/api_urls.json";
import axios from "axios";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function DeleteUserModal({icon, header, children, id}) {
    return (
        <dialog id={id} className="modal">
            <div className="modal-box">
                <h3 className="font-bold text-lg">
                    <i className={icon}> </i>
                    {header}
                </h3>
                {children}
            </div>
        </dialog>
    )
}

export default function UserTableItem({userImg, userName, userOrg, userEmail, userRoles, userVerified, userID}) {
    const [accessToken, setAccessToken] = useLocalStorage('accessToken', '')
    const [authenticatedUserEmail, setAuthenticatedUserEmail] = React.useState('')

    const [deleteLoading, setDeleteLoading] = React.useState(false)
    const [deleteError, setDeleteError] = React.useState(false)
    const [deleteErrorMsg, setDeleteErrorMsg] = React.useState('')
    const [deleteSuccess, setDeleteSuccess] = React.useState(false)

    useEffect(() => {
        // Get the user email from the access token
        let user = jwtDecode(accessToken)
        if (!user) return;

        setAuthenticatedUserEmail(user.sub)
    }, [accessToken]);


    async function callDeleteUserAPI() {
        let url = baseUrl + apiUrls.routes.admin.deleteUser;
        url = url.replace(':userId', userID);

        let headers = {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        }

        try {
            setDeleteLoading(true)

            await axios.delete(url, { headers: headers })
                .then(response => {
                    // Wait for half a second to show the success message
                    setTimeout(() => {
                        setDeleteSuccess(true)
                        setDeleteLoading(false)
                    }, 500)
                })
        } catch (error) {
            setDeleteError(true)
            setDeleteErrorMsg(error.response.data.message)
            setDeleteLoading(false)
        }
    }

    function handleDeleteUser() {
        console.log('Deleting user with email: ', userEmail)

        document.getElementById(`deleteUserModal-${userID}`).showModal()
    }

    return (
        <>
            <DeleteUserModal
                icon="bi bi-trash-fill"
                header="Delete User"
                id={`deleteUserModal-${userID}`}
            >
                <p className="pb-3">
                    Are you sure you want to delete the user with email <strong>{userEmail}</strong>?
                </p>

                {deleteError && (
                    <div role="alert" className="alert alert-error mb-3">
                        <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                        <span>{deleteErrorMsg}</span>
                    </div>
                )}

                {deleteSuccess && (
                    <div role="alert" className="alert alert-success mb-3">
                        <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                        <span>
                            User with email <strong>{userEmail}</strong> has been deleted! <br/>
                            Refresh the page to see the changes.
                        </span>
                    </div>

                )}
                <div className="flex justify-end gap-3">
                    <button className="btn btn-sm btn-neutral" onClick={() => document.getElementById(`deleteUserModal-${userID}`).close()}>
                        <i className="bi bi-x-circle"> </i>
                        Cancel
                    </button>
                    <button className="btn btn-sm btn-error" onClick={() => callDeleteUserAPI()}
                            disabled={deleteLoading || deleteSuccess}
                    >
                        {deleteLoading ? (
                            <span className="loading loading-sm"></span>
                        ) : (
                            <i className="bi bi-trash-fill"> </i>
                        )}
                        Delete
                    </button>
                </div>
            </DeleteUserModal>

            <tr key={userID} className={authenticatedUserEmail === userEmail ? "bg-base-200" : ""}>
                <td>
                    <div className="flex items-center gap-3">
                        <div className="avatar">
                            <div className="mask mask-squircle w-12 h-12">
                                <img src={userImg} alt="Avatar Tailwind CSS Component" />
                            </div>
                        </div>
                        <div>
                            <div className="font-bold">
                                {userName}
                                {authenticatedUserEmail === userEmail && (
                                    <span className="badge badge-accent badge-sm ml-2">You</span>
                                )}
                            </div>
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
                        <button className="btn btn-sm btn-primary" disabled>
                            <i className="bi bi-pencil-fill"> </i>
                            Edit
                        </button>
                        <button className="btn btn-sm btn-error" disabled={(authenticatedUserEmail === userEmail)}
                                onClick={() => handleDeleteUser()}
                        >
                            <i className="bi bi-trash-fill"> </i>
                            Delete
                        </button>
                    </div>
                </td>
            </tr>
        </>
    )
}