import {useParams} from "react-router-dom";

import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";

import style from "../../styles/quotation-form.module.scss";
import React from "react";

function QuotationForm() {
    const params = useParams();
    console.log(params);
    return <>
        <Header/>
        <main className={style['quotation-form']}>
            <div className={style['container']}>
                <div className={style['title']}>견적서 작성</div>
                <div className={style['request']}></div>
            </div>
        </main>
        <Footer/>
    </>
}

export default QuotationForm;