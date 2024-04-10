import './FooterCard.css'
import '../../DashboardCardStyle.css'
import logoOpenSourceUoM from '../../../../../assets/img/OS_UoM_Logo.png'
import sdeLogo from '../../../../../assets/img/sde-banner.png'

function FooterCard({gridAreaName}) {
    return (
        <>
            <div
                className={"dashboard-card " }
                id="footerCard"
                style={{gridArea: gridAreaName}}
            >
                <img className="uom-logo" src="https://www.uom.gr/site/images/logos/UOMLOGOGR.png"/>
                <img className="os-uom-logo" src={logoOpenSourceUoM}/>
                <img className="sde-uom-logo" src={sdeLogo}/>
            </div>
        </>
    )
}

export default FooterCard