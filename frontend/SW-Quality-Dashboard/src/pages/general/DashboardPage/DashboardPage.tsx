import './DashboardPage.css'
import DashboardSlideOne from "../../../components/dashboard/slides/DashboardSlideOne/DashboardSlideOne.tsx";
import CollabsibleNavbar from "../../../components/ui/CollabsibleNavbar/CollabsibleNavbar.tsx";
import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import DashboardSlideTwo from "../../../components/dashboard/slides/DashboardSlideTwo/DashboardSlideTwo.tsx";
import DashboardSlideThree from "../../../components/dashboard/slides/DashboardSlideThree/DashboardSlideThree.tsx";
import DashboardSlideFour from "../../../components/dashboard/slides/DashboardSlideFour/DashboardSlideFour.tsx";
import ProtectedRoute from "../../../routes/ProtectedRoute.tsx";
import DashboardSlideFive from "../../../components/dashboard/slides/DashboardSlideFive/DashboardSlideFive.tsx";

function DashboardPage({isAuthenticated, isAdmin}) {
    const location = useLocation()
    const navigate = useNavigate()
    const urlParams = new URLSearchParams(location.search)
    const slideNumber = urlParams.get('p')
    const orgId = urlParams.get('orgID')

    const [loadingAuth, setLoadingAuth] = useState<boolean>(false)

    const [currentSlide, setCurrentSlide] = useState<number>(
        slideNumber ? parseInt(slideNumber) : 1
    );

    // Update URL when slide changes
    useEffect(() => {
        navigate(`?p=${currentSlide}&orgID=${orgId}`)
    }, [currentSlide, history]);

    return (
        <>
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
                    <div className="dashboard-page-container">
                        <CollabsibleNavbar
                            isAuthenticated={isAuthenticated}
                            isAdmin={isAdmin}
                            currentSlide={currentSlide}
                            setCurrentSlide={setCurrentSlide}
                            totalSlides={5}
                        />
                        <div className="dashboard-page bg-base-100">
                            {(!currentSlide || currentSlide === 1) &&
                                <DashboardSlideOne orgID={orgId} />
                            }

                            {currentSlide === 2 &&
                                <DashboardSlideTwo orgID={orgId} />
                            }

                            {currentSlide === 3 &&
                                <DashboardSlideThree orgID={orgId} />
                            }

                            {currentSlide === 4 &&
                                <DashboardSlideFour orgID={orgId} />
                            }

                            {currentSlide === 5 &&
                                <DashboardSlideFive orgID={orgId} />
                            }
                        </div>
                    </div>
                </>
            )}
        </>
    )
}

export default DashboardPage