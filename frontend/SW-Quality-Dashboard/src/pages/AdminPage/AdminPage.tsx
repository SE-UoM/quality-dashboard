import './AdminPage.css'

function AdminPage({icon, title, children}) {
    return (
        <div className="admin-page">
            <h2>
                <i className={icon}> </i>
                {title}
            </h2>

            <div className="admin-page-content">
                {children}
            </div>
        </div>
    )
}

export default AdminPage