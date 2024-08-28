import "./styles/index.scss";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Header from "./components/common/header.jsx";
import Footer from "./components/common/footer.jsx";
import Index from "./pages/index.jsx";
import Login from "./pages/login/login.jsx";
import Signup from "./pages/login/signup.jsx";
import Test from "./pages/test.jsx";
import List from "./pages/list.jsx";

function App() {

    return (
        <div className='App'>
            <BrowserRouter>
                <Header/>
                <Routes>
                    <Route path="/" element={<Index/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/signup" element={<Signup/>}/>
                    <Route path="/test" element={<Test/>}/>
                    <Route path="/t/t" element={<List/>}/>

                </Routes>
                <Footer/>
            </BrowserRouter>
        </div>
    )
}

export default App
