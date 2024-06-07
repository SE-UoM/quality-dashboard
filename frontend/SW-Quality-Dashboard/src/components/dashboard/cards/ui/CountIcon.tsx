import {formatText} from "../../../../utils/textUtils.ts";

export default function CountIcon({icon, title, count}) {
    return (
        <div
            style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
            }}
        >
            <i className={icon} style={{fontSize: "5.5vh"}}> </i>
            <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    marginLeft: "1vh",
                }}
            >
                <div style={{fontSize: "2vh", textWrap: "nowrap"}}>
                    {title}
                </div>

                <div style={{fontSize: "4vh", fontWeight: "bold", lineHeight: "1"}}>
                    {formatText(count, "k")}
                </div>
            </div>
        </div>
    )
}