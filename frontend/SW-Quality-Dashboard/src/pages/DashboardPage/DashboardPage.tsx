import './DashboardPage.css'
import DashboardSlideOne from "../../components/dashboard/slides/DashboardSlideOne/DashboardSlideOne.tsx";
import CollabsibleNavbar from "../../components/ui/CollabsibleNavbar/CollabsibleNavbar.tsx";

function DashboardPage() {
    return (
        <>
            <CollabsibleNavbar />
            <div className="dashboard-page">
                <DashboardSlideOne />
            </div>
        </>
    )
}

export default DashboardPage