import {Route, Routes} from "react-router-dom";

import AdminIndex from "./pages/index.jsx";
import {useEffect} from "react";
import axios from "axios";


function AdminPortfolioList() {

    useEffect(() => {
        axios.get("/api/portfolio/search?searchWord=title&page=0&size=10")
            .then((res) => console.log(res));
    }, []);

    return <>
        <aside></aside>
        <main>
            <Routes>
                <Route path={"/"} element={<AdminIndex/>}/>
            </Routes>
        </main>
    </>
}

export default AdminPortfolioList;