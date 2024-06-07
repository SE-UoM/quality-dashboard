import './FooterCard.css'
import '../../DashboardCardStyle.css'
import logoOpenSourceUoM from '../../../../../assets/img/OS_UoM_Logo.png'
import sdeLogo from '../../../../../assets/img/sde-banner.png'
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";

export default function FooterCard({gridAreaName, loading = false}) {
    return (
        <>
            {loading ? (
                <SimpleDashboardCard
                    id="footerCard"
                    className="skeleton"
                    style={{
                        gridArea: gridAreaName,
                        height: "100%",
                    }}
                >
                </SimpleDashboardCard>
            ) :
                <SimpleDashboardCard
                    id="footerCard"
                    style={{gridArea: gridAreaName}}
                >
                    <img className="uom-logo" src="https://www.uom.gr/site/images/logos/UOMLOGOGR.png"/>
                    <img className="os-uom-logo" src={logoOpenSourceUoM}/>
                    <img className="sde-uom-logo" src={sdeLogo}/>
                </SimpleDashboardCard>
            }
        </>
    )
}