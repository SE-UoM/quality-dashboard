import './CollabsibleNavbar.css'
import React, {useEffect, useState} from "react";
import DashboardNavbar from "../DashboardNavbar/DashboardNavbar.tsx";

function CollabsibleNavbar({isAuthenticated, isAdmin, currentSlide, setCurrentSlide, totalSlides}) {
    const [expanded, setExpanded] = useState(true);
    const [buttonIcon, setButtonIcon] = useState('bi bi-chevron-compact-up')
    const [autoCycle, setAutoCycle] = useState(false)

    // Auto-cycle slides every 10 seconds
    useEffect(() => {
        // If autoCycle is true, cycle through the slides every 10 seconds
        while (autoCycle) {
            const interval = setInterval(() => {
                nextBtnClick()
            }, 10000);

            return () => clearInterval(interval);
        }
    }, [autoCycle, currentSlide, setCurrentSlide]);


    const toggleNavbar = () => {
        setExpanded(!expanded);

        if (expanded) {
            setButtonIcon('bi bi-chevron-compact-down')
        }

        if (!expanded) {
            setButtonIcon('bi bi-chevron-compact-up')
        }
    };

    const nextBtnClick = () => {
        // If the current slide is the last slide, go back to the first slide
        if (currentSlide === totalSlides) {
            setCurrentSlide(1)
            return
        }

        setCurrentSlide(currentSlide + 1)
    }

    const previousBtnClick = () => {
        console.info("Clicked Left Button")

        // If the current slide is the first slide, go to the last slide
        if (currentSlide === 1) {
            setCurrentSlide(totalSlides)
            return
        }

        setCurrentSlide(currentSlide - 1)
    }

    const toggleAutoCycle = () => {
        console.info("Auto-cycling slides")
        setAutoCycle(!autoCycle)
    }

    // Add an event listener to the window to listen for the "keydown" event and cycle through the slides
    useEffect(() => {
        window.addEventListener("keydown", (event) => {
            if (event.key === "ArrowRight") {
                nextBtnClick()
            }

            if (event.key === "ArrowLeft") {
                previousBtnClick()
            }
        })
    }, [currentSlide, setCurrentSlide])

    return (
        <>
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
                                    <input type="checkbox" className="theme-controller" value="light" />

                                    {/* sun icon */}
                                    <i className="bi bi-brightness-high swap-off fill-current"></i>
                                    {/* moon icon */}
                                    <i className="bi bi-moon-stars swap-on fill-current"></i>
                                </label>
                            </li>

                            <li>
                                <a className="tooltip tooltip-bottom" data-tip="Next Screen" onClick={nextBtnClick}>
                                    <i className="bi bi-chevron-right"> </i>
                                </a>
                            </li>
                        </ul>
                    </div>
            )}
        </div>
        </>
    );
}

export default CollabsibleNavbar;