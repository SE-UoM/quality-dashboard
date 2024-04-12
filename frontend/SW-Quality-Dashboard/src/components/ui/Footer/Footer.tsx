import React, { useState } from 'react'
import "./Footer.css"
import {Button} from "react-bootstrap";
import logo from '../../../assets/svg/dashboard_logo_black.svg'

function Footer() {


    return (
        <>
            <footer className="dashboard-footer">
                <section className={"footer-top"}>
                    <div className="footer-top-brand">
                        <img className="footer-logo" src={logo} alt="UoM Quality Dashboard Logo"/>
                        <p>
                            UoM Quality Dashboard <br/>
                            By University of Macedonia, OpenSource Uom and SE Lab Uom
                        </p>
                    </div>

                    <div className="footer-top-content">
                        <div className="footer-top-content-item">
                            <h3>Quick Links</h3>
                            <ul>
                                <li><a href="https://www.uom.gr/dai">Department of Applied Informatics</a></li>
                                <li><a href="https://sde.uom.gr/">Software and Data Engineering Lab</a></li>
                                <li><a href="https://opensource.uom.gr/">OpenSource UoM Team</a></li>
                            </ul>
                        </div>
                        <div className="footer-top-content-item">
                            <h3>Resources</h3>
                            <ul>
                                <li><a href="https://github.com/SE-UoM/quality-dashboard">Github Repository</a></li>
                                <li><a href="https://github.com/SE-UoM/quality-dashboard/discussions/categories/bug-report">Report a Bug</a></li>
                                <li><a href="">Contact</a></li>
                            </ul>
                        </div>

                        <div className="footer-top-content-item">
                            <h3>More Links</h3>
                            <ul>
                                <li><a href="https://my.uom.gr/">myUoM</a></li>
                                <li><a href="https://opensource.uom.gr/index.php/blog/">OpenSource UoM Blog</a></li>
                                <li><a href="https://sde.uom.gr/index.php/research-groups/software-engineering-group/">SDE UoM Software Engineering Group</a></li>
                            </ul>
                        </div>


                    </div>

                </section>

                <section className={"footer-bottom"}>
                    <p>{new Date().getFullYear()} UoM Quality Dashboard</p>
                </section>
            </footer>
        </>
    )
}

export default Footer