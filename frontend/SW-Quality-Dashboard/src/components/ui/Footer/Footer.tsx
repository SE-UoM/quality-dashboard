import React, { useState } from 'react'
import "./Footer.css"
import {Button} from "react-bootstrap";

function Footer() {


    return (
        <>
            <footer className="dashboard-footer">
                <section className={"footer-top"}>

                </section>

                <section className={"footer-bottom"}>
                    <p>© {new Date().getFullYear()} UoM Quality Dashboard</p>
                    <p>
                        Made by <a href="https://opensource.uom.gr/uom" className="attribution-url"> OpenSourceUom</a>, <a href={"https://www.uom.gr/"} className="attribution-url">SE Lab Uom</a> and <a href={"https://www.uom.gr/"} className="attribution-url"> University of Macedonia</a>
                    </p>
                </section>
            </footer>
        </>
    )
}

export default Footer