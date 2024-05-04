import "./SubmitProjectPage.css"
import SubmitProjectForm from "../../../components/forms/SubmitProjectForm/SubmitProjectForm.tsx";
import {useEffect, useState} from "react";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import useAuthenticationCheck from "../../../hooks/useAuthenticationCheck.ts";
import {Spinner} from "react-bootstrap";
import ProtectedRoute from "../../../routes/ProtectedRoute.tsx";

function SubmitProjectPage() {
    const [loadingAuth, setLoadingAuth] = useState<boolean>(true)

    return (
        <>
            <ProtectedRoute loadingAuth={loadingAuth} setLoadingAuth={setLoadingAuth} />

            <div className="submit-project-page">
                {loadingAuth ? (
                    <span className="loading loading-infinity loading-lg"></span>
                ) : (
                    <SubmitProjectForm />
                )}
            </div>
        </>
    )
}

export default SubmitProjectPage