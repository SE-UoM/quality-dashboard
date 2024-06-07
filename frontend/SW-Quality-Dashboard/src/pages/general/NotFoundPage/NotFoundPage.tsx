import React, { useState } from 'react'
import "./NotFoundPage.css"
import logo from "../../../assets/dashboard_logo_transparent.png";

function NotFoundPage() {


    return (
        <>
            <div className="not-found-page">
                <i className="bi bi-exclamation-triangle text-error"></i>
                <h2 className="text-error">
                    <strong>404</strong>
                </h2>
                <p className="opacity-75">
                    Oops! The page you are looking for does not exist.
                </p>

                <a href="/" className="btn btn-primary">
                    Go back to the home page
                </a>
            </div>
        </>
    )
}

export default NotFoundPage