import './DashboardPage.css'
import DashboardSlideOne from "../../../components/dashboard/slides/DashboardSlideOne/DashboardSlideOne.tsx";
import CollabsibleNavbar from "../../../components/ui/CollabsibleNavbar/CollabsibleNavbar.tsx";
import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import DashboardSlideTwo from "../../../components/dashboard/slides/DashboardSlideTwo/DashboardSlideTwo.tsx";
import DashboardSlideThree from "../../../components/dashboard/slides/DashboardSlideThree/DashboardSlideThree.tsx";
import DashboardSlideFour from "../../../components/dashboard/slides/DashboardSlideFour/DashboardSlideFour.tsx";

function DashboardPage({isAuthenticated, isAdmin}) {
    const location = useLocation()
    const navigate = useNavigate()
    const urlParams = new URLSearchParams(location.search)
    const slideNumber = urlParams.get('p')

    const [currentSlide, setCurrentSlide] = useState<number>(
        slideNumber ? parseInt(slideNumber) : 1
    );

    // Update URL when slide changes
    useEffect(() => {
        navigate(`?p=${currentSlide}`)
    }, [currentSlide, history]);

    return (
        <>
            <CollabsibleNavbar
                isAuthenticated={isAuthenticated}
                isAdmin={isAdmin}
                currentSlide={currentSlide}
                setCurrentSlide={setCurrentSlide}
                totalSlides={4}
            />
            <div className="dashboard-page">
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
    )
}

export default DashboardPage