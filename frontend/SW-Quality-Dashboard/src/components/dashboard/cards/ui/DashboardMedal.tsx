import './DashboardMedal.css'
import medalIcon from "../../../../assets/svg/dashboardIcons/simple_medal_icon.svg";

export default function DashboardMedal({rank, medalClass}) {
    return (
        <>
            <div className={"best-project-medal " + medalClass}
                 style={{
                     gridArea: "medal",
                     display: "flex",
                     justifyContent: "center",
                     alignItems: "center",
                     height: "7vh",
                     width: "7vh",
                     margin: "1vh",
                     justifySelf: "center",
                 }}
            >
                <span
                    style={{
                        backgroundImage: `url(${medalIcon}), `,
                        backgroundSize: "85%",
                        backgroundRepeat: "no-repeat",
                        backgroundPositionY: "calc(55% + 0.5vh)", // Center the image
                        backgroundPositionX: "calc(17% + 0.5vh)", // Center the image
                        width: "100%",
                        height: "100%",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        opacity: "0.7",
                        fontSize: "4vh",
                    }}
                >
                    {rank}
                </span>
            </div>
        </>
    )
}