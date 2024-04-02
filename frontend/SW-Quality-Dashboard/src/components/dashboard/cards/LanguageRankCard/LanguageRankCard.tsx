import './LanguageRankCard.css'
import '../DashboardCardStyle.css'
import firstMedal from '../../../../assets/svg/dashboardIcons/first_medal_icon.svg'
import secondMedal from '../../../../assets/svg/dashboardIcons/second_medal_icon.svg'
import thirdMedal from '../../../../assets/svg/dashboardIcons/third_medal_icon.svg'


function LanguageRankCard() {
    return (
        <>
            <div className="dashboard-card" id="languageRank">
                <h2>
                    <i className="bi bi-trophy-fill"> </i>
                    Top Languages in UoM
                </h2>

                <div className="language-rank-container">
                    <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            Java
                        </span>
                        <div className="language-rank-line" id={"second"}>
                            <img src={secondMedal} className="medal-icon"/>
                        </div>
                    </div>

                    <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            Python
                        </span>
                        <div className="language-rank-line" id={"first"}>
                            <img src={firstMedal} className="medal-icon"/>
                        </div>
                    </div>

                    <div className="language-rank">
                        <span className={"lang-name-rank"}>
                            C++
                        </span>
                        <div className="language-rank-line" id={"third"}>
                            <img src={thirdMedal} className="medal-icon"/>
                        </div>
                    </div>

                </div>
            </div>
        </>
    )
}

export default LanguageRankCard