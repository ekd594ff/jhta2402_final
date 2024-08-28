import {Route, Routes} from "react-router-dom";

import AdminIndex from "./pages/index.jsx";


function Admin() {
    return <>
        <aside></aside>
        <main>
            <Routes>
                <Route path={"/"} element={<AdminIndex/>}/>
            </Routes>
        </main>
    </>
}

export default Admin;