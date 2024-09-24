import axios from "axios";

import React, { useEffect, useState } from "react";
import { Swiper, SwiperSlide } from "swiper/react";

import Header from "../components/common/header.jsx";
import Footer from "../components/common/footer.jsx";

import RecommendSlideContent from "../components/index/recommend-slide-content.jsx";
import PortfolioListItem from "../components/index/portfolio-list-item.jsx";
import SolutionListItem from "../components/index/solution-list-item.jsx";
import CompanyListItem from "../components/index/company-list-item.jsx";

import { Pagination, Autoplay } from "swiper/modules";

import "swiper/css";
import "swiper/css/pagination";

import style from "../styles/index.module.scss";

const getRandomPortfolioListPromise = axios.get(
  "/api/portfolio/list/random?count=8"
);
const getTopCompanyPromise = axios.get(`/api/company/list/top/8`);
const getRecommendedPortfolioPromise = axios.get(
  `/api/portfolio/list/recommended?count=${8}`
);
const getLatestTransactionPortfolioPromise = axios.get(
  `/api/portfolio/list/latest/transaction?count=${4}`
);
const getTopSolutionPromise = axios.get(`/api/solution/list/top/${8}`);

function Index() {
  const [recommendList, setRecommendList] = useState([]);
  const [portfolioList, setPortfolioList] = useState([]);
  const [solutionList, setSolutionList] = useState([]);
  const [companyList, setCompanyList] = useState([]);
  const [hotPortfolioList, setHotPortfolioList] = useState([]);

  useEffect(() => {
    Promise.all([
      getRandomPortfolioListPromise,
      getTopCompanyPromise,
      getLatestTransactionPortfolioPromise,
      getRecommendedPortfolioPromise,
      getTopSolutionPromise,
    ]).then(
      ([
        randomPortfolioList,
        getTopCompanyResult,
        hotPortfolioListResult,
        getRecommendedPortfolioResult,
        getTopSolutionListResult,
      ]) => {
        setRecommendList(() => [...hotPortfolioListResult.data]);
        setCompanyList(() => [...getTopCompanyResult.data]);
        setRecommendList(() => [...randomPortfolioList.data]);
        setHotPortfolioList(() => [...hotPortfolioListResult.data]);
        setPortfolioList(() => [...getRecommendedPortfolioResult.data]);
        setSolutionList(() => [...getTopSolutionListResult.data]);
      }
    );
  }, []);

  return (
    <>
      <Header />
      <main className={style["index"]}>
        <div className={style["container"]}>
          <section className={style["recommend"]}>
            <Swiper
              autoplay={{ delay: 3500 }}
              pagination={{
                dynamicBullets: true,
                disableOnInteraction: false,
              }}
              modules={[Pagination, Autoplay]}
              className={style["recommend-swiper"]}
            >
              {recommendList.map((item, index) => (
                <SwiperSlide key={index}>
                  <RecommendSlideContent {...item} />
                </SwiperSlide>
              ))}
            </Swiper>
          </section>
          <section className={style["section"]}>
            <div className={style["section-title"]}>TOP 시공업체</div>
            <div className={style["section-content"]}>
              <ul className={style["company-list"]}>
                {companyList.map((item, index) => (
                  <CompanyListItem {...item} rank={index} key={item.id} />
                ))}
              </ul>
            </div>
          </section>
          <section className={style["section"]}>
            <div className={style["section-title"]}>추천 포트폴리오</div>
            <div className={style["section-content"]}>
              <ul className={style["portfolio-list"]}>
                {portfolioList.map((item, index) => {
                  return (
                    <PortfolioListItem
                      {...item}
                      key={`${item.portfolioid}_${index}_rec`}
                    />
                  );
                })}
              </ul>
            </div>
          </section>
          <section className={style["section"]}>
            <div className={style["section-title"]}>추천 솔루션</div>
            <div className={style["section-content"]}>
              <ul className={style["solution-list"]}>
                {solutionList.map((item, index) => {
                  return (
                    <SolutionListItem
                      {...item}
                      key={`${index}_${item.id}_sol`}
                    />
                  );
                })}
              </ul>
            </div>
          </section>
          <section className={style["section"]}>
            <div className={style["section-title"]}>요즘 뜨는 인테리어</div>
            <div className={style["section-content"]}>
              <ul className={style["portfolio-list"]}>
                {hotPortfolioList.map((item, index) => {
                  return (
                    <PortfolioListItem
                      {...item}
                      key={`${item.portfolioid}_${index}_hot`}
                    />
                  );
                })}
              </ul>
            </div>
          </section>
        </div>
      </main>
      <Footer />
    </>
  );
}

export default Index;
