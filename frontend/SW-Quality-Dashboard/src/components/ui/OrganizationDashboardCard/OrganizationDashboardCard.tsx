import React from "react";
import {anonymizeCreators} from "../../../assets/data/config.json";

function OrganizationDashboardCard({org, btnText, btnIcon, onClick, children}) {
    return (
        <>
            <div className="card card-side bg-base-100 org-card">
                <figure style={{height: '30vh'}}>
                    <img
                        className={anonymizeCreators ? "anonymized" : ""}
                        src={org.imgURL}
                        style={{height: '30vh', backgroundColor: 'white'}}
                        alt="Organization Logo"/>
                </figure>

                <div className="card-body">
                    <h2 className={anonymizeCreators ? "card-title anonymized" : "card-title"}>{org.name}</h2>

                    {children}

                    <div className="card-actions justify-end">
                        <button className="btn btn-primary"
                                onClick={onClick}>
                            {btnIcon}
                            {btnText}
                        </button>
                    </div>
                </div>

            </div>
        </>
    );
}

export default OrganizationDashboardCard;