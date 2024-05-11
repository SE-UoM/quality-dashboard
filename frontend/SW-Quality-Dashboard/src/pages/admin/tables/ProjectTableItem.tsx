import {truncateString} from "../../../utils/textUtils.ts";
import React from "react";

export default function ProjectTableItem({project}) {
    return (
        <>
            <tr>
                <td>
                    <div className="flex items-center gap-3">
                        <div className="avatar">
                            <div className="mask mask-squircle w-12 h-12">
                                <img src={`https://ui-avatars.com/api/?name=${project.name}&background=random`} alt="Avatar Tailwind CSS Component" />
                            </div>
                        </div>
                        <div>
                            <div className="font-bold">{project.name}</div>
                            <div className="text-sm opacity-50">{project.organizationName}</div>
                        </div>
                    </div>
                </td>
                <td>
                    <a className="link link-info" href={project.repoUrl}>
                        {truncateString(project.repoUrl, 50)}
                    </a>
                </td>
                <td>
                            <span className={project.status === 'ANALYSIS_TO_BE_REVIEWED' ? 'badge  badge-sm badge-error' : 'badge  badge-sm badge-info'}>
                                <i className={project.status === 'ANALYSIS_TO_BE_REVIEWED' ? 'bi bi-exclamation-circle' : 'bi bi-check2-circle'}> </i> &nbsp;
                                {project.status === 'ANALYSIS_TO_BE_REVIEWED' ? 'TO BE REVIEWED' : 'REVIEWED'}
                            </span>

                </td>
                <td>
                    {project.status === 'ANALYSIS_TO_BE_REVIEWED' ? (
                        <span className="badge badge-info badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-clock">&nbsp;</i>
                            REVIEW
                        </span>
                    ) : project.status === 'ANALYSIS_COMPLETED' ? (
                        <span className="badge badge-success badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-check2-circle">&nbsp;</i>
                            COMPLETED
                        </span>
                    ) : project.status === 'ANALYSIS_FAILED' ? (
                        <span className="badge badge-error badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-x-circle">&nbsp;</i>
                            FAILED
                        </span>
                    ) : project.status === 'ANALYSIS_SKIPPED' ? (
                        <span className="badge badge-warning badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-x-circle">&nbsp;</i>
                            SKIPPED
                        </span>
                    ) : project.status === 'ANALYSIS_NOT_STARTED' ? (
                        <span className="badge badge-neutral badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-x-circle">&nbsp;</i>
                            NOT STARTED
                        </span>
                    ) : project.status === 'ANALYSIS_STARTED' ? (
                        <span className="badge badge-accent badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-clock">&nbsp;</i>
                            STARTED
                        </span>
                    ) : project.status === 'ANALYSIS_READY' ? (
                        <span className="badge badge-success badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-check2-circle">&nbsp;</i>
                            READY
                        </span>
                    ) : (
                        <span className="badge badge-warning badge-outline badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-x-circle">&nbsp;</i>
                            UNKNOWN
                        </span>
                    )}
                </td>

                <td>
                    <div className="flex items-center gap-3">
                        <button className="btn btn-sm btn-outline btn-success">
                            <i className="bi bi-check2-circle"> </i>
                            Approve
                        </button>

                        <button className="btn btn-sm btn-outline btn-error">
                            <i className="bi bi-x-circle"> </i>
                            Reject
                        </button>
                    </div>
                </td>
            </tr>
        </>
    )
}