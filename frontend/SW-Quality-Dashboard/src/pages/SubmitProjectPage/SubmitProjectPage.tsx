import "./SubmitProjectPage.css"
import SubmitProjectForm from "../../components/forms/SubmitProjectForm/SubmitProjectForm.tsx";
import {useEffect} from "react";
import useLocalStorage from "../../hooks/useLocalStorage.ts";
import useAuthenticationCheck from "../../hooks/useAuthenticationCheck.ts";

function SubmitProjectPage() {
    let [accessToken] = useLocalStorage<string>('accessToken', '');
    let [isAuthenticated] = useAuthenticationCheck(accessToken)

    // If the user is not authenticated, redirect to the login page
    useEffect(() => {
        // Wait for the authentication check to finish
        if (isAuthenticated === null)
            return

        if (!isAuthenticated || accessToken === "" || accessToken === null) {
            window.location.href = '/login'
        }
    }, [isAuthenticated]);


    return (
        <>
            <div className="submit-project-page">
                <SubmitProjectForm />
            </div>
        </>
    )
}

export default SubmitProjectPage