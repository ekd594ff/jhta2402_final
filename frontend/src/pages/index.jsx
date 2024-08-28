import React, {useEffect} from 'react';
import Header from "../components/common/header.jsx";
import Footer from "../components/common/footer.jsx";

import style from "../styles/index.module.scss";


function Index({isLoggedIn, username, handleLogout}) {
    useEffect(() => {
        console.log(document.cookie);
    }, []);
    return (
        <>
            <Header isLoggedIn={isLoggedIn} username={username} handleLogout={handleLogout} />
            <main className={style['index']}>
                <div className={style['container']}>
                    <section className={style['recommend']}></section>
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default Index;