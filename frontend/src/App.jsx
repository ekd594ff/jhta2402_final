import "./styles/index.scss";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Index from "./pages/index.jsx";
import Login from "./pages/login/login.jsx";
import Signup from "./pages/login/signup.jsx";
import Admin from "./pages/admin/admin.jsx";
import MyPage from "./pages/login/mypage.jsx";

import PortfolioDetail from "./pages/portfolio/portfolioDetail.jsx";
import Registration from "./pages/portfolio/registration.jsx";
import CreateEditCompany from "./pages/company/createEdit.jsx";
import SearchList from "./pages/searchlist.jsx";
import QuotationRequest from "./pages/quotationRequest.jsx";
import CompanyDetail from "./pages/company/companyPage.jsx";
import QuotationRequestList from "./pages/quotationRequest/quotationRequestList.jsx";
import QuotationRequestDetail from "./pages/quotationRequest/quotationRequestDetail.jsx";
import QuotationForm from "./pages/quotation/quotation-form.jsx";
import ReportUserList from "./pages/reportUserList.jsx";
import ReviewUserList from "./pages/reviewUserList.jsx";

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Index/>}/>
                    <Route path="/signup" element={<Signup/>}/>
                    <Route path="/mypage/*" element={<MyPage/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/company/:id" element={<CompanyDetail/>}/>
                    <Route path="/company/info" element={<CompanyDetail/>}/>
                    <Route path="/company/create" element={<CreateEditCompany/>}/>
                    <Route path="/company/edit" element={<CreateEditCompany/>}/>
                    <Route path="/admin/*" element={<Admin/>}/>
                    <Route path="/portfolio/registration" element={<Registration/>}/>
                    <Route path="/portfolio/:id" element={<PortfolioDetail/>}/>
                    <Route path="/portfolio/edit/:id" element={<Registration/>}/>
                    <Route path="/search/detailed" element={<SearchList/>}/>
                    <Route path="/quotationRequest" element={<QuotationRequest/>}/>
                    <Route path="/quotationRequest/member" element={<QuotationRequestList/>}/>
                    <Route path="/quotationRequest/company" element={<QuotationRequestList/>}/>
                    <Route path="/quotationRequest/:id" element={<QuotationRequestDetail/>}/>
                    <Route path="/quotation/form/:requestId" element={<QuotationForm/>}/>
                    <Route path="/reportUserList/:id" element={<ReportUserList />} />
                    <Route path="/reviewUserList" element={<ReviewUserList />}/>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
