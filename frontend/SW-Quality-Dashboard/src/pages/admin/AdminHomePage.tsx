import AdminTabContent from "./AdminTabContent/AdminTabContent.tsx";

export default function AdminHomePage() {
    return (
        <>
            <AdminTabContent icon="bi bi-house" title="Home">
                <p>
                    Welcome to the UoM Software Quality Dashboard Admin Panel. <br/>
                    Here you can manage all the users and projects.

                    To get started, please use the sidebar to navigate to the desired page.
                </p>

                <div role="alert" className="alert mt-5">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="stroke-info shrink-0 w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                    <span>
                        Please note that the project is still under development and some features may not work as expected.
                    </span>
                </div>
            </AdminTabContent>
        </>
    );
}