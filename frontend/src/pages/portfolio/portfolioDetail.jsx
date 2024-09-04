import React, {useEffect, useState} from 'react';
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";

import PortfolioImgListItem from "./portfolio-img-list-item.jsx";
import style from "../../styles/portfolio-detail.module.scss";
import {Backdrop} from "@mui/material";
import List from "@mui/material/List";
import PortfolioSolutionListItem from "./portfolio-solution-list-item.jsx";
import PortfolioReviewListItem from "./portfolio-review-list-item.jsx";
import Divider from '@mui/material/Divider';
import Button from "@mui/material/Button";


import {Swiper, SwiperSlide} from 'swiper/react';

import 'swiper/css';
import 'swiper/css/pagination';


const solutionAJAXPromise = (portfolioId) => axios.get(`/api/solution/portfolio/${portfolioId}`);
const reviewListAJAXPromise = (portfolioId, pagination) =>
    axios.get(`/api/review/portfolio/${portfolioId}?${Object
        .entries(pagination)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`).join('&')}`);

function PortfolioDetail() {
    const {id} = useParams();
    const navigate = useNavigate();

    const [portfolioImgList, setPortfolioImgList] = useState([1, 2, 3, 4, 5, 6, 7, 8, 9, 10]);
    const [modalOpen, setModalOpen] = useState(false);
    const [solutionList, setSolutionList] = useState([]);
    const [modalImg, setModalImg] = useState("");

    const [portfolioInfo, setPortfolioInfo] = useState({
        portfolioId: "",
        title: "",
        description: "",
        company: {
            companyName: "",
            description: "",
            phone: "",
            url: "",
        },
        solution: [],
        imageUrl: [],
    });

    const [reviewInfo, setReviewInfo] = useState({
        review: [],
        page: 0,
        totalPages: 0,
        totalElements: 0,
        size: 5
    });

    const size = 3;

    useEffect(() => {
        const portFolioAJAXPromise = axios.get(`/api/portfolio/${id}`)
            .then((res) => {
                console.log("\n\n--------------------portfolio--------------------\n", res.data);
                setPortfolioInfo({
                    ...portfolioInfo,
                    ...res.data,
                })
            })
            .catch(() => {
                alert("유효하지 않은 페이지입니다.");
                navigate(-1);
            });

        Promise.all([reviewListAJAXPromise(id, {
            page: reviewInfo.page,
            size: reviewInfo.size
        }), portFolioAJAXPromise, solutionAJAXPromise(id)])
            .then(([reviewListResult, portfolioResult, solutionResult]) => {
                const {data: {content, page: {number, totalElements, totalPages}}} = reviewListResult;
                setReviewInfo({...reviewInfo, page: number, totalElements, totalPages, review: content});
                const {data} = solutionResult;
                setSolutionList(data);
            });
    }, [id]); // 의존성 배열 수정

    useEffect(() => {
        if (portfolioImgList.length) {
            setModalImg(`https://picsum.photos/seed/${portfolioImgList[0]}/1200/800`);
        }
    }, [portfolioImgList]);

    useEffect(() => {
        if (modalOpen) {
            document.body.classList.add("modal");
        } else {
            document.body.classList.remove("modal");
        }
    }, [modalOpen]);

    useEffect(() => {
        console.log(reviewInfo);
    }, [
        reviewInfo
    ])

    return (
        <>
            <Header/>
            <main className={style['portfolio-detail']}>
                <div className={style['container']}>
                    <div className={style['top']}>
                        <div className={style['left']}>
                            <div className={style['selected-img']}>
                                <div className={style['selected-img-wrapper']}>
                                    {
                                        portfolioImgList[0] ?
                                            <img alt="selected-img"
                                                 src={`https://picsum.photos/seed/${portfolioImgList[0]}/1200/800`}
                                                 onClick={() => {
                                                     setModalOpen(prev => !prev);
                                                     setModalImg(`https://picsum.photos/seed/${portfolioImgList[0]}/1200/800`)
                                                 }}/> :
                                            <></>
                                    }
                                </div>
                            </div>
                        </div>
                        <div className={style['right']}>
                            <div className={style['info']}>
                                <div>
                                    <span
                                        className={style['company-name']}>{`업체명_${portfolioInfo.company.companyName}`}</span>
                                </div>
                                <div>
                                    <span className={style['title']}>{`포트폴리오제목_${portfolioInfo.title}`}</span>
                                </div>
                                <div>
                                    <span
                                        className={style['description']}>{`포트폴리오 상세_${portfolioInfo.description}`}</span>
                                </div>
                            </div>
                            <div className={style['solution-list-title']}>인테리어 솔루션</div>
                            <List className={style['portfolio-solution-list']}>
                                {solutionList.map((item, index) => {
                                    const render = [(
                                        <PortfolioSolutionListItem key={`${item.id}_${index}`} value={item}/>)];
                                    if (index !== solutionList.length - 1) {
                                        render.push(<Divider variant="middle" key={`${item.id}_${index}_${index}`}/>);
                                    }
                                    return render;
                                })}
                            </List>
                            <Button className={style['solution-list-submit']} size="large" disableRipple>신청하기</Button>
                        </div>
                    </div>
                    <div className={style['middle']}>
                        <Swiper
                            slidesPerView={4}
                            spaceBetween={32}
                            pagination={{
                                clickable: true,
                            }}
                            breakpoints={{
                                960: {
                                    slidesPerView: 4,
                                    spaceBetween: 8
                                },
                                720: {
                                    slidesPerView: 3,
                                    spaceBetween: 6
                                },
                                540: {
                                    slidesPerView: 2,
                                    spaceBetween: 4
                                },
                                320: {
                                    slidesPerView: 1,
                                    spaceBetween: 2
                                }
                            }}
                            className="mySwiper"
                        >
                            {portfolioImgList.map((value) => (
                                <SwiperSlide key={value}>
                                    <PortfolioImgListItem value={value} setter={setModalImg}
                                                          modalOpener={setModalOpen}/>
                                </SwiperSlide>
                            ))}
                        </Swiper>
                    </div>
                    <div className={style['bottom']}>
                        <div className={style['review-list-title']}>리뷰
                            <span>{reviewInfo.totalElements || ""}</span>
                        </div>
                        <ul className={style['review-list']}>
                            {
                                reviewInfo.review.map((item, index) => {
                                    const render = [<PortfolioReviewListItem
                                        {...item}
                                        key={`${index}_${index}`}/>];
                                    if (index !== reviewInfo.review.length - 1) {
                                        render.push(<Divider variant="middle"
                                                             key={`${item.id}_${index}_${index}_dvd}`}/>);
                                    }
                                    return render;
                                })
                            }
                        </ul>
                    </div>
                </div>
            </main>
            <Footer/>
            <Backdrop
                className={style['modal']}
                open={modalOpen}
                onClick={() => {
                    setModalOpen(false);
                }}
            >
                <img alt="modal image"
                     src={modalImg}/>
            </Backdrop>
        </>
    );
}

export default PortfolioDetail;