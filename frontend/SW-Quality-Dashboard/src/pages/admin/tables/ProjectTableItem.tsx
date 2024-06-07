import {truncateString} from "../../../utils/textUtils.ts";
import React from "react";
import apiUrls from "../../../assets/data/api_urls.json";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import axios from "axios";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

function ProjectActionModal({icon, header, children, id}) {
    return (
        <dialog id={id} className="modal">
            <div className="modal-box">
                <h3 className="font-bold text-lg">
                    <i className={icon}> </i>
                    {header}
                </h3>
                {children}
            </div>
        </dialog>
    )
}

export default function ProjectTableItem({project}) {
    const [accessToken] = useLocalStorage('accessToken', '')

    const [approveLoading, setApproveLoading] = React.useState(false)
    const [approveError, setApproveError] = React.useState(false)
    const [approveErrorMsg, setApproveErrorMsg] = React.useState('')
    const [approveSuccess, setApproveSuccess] = React.useState(false)

    const [startAnalysisLoading, setStartAnalysisLoading] = React.useState(false)
    const [startAnalysisError, setStartAnalysisError] = React.useState(false)
    const [startAnalysisErrorMsg, setStartAnalysisErrorMsg] = React.useState('')
    const [startAnalysisSuccess, setStartAnalysisSuccess] = React.useState(false)

    const [rejectLoading, setRejectLoading] = React.useState(false)
    const [rejectError, setRejectError] = React.useState(false)
    const [rejectErrorMsg, setRejectErrorMsg] = React.useState('')
    const [rejectSuccess, setRejectSuccess] = React.useState(false)

    const [modalText, setModalText] = React.useState('')
    const [modalSubtext, setModalSubtext] = React.useState('')

    async function callApproveProjectAPI(projectId) {
        let url = baseUrl + apiUrls.routes.admin.authorizeProject.replace(':projectId', projectId)

        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        try {
            setApproveLoading(true)
            await axios.put(url, {}, {headers: headers})
                .then(response => {
                    // Wait for half a second to show the success message
                    setTimeout(() => {
                        setApproveSuccess(true)
                        setApproveLoading(false)
                    }, 500)
                })
        } catch (error) {
            setApproveError(true)
            setApproveErrorMsg(error.response.data.message)
            setApproveLoading(false)
        }
    }

    async function callStartAnalysisAPI(projectURL) {
        let url = baseUrl + apiUrls.routes.startAnalysis.replace('${githubUrl}', projectURL)

        let headers = {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }

        try {
            setStartAnalysisLoading(true)
            await axios.post(url, {}, {headers: headers})
                .then(response => {
                    // Wait for half a second to show the success message
                    setTimeout(() => {
                        setStartAnalysisSuccess(true)
                        setStartAnalysisLoading(false)
                    }, 500)
                })
        } catch (error) {
            setStartAnalysisError(true)
            setStartAnalysisErrorMsg(error.response.data.message)
            setStartAnalysisLoading(false)
        }
    }

    function handleApprove() {
        console.log('Approve')
        document.getElementById(`approveProject${project.id}`).showModal()
    }

    function handleReject() {
        console.log('Reject')
        document.getElementById(`rejectProject${project.id}`).showModal()
    }

    return (
        <>
            <ProjectActionModal
                icon="bi bi-exclamation-circle"
                header="Approve Project"
                id={`approveProject${project.id}`}
            >
                <p>
                    <br/>
                    Are you sure you want to approve this project for Analysis? <br/> <br/>
                    <strong>This action cannot be undone.</strong>
                </p>

                {approveError && (
                    <div role="alert" className="alert alert-error mt-3">
                        <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                        <span>{approveErrorMsg}</span>
                    </div>
                )}

                {approveSuccess && (
                    <>
                        <div role="alert" className="alert alert-success mt-3 flex-column">
                            <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                            <span>
                                Project approved!
                            </span>
                        </div>
                        <div role="alert" className="alert mt-3 flex-column">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="stroke-info shrink-0 w-6 h-6"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                            <span>
                            Do you want to analyze the project now?
                        </span>
                            <div style={{display: "flex", flexDirection: "row", gap: "1vh"}}>
                                <button className="btn btn-sm btn-primary" onClick={() => callStartAnalysisAPI(project.repoUrl)}
                                        disabled={startAnalysisLoading || startAnalysisSuccess}
                                >
                                    {startAnalysisLoading ? (
                                        <span className="loading loading-sm"></span>
                                    ) : (
                                        <i className="bi bi-play-circle"> </i>
                                    )}
                                    {startAnalysisLoading ? 'Started...' : 'Yes'}
                                </button>
                            </div>
                        </div>
                    </>
                )}

                <div className="modal-action">
                    <button className="btn" onClick={() => document.getElementById(`approveProject${project.id}`).close()}>
                        <i className="bi bi-x-circle"> </i>
                        No
                    </button>
                    <button className="btn btn-success" onClick={() => callApproveProjectAPI(project.id)}
                        disabled={approveLoading || approveSuccess}
                    >
                        {approveLoading ? (
                            <span className="loading loading-sm"></span>
                        ) : (
                            <i className="bi bi-check2-circle"> </i>
                        )}
                        Yes
                    </button>
                </div>
            </ProjectActionModal>

            <ProjectActionModal
                icon="bi bi-x-circle"
                header="Reject Project"
                id={`rejectProject${project.id}`}
            >
                <p>Are you sure you want to reject this project?</p>
                <div className="modal-action">
                    <button className="btn btn-success">
                        <i className={"bi bi-check2-circle"}> </i>
                        Yes
                    </button>

                    <button className="btn btn-error" onClick={() => document.getElementById(`rejectProject${project.id}`).close()}>
                        <i className="bi bi-x-circle"> </i>
                        No
                    </button>
                </div>
            </ProjectActionModal>

            <ProjectActionModal
                icon="bi bi-exclamation-circle"
                header="Oops!"
                id={"notImplementedFeatureModal"}
            >
                <p className="pt-2">
                    We are sorry, but this feature is not implemented yet. <br/>
                    Please check back later.
                </p>

                <div className="modal-action">
                    <form method="dialog">
                        <button className="btn">Close</button>
                    </form>
                </div>
            </ProjectActionModal>

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
                            <span className={project.status === 'ANALYSIS_TO_BE_REVIEWED' ? 'badge  badge-sm badge-warning' : 'badge badge-sm badge-info'}>
                                <i className={project.status === 'ANALYSIS_TO_BE_REVIEWED' ? 'bi bi-exclamation-circle' : 'bi bi-check2-circle'}> </i> &nbsp;
                                {project.status === 'ANALYSIS_TO_BE_REVIEWED' ? 'PENDING' : 'APPROVED'}
                            </span>

                </td>
                <td>
                    {project.status === 'ANALYSIS_TO_BE_REVIEWED' ? (
                        <span className="badge badge-warning badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-eyeglasses" style={{paddingRight: "0.5vh"}}></i>
                            REVIEW
                        </span>
                    ) : project.status === 'ANALYSIS_COMPLETED' ? (
                        <span className="badge badge-success badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-check2-circle" style={{paddingRight: "0.5vh"}}></i>
                            COMPLETED
                        </span>
                    ) : project.status === 'ANALYSIS_FAILED' ? (
                        <span className="badge badge-error badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-exclamation-triangle" style={{paddingRight: "0.5vh"}}></i>
                            FAILED
                        </span>
                    ) : project.status === 'ANALYSIS_SKIPPED' ? (
                        <span className="badge badge-warning badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-x-circle" style={{paddingRight: "0.5vh"}}></i>
                            SKIPPED
                        </span>
                    ) : project.status === 'ANALYSIS_NOT_STARTED' ? (
                        <span className="badge badge-neutral badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-x-circle" style={{paddingRight: "0.5vh"}}></i>
                            SLEEPING
                        </span>
                    ) : project.status === 'ANALYSIS_STARTED' ? (
                        <span className="badge badge-accent badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-clock" style={{paddingRight: "0.5vh"}}></i>
                            STARTED
                        </span>
                    ) : project.status === 'ANALYSIS_READY' ? (
                        <span className="badge badge-success badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-check2-circle" style={{paddingRight: "0.5vh"}}></i>
                            READY
                        </span>
                    ) : (
                        <span className="badge badge-warning badge-outline badge-sm" style={{fontWeight: "normal"}}>
                            <i className="bi bi-question-octagon" style={{paddingRight: "0.5vh"}}></i>
                            UNKNOWN
                        </span>
                    )}
                </td>

                <td>
                    <div className="flex items-center gap-3">
                        <button className="btn btn-sm btn-success" onClick={handleApprove}
                            disabled={project.status !== 'ANALYSIS_TO_BE_REVIEWED'}
                        >
                            <i className="bi bi-check2-circle"> </i>
                            Approve
                        </button>

                        <button className="btn btn-sm btn-error" onClick={handleReject}
                                disabled={project.status !== 'ANALYSIS_TO_BE_REVIEWED'}
                        >
                            <i className="bi bi-x-circle"> </i>
                            Reject
                        </button>
                        {/*<button className="btn btn-sm btn-outline">*/}
                        {/*    <i className="bi bi-three-dots-vertical"> </i>*/}
                        {/*</button>*/}
                        <details className="dropdown dropdown-end">
                            <summary className="btn btn-sm">
                                <i className="bi bi-three-dots-vertical"> </i>
                            </summary>
                            <ul className="p-2 shadow menu dropdown-content z-[1] bg-base-100 rounded-box w-52">
                                <li>
                                    <button style={{justifyContent: "flex-start"}}
                                            onClick={() => document.getElementById('notImplementedFeatureModal').showModal()}
                                    >
                                        <i className="bi bi-play-circle"> </i>
                                        Analyze Project
                                    </button>
                                </li>

                                <li>
                                    <button style={{justifyContent: "flex-start"}}
                                        onClick={() => document.getElementById('notImplementedFeatureModal').showModal()}
                                    >
                                        <i className="bi bi-eye"> </i>
                                        View Project Details
                                    </button>
                                </li>

                                <li>
                                    <button style={{justifyContent: "flex-start"}}
                                            onClick={() => document.getElementById('notImplementedFeatureModal').showModal()}
                                    >
                                        <i className="bi bi-pencil"> </i>
                                        Edit Project
                                    </button>
                                </li>

                                <li>
                                    <button style={{justifyContent: "flex-start"}}
                                            onClick={() => document.getElementById('notImplementedFeatureModal').showModal()}
                                    >
                                        <i className="bi bi-trash"> </i>
                                        Delete Project
                                    </button>
                                </li>

                                <li>
                                    <a
                                        href={project.repoUrl}
                                        style={{justifyContent: "flex-start"}}
                                        rel={"noreferrer"}
                                        target={"_blank"}
                                    >
                                        <i className="bi bi-github"> </i>
                                        View on Github
                                    </a>
                                </li>
                            </ul>
                        </details>
                    </div>
                </td>
            </tr>
        </>
    )
}