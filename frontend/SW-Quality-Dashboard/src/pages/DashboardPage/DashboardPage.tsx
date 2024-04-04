import './DashboardPage.css'
import DashboardSlideOne from "../../components/dashboard/slides/DashboardSlideOne/DashboardSlideOne.tsx";
import CollabsibleNavbar from "../../components/ui/CollabsibleNavbar/CollabsibleNavbar.tsx";

function DashboardPage({isAuthenticated, isAdmin}) {
    return (
        <>
            <CollabsibleNavbar isAuthenticated={isAuthenticated} isAdmin={isAdmin} />
            <div className="dashboard-page">
                <DashboardSlideOne />
            </div>
        </>
    )
}

export default DashboardPage