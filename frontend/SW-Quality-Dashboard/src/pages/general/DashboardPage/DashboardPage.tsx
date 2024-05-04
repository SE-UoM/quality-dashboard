import './DashboardPage.css'
import DashboardSlideOne from "../../../components/dashboard/slides/DashboardSlideOne/DashboardSlideOne.tsx";
import CollabsibleNavbar from "../../../components/ui/CollabsibleNavbar/CollabsibleNavbar.tsx";
import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import DashboardSlideTwo from "../../../components/dashboard/slides/DashboardSlideTwo/DashboardSlideTwo.tsx";
import DashboardSlideThree from "../../../components/dashboard/slides/DashboardSlideThree/DashboardSlideThree.tsx";
import DashboardSlideFour from "../../../components/dashboard/slides/DashboardSlideFour/DashboardSlideFour.tsx";
import ProtectedRoute from "../../../routes/ProtectedRoute.tsx";
import SubmitProjectForm from "../../../components/forms/SubmitProjectForm/SubmitProjectForm.tsx";

function DashboardPage({isAuthenticated, isAdmin}) {
    const location = useLocation()
    const navigate = useNavigate()
    const urlParams = new URLSearchParams(location.search)
    const slideNumber = urlParams.get('p')

    const [loadingAuth, setLoadingAuth] = useState<boolean>(true)

    const [currentSlide, setCurrentSlide] = useState<number>(
        slideNumber ? parseInt(slideNumber) : 1
    );

    // Update URL when slide changes
    useEffect(() => {
        navigate(`?p=${currentSlide}`)
    }, [currentSlide, history]);

    return (
        <>
            <ProtectedRoute loadingAuth={loadingAuth} setLoadingAuth={setLoadingAuth} />

            {loadingAuth ? (
                <div
                    className="loading-item"
                    style={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        height: "100vh"
                    }}
                >
                    <span className="loading loading-infinity loading-lg"></span>
                </div>
            ) : (
                <>
                    <CollabsibleNavbar
                        isAuthenticated={isAuthenticated}
                        isAdmin={isAdmin}
                        currentSlide={currentSlide}
                        setCurrentSlide={setCurrentSlide}
                        totalSlides={4}
                    />
                    <div className="dashboard-page bg-primary">
                        {(!currentSlide || currentSlide === 1) &&
                            <DashboardSlideOne />
                        }

                        {currentSlide === 2 &&
                            <DashboardSlideTwo />
                        }

                        {currentSlide === 3 &&
                            <DashboardSlideThree />
                        }

                        {currentSlide === 4 &&
                            <DashboardSlideFour />
                        }
                    </div>
                </>
            )}
        </>
    )
}

export default DashboardPage