import "./styles/index.scss";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import { useState } from "react";
import Index from "./pages/index.jsx";
import Login from "./pages/login/login.jsx";
import Signup from "./pages/login/signup.jsx";
import Admin from "./pages/admin/admin.jsx";
import Mypage from "./pages/login/Mypage.jsx";
import Header from "./components/common/header.jsx";

import PortfolioDetail from "./pages/portfolio/portfolioDetail.jsx";
import Register from "./pages/portfolio/register.jsx";
import CreateCompany from "./pages/company/create.jsx";

function App() {
    return (
        <div className='App'>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Index/>}/>
                    <Route path="/signup" element={<Signup />}/>
                    <Route path="/mypage" element={<Mypage />}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/company/create" element={<CreateCompany/>}/>
                    <Route path="/admin/*" element={<Admin/>}/>
                    <Route path="/portfolio/registration" element={<Register/>}/>
                    <Route path="/portfolio/:id" element={<PortfolioDetail/>}/>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
