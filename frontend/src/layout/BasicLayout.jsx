import React from 'react';
import Header from "../components/common/header.jsx";
import Footer from "../components/common/footer.jsx";


function BasicLayout({children}) {
    return (
        <>
            <Header/>
            {children}
            <Footer/>
        </>
    );
}

export default BasicLayout;