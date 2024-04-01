import React, { useState } from 'react'
import "./UserHomePage.css"
import {Button} from "react-bootstrap";
import useLocalStorage from "../../hooks/useLocalStorage.ts";

function UserHomePage() {
    let [accessToken, setAccessToken] = useLocalStorage('accessToken', '');

    //

    return (
        <>
            <div className="user-home-page">
                <h1>User Home Page</h1>
            </div>
        </>
    )
}

export default UserHomePage