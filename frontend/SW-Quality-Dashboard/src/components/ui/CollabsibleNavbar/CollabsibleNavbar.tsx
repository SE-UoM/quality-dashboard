import './CollabsibleNavbar.css'
import React, {useState} from "react";
import DashboardNavbar from "../DashboardNavbar/DashboardNavbar.tsx";

function CollabsibleNavbar({isAuthenticated, isAdmin, currentSlide, setCurrentSlide, totalSlides}) {
    const [expanded, setExpanded] = useState(true);
    const [buttonIcon, setButtonIcon] = useState('bi bi-chevron-compact-up')

    console.log(currentSlide)

    const toggleNavbar = () => {
        setExpanded(!expanded);

        if (expanded) {
            setButtonIcon('bi bi-chevron-compact-down')
        }

        if (!expanded) {
            setButtonIcon('bi bi-chevron-compact-up')
        }
    };

    const rightBtnClick = () => {
        console.log("LEFT")

        // If the current slide is the last slide, go back to the first slide
        if (currentSlide === totalSlides) {
            setCurrentSlide(1)
            return
        }

        console.log(currentSlide)

        setCurrentSlide(currentSlide + 1)
    }

    const leftBtnClick = () => {
        console.log("Previous Clicked")

        // If the current slide is the first slide, go to the last slide
        if (currentSlide === 1) {
            setCurrentSlide(totalSlides)
            return
        }

        setCurrentSlide(currentSlide - 1)
    }

    return (
        <>

            {expanded && (
                <DashboardNavbar isAuthenticated={isAuthenticated} isAdmin={isAdmin} />
            )}
            <div className="navbar-close-button">
                <button className="btn" onClick={leftBtnClick}>
                    <i className="bi bi-arrow-left"></i>
                </button>

                <button className="btn" onClick={toggleNavbar}>
                    <i className={buttonIcon}></i>
                </button>

                <button className="btn" onClick={rightBtnClick}>
                    <i className="bi bi-arrow-right"></i>
                </button>
            </div>
        </>
    );
}

export default CollabsibleNavbar;