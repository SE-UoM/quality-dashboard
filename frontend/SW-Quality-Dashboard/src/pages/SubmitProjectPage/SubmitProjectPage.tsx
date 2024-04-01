import "./SubmitProjectPage.css"
import SubmitProjectForm from "../../components/forms/SubmitProjectForm/SubmitProjectForm.tsx";
import {useEffect} from "react";
import useLocalStorage from "../../hooks/useLocalStorage.ts";
import useAuthenticationCheck from "../../hooks/useAuthenticationCheck.ts";
import {Spinner} from "react-bootstrap";

function SubmitProjectPage() {
    const [accessToken] = useLocalStorage<string>('accessToken', '');
    const [isAuthenticated, setIsAuthenticated] = useAuthenticationCheck(accessToken)

    // Redirect to login page if not authenticated
    useEffect(() => {
        if (isAuthenticated === false || accessToken === "" || accessToken === null) {
            window.location.href = '/login';
        }
    }, [isAuthenticated, accessToken]);


    return (
        <>
            <div className="submit-project-page">
                {!isAuthenticated && <Spinner animation="grow" />}
                {isAuthenticated && <SubmitProjectForm/>}
            </div>
        </>
    )
}

export default SubmitProjectPage