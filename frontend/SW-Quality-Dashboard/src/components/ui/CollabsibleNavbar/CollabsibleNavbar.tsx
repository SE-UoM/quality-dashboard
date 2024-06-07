import './CollabsibleNavbar.css'
import React, {useEffect, useState} from "react";
import DashboardNavbar from "../DashboardNavbar/DashboardNavbar.tsx";
import {CiSettings} from "react-icons/ci";
import { MdDisplaySettings } from 'react-icons/md';
import daisyThemes from '../../../assets/data/default_daisy_theme_names.json'

function CollabsibleNavbar({isAuthenticated, isAdmin, currentSlide, setCurrentSlide, totalSlides}) {
    const [expanded, setExpanded] = useState(true);
    const [buttonIcon, setButtonIcon] = useState('bi bi-chevron-compact-up')

    const [autoCycle, setAutoCycle] = useState(localStorage.getItem('dashboardScreensAutoCycle') || false)
    const [autoCycleInterval, setAutoCycleInterval] = useState(localStorage.getItem('dashboardScreensAutoCycleInterval') || 10)

    const [reloadPage, setReloadPage] = useState(localStorage.getItem('dashboardScreensReloadPage') || false)
    const [reloadIntervalMins, setReloadIntervalMins] = useState(localStorage.getItem('dashboardScreensReloadIntervalMins') || 10)

    const [themePrimary, setThemePrimary] = useState(localStorage.getItem('dashboardScreensThemePrimary') || 'dark')
    const [themeSecondary, setThemeSecondary] = useState(localStorage.getItem('dashboardScreensThemeSecondary') || 'light')

    // Set the theme of the dashboard
    useEffect(() => {
        document.documentElement.setAttribute('data-theme', themePrimary)
        localStorage.setItem('dashboardScreensThemePrimary', themePrimary)
    }, [themePrimary])

    useEffect(() => {
        localStorage.setItem('dashboardScreensThemeSecondary', themeSecondary)
    }, [themeSecondary])

    // Auto-cycle slides every 10 seconds
    useEffect(() => {
        if (autoCycle) {
            const interval = setInterval(() => {
                nextBtnClick()
            }, autoCycleInterval * 1000);

            return () => clearInterval(interval);
        }
    }, [autoCycle, currentSlide, setCurrentSlide, autoCycleInterval]);

    // Reload the page every `reloadIntervalMins` minutes
    useEffect(() => {
        if (reloadPage) {
            const interval = setInterval(() => {
                window.location.reload()
            }, reloadIntervalMins * 60 * 1000);

            return () => clearInterval(interval);
        }
    }, [reloadPage, reloadIntervalMins]);

    const toggleNavbar = () => {
        setExpanded(!expanded);

        if (expanded) {
            setButtonIcon('bi bi-chevron-compact-down')
        } else {
            setButtonIcon('bi bi-chevron-compact-up')
        }
    };

    const nextBtnClick = () => {
        if (currentSlide === totalSlides) {
            setCurrentSlide(1)
            return
        }

        setCurrentSlide(currentSlide + 1)
    }

    const previousBtnClick = () => {
        console.info("Clicked Left Button")

        if (currentSlide === 1) {
            setCurrentSlide(totalSlides)
            return
        }

        setCurrentSlide(currentSlide - 1)
    }

    const toggleAutoCycle = () => {
        console.info("Auto-cycling slides")
        let changedValue = !autoCycle
        setAutoCycle(changedValue)
        localStorage.setItem('dashboardScreensAutoCycle', changedValue)
        localStorage.setItem('dashboardScreensAutoCycleInterval', autoCycleInterval)
    }

    useEffect(() => {
        const handleKeyDown = (event) => {
            if (event.key === "ArrowRight") {
                nextBtnClick()
            }

            if (event.key === "ArrowLeft") {
                previousBtnClick()
            }
        }

        window.addEventListener("keydown", handleKeyDown)
        return () => window.removeEventListener("keydown", handleKeyDown)
    }, [currentSlide, setCurrentSlide])

    return (
        <>
            {/* SETTINGS MODAL */}
            {/* Open the modal using document.getElementById('ID').showModal() method */}
            <dialog id="dashboard_settings_modal" className="modal modal-bottom sm:modal-middle">
                <div className="modal-box">
                    <form method="dialog">
                        <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
                            <i className="bi bi-x" style={{fontSize: "4vh"}}></i>
                        </button>
                    </form>

                    <h3 className="font-bold text-lg"
                        style={{
                            display: "flex",
                            alignItems: "center",
                            gap: "0.5vw"
                        }}
                    >
                        <MdDisplaySettings />
                        Dashboard Settings
                    </h3>
                    <p className="py-4">
                        Here you can change the display settings of the dashboard.
                    </p>

                    <div className="divider">
                        <i className="bi bi-arrow-repeat"></i>
                        Auto-cycle Slides
                    </div>

                    {/* Autocycle Toggler */}
                    <div className="form-control">
                        <label className="cursor-pointer label">
                            <span className="label-text">Auto-cycle On/Off</span>
                            <div className="relative flex items-center">
                                <input type="checkbox" className="toggle toggle-primary" checked={autoCycle} onChange={toggleAutoCycle} />
                            </div>
                        </label>
                    </div>

                    {/* Autocycle  Interval */}
                    <div className="form-control">
                        <label className="label">
                            <span className="label-text">Auto-cycle Interval (seconds)</span>
                            <input min={1} type="number" className="input input-bordered w-50" placeholder="10" value={autoCycleInterval} onChange={(e) => {
                                setAutoCycleInterval(parseInt(e.target.value))
                                localStorage.setItem('dashboardScreensAutoCycleInterval', parseInt(e.target.value))
                            }} />
                        </label>
                    </div>

                    <div className="divider">
                        <i className="bi bi-arrow-clockwise"></i>
                        Reload Dashboard
                    </div>

                    {/* Reload Toggler */}
                    <div className="form-control">
                        <label className="cursor-pointer label">
                            <span className="label-text">Reload On/Off</span>
                            <div className="relative flex items-center">
                                <input type="checkbox" className="toggle toggle-primary" checked={reloadPage} onChange={()=> {
                                    let changedValue = !reloadPage
                                    setReloadPage(changedValue)
                                    localStorage.setItem('dashboardScreensReloadPage', changedValue)
                                }} />
                            </div>
                        </label>
                    </div>

                    {/* Reload Interval */}
                    <div className="form-control">
                        <label className="label">
                            <span className="label-text">Reload Interval (minutes)</span>
                            <input type="number" min={1} className="input input-bordered w-50" placeholder="10" value={reloadIntervalMins} onChange={(e) => {
                                setReloadIntervalMins(parseInt(e.target.value))
                                localStorage.setItem('dashboardScreensReloadIntervalMins', parseInt(e.target.value))
                            }} />
                        </label>
                    </div>

                    <div className="divider">
                        <i className="bi bi-palette"></i>
                        Theme Settings
                    </div>

                    {/* Theme Selector (Primary) */}
                    <div className="form-control">
                        <label className="label">
                            <span className="label-text">Primary Theme</span>
                            <select className="select select-bordered w-50" value={themePrimary} onChange={(e) => setThemePrimary(e.target.value)}>
                                {daisyThemes.filter((theme) => theme !== themeSecondary).map((theme) => (
                                    <option key={theme} value={theme}>{theme}</option>
                                ))}
                            </select>
                        </label>
                    </div>

                    {/* Theme Selector (Secondary) */}
                    <div className="form-control">
                        <label className="label">
                            <span className="label-text">Secondary Theme</span>
                            <select className="select select-bordered w-50" value={themeSecondary} onChange={(e) => setThemeSecondary(e.target.value)}>
                                {   // Do not include the primary theme in the secondary theme options
                                    daisyThemes.filter((theme) => theme !== themePrimary).map((theme) => (
                                    <option key={theme} value={theme}>{theme}</option>
                                ))}
                            </select>
                        </label>
                    </div>
                </div>
            </dialog>

            <div
                className="navbar bg-base-100"
                style={{
                    display: "flex",
                    flexDirection: "column",
                }}
            >
                {expanded && (
                    <div className="menu menu-horizontal bg-base-200 rounded-box"
                         style={{
                             display: "flex",
                             justifyContent: "center",
                             alignItems: "center",
                             padding: "1em",
                             gap: "1em",
                             width: "100%"
                         }}
                    >
                        <ul
                            style={{
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center",
                                gap: "1em",
                                listStyle: "none",
                                padding: "0"
                            }}
                        >
                            <li>
                                <a onClick={previousBtnClick} className="tooltip tooltip-bottom" data-tip="Previous Screen">
                                    <i className="bi bi-chevron-left"> </i>
                                </a>
                            </li>
                            <li>
                                <a href="/" className="tooltip tooltip-bottom" data-tip="Back to Home Page">
                                    <i className="bi bi-house"></i>
                                </a>
                            </li>
                            {isAdmin && (
                                <li>
                                    <a href="/admin" className="tooltip tooltip-bottom" data-tip="Admin Panel">
                                        <i className="bi bi-person-gear"></i>
                                    </a>
                                </li>
                            )}
                            <li>
                                <a href="/submit-project" className="tooltip tooltip-bottom" data-tip="Submit a Project">
                                    <i className="bi bi-plus-circle"></i>
                                </a>
                            </li>

                            <li>
                                <a className="tooltip tooltip-bottom" data-tip={"Auto-cycle Slides (" + (autoCycle ? "On" : "Off") + ")"} onClick={toggleAutoCycle}>
                                    <i className="bi bi-arrow-repeat"></i>
                                </a>
                            </li>

                            <li className="tooltip tooltip-bottom"  data-tip="Change Theme">
                                <label className="swap swap-rotate">

                                    {/* this hidden checkbox controls the state */}
                                    <input type="checkbox" className="theme-controller" value={themeSecondary} />

                                    {/* sun icon */}
                                    <i className="bi bi-brightness-high swap-off fill-current"></i>
                                    {/* moon icon */}
                                    <i className="bi bi-moon-stars swap-on fill-current"></i>
                                </label>
                            </li>

                            <li>
                                <a className="tooltip tooltip-bottom" data-tip="Dashboard Settings" onClick={()=>window.document.getElementById('dashboard_settings_modal').showModal()}>
                                    <i className="bi bi-gear"></i>
                                </a>
                            </li>

                            <li>
                                <a onClick={nextBtnClick} className="tooltip tooltip-bottom" data-tip="Next Screen">
                                    <i className="bi bi-chevron-right"> </i>
                                </a>
                            </li>
                        </ul>
                    </div>
                )
                }

                <div
                    className="btn btn-circle"
                    onClick={toggleNavbar}
                    style={{
                        position: "absolute",
                        bottom: "-1.5em",
                        alignSelf: "center"
                    }}
                >
                    <i className={buttonIcon}></i>
                </div>
            </div>
        </>
    );
}

export default CollabsibleNavbar;
