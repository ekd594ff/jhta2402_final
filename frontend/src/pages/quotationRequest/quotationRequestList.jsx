import React from 'react';
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import QuotationRequestListComponent from "./quotationRequestListComponent.jsx";

function QuotationRequestList() {
    return (
        <>
            <Header/>
            <QuotationRequestListComponent/>
            <Footer/>
        </>
    );
}

export default QuotationRequestList;