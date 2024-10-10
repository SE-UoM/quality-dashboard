import './FooterCard.css'
import '../../DashboardCardStyle.css'
import logoOpenSourceUoM from '../../../../../assets/img/OS_UoM_Logo.png'
import sdeLogo from '../../../../../assets/img/sde-banner.png'
import SimpleDashboardCard from "../../SimpleDashboardCard.tsx";
import {anonymizeCreators} from "../../../../../assets/data/config.json"

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
                    <div className="tooltip tooltip-top" style={{display: "flex", width: "100%", alignItems: "center", justifyContent: "space-between"}} data-tip={anonymizeCreators ? 'Omitted for Anonymity' : ''}>
                    <img className={anonymizeCreators ? 'anonymized uom-logo' : 'uom-logo'} src="https://www.uom.gr/site/images/logos/UOMLOGOGR.png"/>
                    <img className={anonymizeCreators ? 'anonymized os-uom-logo' : 'os-uom-logo'} src={logoOpenSourceUoM}/>
                    <img className={anonymizeCreators ? 'anonymized sde-uom-logo' : 'sde-uom-logo'} src={sdeLogo}/>
                    </div>
                </SimpleDashboardCard>
            }
        </>
    )
}