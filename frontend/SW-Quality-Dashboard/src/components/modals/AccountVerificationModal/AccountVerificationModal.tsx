import {Alert, Button, Modal} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import axios from "axios";
import apiUrls from "../../../assets/data/api_urls.json";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL;

function AccountVerificationModal({show, setShow, uid}) {
    const [resendBtnText, setResendBtnText] = useState("Resend Verification Email");
    const [resendBtnDisabled, setResendBtnDisabled] = useState(false);
    const [resendSuccess, setResendSuccess] = useState(false);

    const [error, setError] = useState(null);
    const [errorMessage, setErrorMessage] = useState(null);

    const handleClose = () => setShow(false);

    const handleResend = () => {
        setResendBtnText("Resending...");
        setResendBtnDisabled(true);

        let url = baseApiUrl + apiUrls.routes.resendUserVerification + "?uid=" + uid;

        let headers = {
            'Content-Type': 'application/json'
        }

        axios.post(url, {}, {headers: headers})
            .then((response) => {
                setResendSuccess(true);

                // Prevent the user from spamming the resend button for 5 seconds
                let count = 10;  // Resend button will be enabled after `count` seconds
                const countdownInterval = setInterval(() => {
                    setResendBtnText(`Resend (${count})`);
                    count--;
                    if (count === 0) {
                        clearInterval(countdownInterval);
                        setResendBtnText("Resend Verification Email");
                        setResendBtnDisabled(false);
                    }

                    // After 5 seconds, reset the resend success alert
                    if (count === 5) setResendSuccess(false)
                }, 1000);
            })
            .catch((error) => {
                setError(error);
                setErrorMessage("Failed to resend verification email. Please try again later.");
                setResendBtnText("Resend Verification Email");
                setResendBtnDisabled(false);

                console.warn("Failed to Verify Account: " + error);
            });
    }

    // Effect to open the modal when showModal becomes true
    useEffect(() => {
        if (show) {
            const modal = document.getElementById("my_modal_5");
            modal.showModal();
        }
    }, [show]);

    return (
        <>
            <dialog id="my_modal_5" className="modal modal-bottom sm:modal-middle">
                <div className="modal-box">
                    <form method="dialog">
                        <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                    </form>

                    <h3 className="font-bold text-lg">
                        <i className="bi bi-person-fill-check"> </i>
                        Account Verification
                    </h3>

                    <p className="py-4">
                        <div role="alert" className="alert alert-info">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="stroke-current shrink-0 w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                            <span>
                                 Please verify your account!
                            </span>
                        </div>
                        <br/>

                        We sent you an email to verify your account. Please check your email and click on the provided verification link. <br/> <br/>
                        <strong>You won't be able to use the dashboard and submit projects until you verify your account.</strong>
                    </p>
                    <div className="modal-action">
                        <button className="btn btn-primary" onClick={handleResend} disabled={resendBtnDisabled}>
                            {resendBtnText}
                        </button>
                    </div>
                </div>
            </dialog>

            {/*<Modal*/}
            {/*    show={show}*/}
            {/*    onHide={handleClose}*/}
            {/*    backdrop="static"*/}
            {/*    keyboard={false}*/}
            {/*>*/}
            {/*    <Modal.Header closeButton>*/}
            {/*        <Modal.Title>*/}
            {/*            <i className="bi bi-person-fill-check"> </i>*/}
            {/*            Account Verification*/}
            {/*        </Modal.Title>*/}
            {/*    </Modal.Header>*/}
            {/*    <Modal.Body>*/}
            {/*        <Alert variant="info">*/}
            {/*            <i className="bi bi-info-circle"> </i>*/}
            {/*            <strong> Please verify your account </strong>*/}
            {/*        </Alert>*/}

            {/*        We sent you an email to verify your account. Please check your email and click on the link to verify your account. <br/><br/>*/}

            {/*        You won't be able to use the dashboard and submit projects until you verify your account. <br/><br/>*/}

            {/*        {resendSuccess &&*/}
            {/*            <Alert variant="success" id="resend-success-alert">*/}
            {/*                <i className="bi bi-mailbox2-flag"></i>*/}
            {/*                <strong> New verification Link sent!</strong>*/}
            {/*            </Alert>*/}
            {/*        }*/}
            {/*    </Modal.Body>*/}
            {/*    <Modal.Footer>*/}
            {/*        <Button variant="secondary" onClick={handleClose}>*/}
            {/*            Close*/}
            {/*        </Button>*/}
            {/*        <Button variant="primary" onClick={handleResend} disabled={resendBtnDisabled}>*/}
            {/*            {resendBtnText}*/}
            {/*        </Button>*/}
            {/*    </Modal.Footer>*/}
            {/*</Modal>*/}
        </>
    )
}

export default AccountVerificationModal