import React, { useState } from 'react'
import "./NotFoundPage.css"
import {Button} from "react-bootstrap";
import logo from "../../assets/dashboard_logo_transparent.png";

function NotFoundPage() {


    return (
        <>
            <div className="not-found-page">
                <img src={logo} alt="Quality Dashboard Logo" className="not-found-logo" height="40%"/>
                <h2>
                    <strong>404</strong>
                </h2>
                <h3>Page not found</h3>
                <p>
                    Sorry, the page you are looking for does not exist or has been moved.
                </p>
            </div>
        </>
    )
}

export default NotFoundPage