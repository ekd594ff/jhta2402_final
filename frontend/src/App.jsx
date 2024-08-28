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

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [username, setUsername] = useState('');

    const handleLogout = () => {
        setIsLoggedIn(false);
        setUsername('');
    };

    return (
        <div className='App'>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Index isLoggedIn={isLoggedIn} username={username} handleLogout={handleLogout} />}/>
                    <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} setUsername={setUsername} />}/>
                    <Route path="/signup" element={<Signup />}/>
                    <Route path="/admin" element={<Admin />}/>
                    <Route path="/mypage" element={<Mypage />}/>
                    <Route path="/" element={<Index/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/signup" element={<Signup/>}/>
                    <Route path="/admin/*" element={<Admin/>}/>
                    <Route path="/portfolio/:id" element={<PortfolioDetail/>}/>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
