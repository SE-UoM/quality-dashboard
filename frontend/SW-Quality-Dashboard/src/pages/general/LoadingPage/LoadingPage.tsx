import './LoadingPage.css'
import logo from '../../../assets/dashboard_logo_transparent.png'

function LoadingPage() {
    return (
        <div className="loading-page">
            <div className="loading-page-content">
                <img src={logo}/>
            </div>
        </div>
    )
}

export default LoadingPage