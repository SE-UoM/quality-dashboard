import {Alert, Button, Modal} from "react-bootstrap";
import {useState} from "react";
import axios from "axios";
import apiUrls from "../../../assets/data/api_urls.json";
import {isProduction} from "../../../assets/data/config.json"

function AccountVerificationModal({show, setShow, uid}) {
    const [baseApiUrl, setBaseApiUrl] = useState(isProduction ? apiUrls.productionBackend : apiUrls.developmentBackend);

    const [resendBtnText, setResendBtnText] = useState("Resend Verification Email");
    const [resendBtnDisabled, setResendBtnDisabled] = useState(false);
    const [resendSuccess, setResendSuccess] = useState(false);

    const handleClose = () => setShow(false);

    const handleResend = () => {
        setResendBtnText("Resending...");
        setResendBtnDisabled(true);

        let url = baseApiUrl + apiUrls.routes.resendUserVerification + "?uid=" + uid;

        axios.post(url)
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
                console.log(error);
            });
    }

    return (
        <>
            <Modal
                show={show}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>
                        <i className="bi bi-person-fill-check"> </i>
                        Account Verification
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Alert variant="info">
                        <i className="bi bi-info-circle"> </i>
                        <strong> Please verify your account </strong>
                    </Alert>

                    We sent you an email to verify your account. Please check your email and click on the link to verify your account. <br/><br/>

                    You won't be able to use the dashboard and submit projects until you verify your account. <br/><br/>

                    {resendSuccess &&
                        <Alert variant="success" id="resend-success-alert">
                            <i className="bi bi-mailbox2-flag"></i>
                            <strong> New verification Link sent!</strong>
                        </Alert>
                    }
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleResend} disabled={resendBtnDisabled}>
                        {resendBtnText}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

export default AccountVerificationModal