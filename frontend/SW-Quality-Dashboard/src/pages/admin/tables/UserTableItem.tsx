import React, {useEffect} from "react";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import {jwtDecode} from "jwt-decode";

export default function UserTableItem({userImg, userName, userOrg, userEmail, userRoles, userVerified, key}) {
    const [accessToken, setAccessToken] = useLocalStorage('accessToken', '')
    const [authenticatedUserEmail, setAuthenticatedUserEmail] = React.useState('')

    useEffect(() => {
        // Get the user email from the access token
        let user = jwtDecode(accessToken)
        if (!user) return;

        setAuthenticatedUserEmail(user.sub)
    }, [accessToken]);


    function handleDeleteUser(userEmail) {
        console.log(`Deleting user with email: ${userEmail}`)
    }

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
                        <button className="btn btn-sm btn-error" disabled={(authenticatedUserEmail === userEmail)}>
                            <i className="bi bi-trash-fill"> </i>
                            Delete
                        </button>
                    </div>
                </td>
            </tr>
        </>
    )
}