import React from 'react';
import Header from "../components/common/header.jsx";
import Footer from "../components/common/footer.jsx";

import style from "../styles/index.module.scss";


function Index(props) {
    return (
        <>
            <Header/>
            <main className={style['index']}>
                <div className={style['container']}></div>
            </main>
            <Footer/>
        </>
    );
}

export default Index;