import React, {useEffect, useState} from 'react';
import {Swiper, SwiperSlide} from 'swiper/react';

import Header from "../components/common/header.jsx";
import Footer from "../components/common/footer.jsx";

import RecommendSwiperSlide from "../components/index/recommend-slide-content.jsx";

import {Pagination} from 'swiper/modules';

import 'swiper/css';
import 'swiper/css/pagination';

import style from "../styles/index.module.scss";

function Index({isLoggedIn, username, handleLogout}) {
    useEffect(() => {
        console.log(document.cookie);
    }, []);

    return (
        <>
            <Header isLoggedIn={isLoggedIn} username={username} handleLogout={handleLogout}/>
            <main className={style['index']}>
                <div className={style['container']}>
                    <section className={style['recommend']}>
                        <Swiper
                            pagination={{
                                dynamicBullets: true,
                            }}
                            modules={[Pagination]}
                            className={style['recommend-swiper']}
                        >
                        </Swiper>
                    </section>
                    <section className={style['section']}>
                        <div className={style['section-top']}>
                            <p>강력 추천 인테리어 업체</p>
                        </div>
                        <div className={style['section-content']}>
                            <ul></ul>
                        </div>
                    </section>
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default Index;