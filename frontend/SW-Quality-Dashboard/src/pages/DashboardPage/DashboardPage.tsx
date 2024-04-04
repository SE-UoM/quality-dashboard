import './DashboardPage.css'
import DashboardSlideOne from "../../components/dashboard/slides/DashboardSlideOne/DashboardSlideOne.tsx";
import CollabsibleNavbar from "../../components/ui/CollabsibleNavbar/CollabsibleNavbar.tsx";
import {useLocation} from "react-router-dom";
import {useState} from "react";
import DashboardSlideTwo from "../../components/dashboard/slides/DashboardSlideTwo/DashboardSlideTwo.tsx";

function DashboardPage({isAuthenticated, isAdmin}) {
    const location = useLocation()
    const urlParams = new URLSearchParams(location.search)
    const slideNumber = urlParams.get('p')

    const [currentSlide, setCurrentSlide] = useState<number>(slideNumber ? parseInt(slideNumber) : 1)

    console.log("Current Slide:" + currentSlide)

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
                    <h1>Slide 3</h1>
                }

                {currentSlide === 4 &&
                    <h1>Slide 4</h1>
                }
            </div>
        </>
    )
}

export default DashboardPage