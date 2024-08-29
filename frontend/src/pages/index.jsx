import React, {useEffect, useState} from 'react';
import {Swiper, SwiperSlide} from 'swiper/react';

import Header from "../components/common/header.jsx";
import Footer from "../components/common/footer.jsx";

import RecommendSlideContent from "../components/index/recommend-slide-content.jsx";
import PortfolioListItem  from "../components/index/portfolio-list-item.jsx";

import {Pagination} from 'swiper/modules';

import 'swiper/css';
import 'swiper/css/pagination';

import style from "../styles/index.module.scss";
import SolutionListItem from "../components/index/solution-list-item.jsx";

function Index() {

    const [recommendList, setRecommendList] = useState([1, 2, 3, 4, 5, 6, 7, 8, 9, 10]);
    const [portfolioList, setPortfolioList] = useState([1, 2, 3, 4, 5, 6, 7, 8]);
    const [solutionList, setSolution] = useState([1,2,3,4,5,6,7,8]);

    useEffect(() => {
        console.log(document.cookie);
    }, []);

    return (
        <>
            <Header/>
            <main className={style['index']}>
                <div className={style['container']}>
                    <section className={style['recommend']}>
                        <Swiper
                            pagination={{
                                dynamicBullets: true,
                            }}
                            modules={[Pagination]}
                            className={style['recommend-swiper']}
                        >{recommendList.map((item, index) =>
                            <SwiperSlide key={index}>
                                <RecommendSlideContent value={item + index}/>
                            </SwiperSlide>)}
                        </Swiper>
                    </section>
                    <section className={style['section']}>
                        <div className={style['section-title']}>
                            추천 인테리어 업체
                        </div>
                        <div className={style['section-content']}>
                            <ul className={style['portfolio-list']}>
                                {portfolioList.map((item, index) => {
                                    return <PortfolioListItem value={item} key={index}/>
                                })}
                            </ul>
                        </div>
                    </section>
                    <section className={style['section']}>
                        <div className={style['section-title']}>
                            추천 솔루션
                        </div>
                        <div className={style['section-content']}>
                            <ul className={style['solution-list']}>
                                <SolutionListItem/>
                                <SolutionListItem/>
                                <SolutionListItem/>
                                <SolutionListItem/>
                                <SolutionListItem/>
                                <SolutionListItem/>
                            </ul>
                        </div>
                    </section>
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default Index;