import React, {useEffect, useState} from 'react';
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";

import style from "../../styles/portfolio-detail.module.scss";


function PortfolioDetail() {

    const {id} = useParams();
    const navigate = useNavigate();

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
        totalPage: 0,
    });

    const size = 3;

    useEffect(() => {
        axios.get(`/api/portfolio/${id}`)
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
    }, []);

    useEffect(() => {
        axios.get(`/api/review/portfolio/${id}?page=${reviewInfo.page}&size=${size}`)
            .then((res) => {
                console.log("\n\n--------------------review--------------------\n", res.data);
                setReviewInfo(
                    {
                        review: res.data.content,
                        page: res.data.page.number,
                        totalPage: res.data.page.totalPage,
                    }
                );
            }).catch((err) => console.log(err));
    }, [reviewInfo.page]);

    return (
        <>
            <Header/>
            <main className={style['portfolio-detail']}>
                <div className={style['container']}></div>
            </main>
            <Footer/>
        </>
    );
}

export default PortfolioDetail;

/*

<main key={portfolioInfo.portfolioId} className={style['index']}>
                <div>
                    {portfolioInfo.portfolioId}
                    <br/>
                    {portfolioInfo.title}
                    <br/>
                    {portfolioInfo.description}
                    <br/>
                    {portfolioInfo.imageUrl.map((url, index) =>
                        <div key={index}>
                            <img src={url}/>
                        </div>
                    )}
                    <br/>
                    <br/>

                    {portfolioInfo.company.companyName}
                    <br/>
                    {portfolioInfo.company.description}
                    <br/>
                    {portfolioInfo.company.phone}
                    <br/>
                    <img src={portfolioInfo.company.url}/>
                    <br/>
                    <br/>

                    {portfolioInfo.solution.map((solution) =>
                        <div id={solution.id}>
                            {solution.id}
                            <br/>
                            {solution.title}
                            <br/>
                            {solution.description}
                            <br/>
                            {solution.price}
                        </div>)}
                    <br/>
                    <br/>

                    {reviewInfo.review.map((review) =>
                        <div id={review.id}>
                            <br/>
                            {review.title}
                            <br/>
                            {review.description}
                            <br/>
                            {review.rate}
                        </div>)}

                </div>
            </main>


* */