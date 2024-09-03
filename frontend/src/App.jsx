import "./styles/index.scss";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Index from "./pages/index.jsx";
import Login from "./pages/login/login.jsx";
import Signup from "./pages/login/signup.jsx";
import Admin from "./pages/admin/admin.jsx";
import Mypage from "./pages/login/Mypage.jsx";

import PortfolioDetail from "./pages/portfolio/portfolioDetail.jsx";
import CreateCompany from "./pages/company/create.jsx";
import Registration from "./pages/portfolio/registration.jsx";
import SearchList from "./pages/searchlist.jsx";
import QuotationRequest from "./pages/quotationRequest.jsx";

function App() {
    return (
        <div className='App'>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Index/>}/>
                    <Route path="/signup" element={<Signup/>}/>
                    <Route path="/mypage" element={<Mypage/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/company/create" element={<CreateCompany/>}/>
                    <Route path="/admin/*" element={<Admin/>}/>
                    <Route path="/portfolio/registration" element={<Registration/>}/>
                    <Route path="/portfolio/:id" element={<PortfolioDetail/>}/>
                    <Route path="/search/detailed" element={<SearchList/>}/>
                    <Route path="/quotationRequest" element={<QuotationRequest/>}/>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
