import './AdminTabContent.css'

function AdminTabContent({icon, title, children}) {
    return (
        <div>
            <h2 style={{fontSize: "4vh"}}>
                <i className={icon}> </i>
                {title}
            </h2>
            <span className="divider"></span>

            <div>
                {children}
            </div>
        </div>
    )
}

export default AdminTabContent