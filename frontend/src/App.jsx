import "./styles/index.scss";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Index from "./pages/index.jsx";
import Login from "./pages/login/login.jsx";
import Signup from "./pages/login/signup.jsx";
import List from "./pages/list.jsx";
import AdminPortfolioList from "./pages/admin/admin.jsx";

function App() {

    return (
        <div className='App'>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Index/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/signup" element={<Signup/>}/>
                    <Route path="/test" element={<List/>}/>
                    <Route path="/admin/list" element={<AdminPortfolioList/>}/>
                </Routes>
            </BrowserRouter>
        </div>
    )
}

export default App
