import React, { useState } from 'react'
import "./Footer.css"
import brandData from '../../../../content/settings.json'
import logo from '../../../assets/svg/dashboard_logo_black.svg'
import footerData from '../../../../content/footer.json'


function Footer() {


    return (
        <>
            <footer className="footer p-10 bg-base-200 text-base-content">
                <nav>
                    <h6 className="footer-title">Resources</h6>
                    {footerData.resources.map((resource, index) => {
                        return (
                            <a
                                key={index}
                                href={resource.url}
                                className="link link-hover"
                            >
                                {resource.text}
                            </a>
                        )
                    })}
                </nav>
                <nav>
                    <h6 className="footer-title">University of Macedonia</h6>
                    {footerData.uom.map((uom, index) => {
                        return (
                            <a
                                key={index}
                                href={uom.url}
                                className="link link-hover"
                            >
                                {uom.text}
                            </a>
                        )
                    })}
                </nav>

                <nav>
                    <h6 className="footer-title">OpenSource UoM</h6>
                    {footerData.osuom.map((openSourceUoM, index) => {
                        return (
                            <a
                                key={index}
                                href={openSourceUoM.url}
                                className="link link-hover"
                            >
                                {openSourceUoM.text}
                            </a>
                        )
                    })}
                </nav>
            </footer>
            <footer className="footer px-10 py-4 border-t bg-base-200 text-base-content border-base-300">
                <aside className="items-center grid-flow-col">
                    <img className="footer-logo" src={logo} alt="UoM Quality Dashboard Logo"/>
                    <p>
                        &copy; {new Date().getFullYear() + " " + brandData.brand} <br/>
                        by &nbsp;
                        <a href={brandData.orgs.uom.url} className="link">{brandData.orgs.uom.name}</a>, &nbsp;
                        <a href={brandData.orgs.openSourceUoM.url} className="link">{brandData.orgs.openSourceUoM.name}</a>, and &nbsp;
                        <a href={brandData.orgs.sde.url} className="link">{brandData.orgs.sde.name}</a>.
                    </p>
                </aside>
                <nav className="md:place-self-center md:justify-self-end">
                    <div className="grid grid-flow-col gap-4">
                        <a
                            href="https://github.com/SE-UoM/quality-dashboard"
                            className="btn btn-ghost"
                            style={{fontSize: "5vh"}}
                        >
                            <i className={"bi bi-github"}> </i>
                        </a>
                    </div>
                </nav>
            </footer>
        </>
    )
}

export default Footer