import React, { useState } from 'react'
import "./HomePage.css"
import {Button} from "react-bootstrap";

function HomePage() {

    return (
        <>
            <div className="home-page">
                <h1>Welcome to UoM Dashboard</h1>
                <p>Here you can find all the information you need about the software quality of University of Macedonia</p>

                <section className={"homepage-action-buttons"}>
                    <Button href="/login" variant="outline-light">Login</Button>
                    <Button href="/register" variant="outline-light">Register</Button>
                    <Button href="/dashboard" variant="outline-light">Dashboard</Button>
                </section>
            </div>
        </>
    )
}

export default HomePage