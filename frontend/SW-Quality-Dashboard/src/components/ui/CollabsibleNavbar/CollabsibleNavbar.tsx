import './CollabsibleNavbar.css'
import React, {useState} from "react";
import DashboardNavbar from "../DashboardNavbar/DashboardNavbar.tsx";

function CollabsibleNavbar(props) {
    const [expanded, setExpanded] = useState(true);
    const [buttonIcon, setButtonIcon] = useState('bi bi-chevron-compact-up')

    const toggleNavbar = () => {
        setExpanded(!expanded);

        if (expanded) {
            setButtonIcon('bi bi-chevron-compact-down')
        }

        if (!expanded) {
            setButtonIcon('bi bi-chevron-compact-up')
        }
    };

    return (
        <>

            {expanded && (
                <DashboardNavbar {...props} expanded={expanded} />
            )}
            <div className="navbar-close-button" onClick={toggleNavbar}>
                <button className="btn btn-outline-dark">
                    <i className={buttonIcon}></i>
                </button>
            </div>
        </>
    );
}

export default CollabsibleNavbar;