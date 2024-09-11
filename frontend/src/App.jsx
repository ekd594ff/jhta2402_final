import "./styles/index.scss";
import { BrowserRouter, Route, Routes } from "react-router-dom";
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
import QuotationRequestUserList from "./pages/quotationRequestUserList.jsx";
import QuotationRequestSellerList from "./pages/quotationReqeustSellerList.jsx";
import CompanyDetail from "./pages/login/companyPage.jsx";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Index />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/company/info" element={<CompanyDetail />} />
          <Route path="/company/create" element={<CreateEditCompany />} />
          <Route path="/company/edit" element={<CreateEditCompany />} />
          <Route path="/admin/*" element={<Admin />} />
          <Route path="/portfolio/registration" element={<Registration />} />
          <Route path="/portfolio/:id" element={<PortfolioDetail />} />
          <Route path="/search/detailed" element={<SearchList />} />
          <Route path="/quotationRequest" element={<QuotationRequest />} />
          <Route
            path="/qr/userList/:memberId"
            element={<QuotationRequestUserList />}
          />
          <Route
            path="/qr/sellerList/:companyId"
            element={<QuotationRequestSellerList />}
          />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
