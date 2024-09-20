import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/reviewUserList.scss';
import { Typography } from '@mui/material';

const ReviewUserList = () => {
    const [reviews, setReviews] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        fetchReviews();
    }, [page]);

    const fetchReviews = async () => {
        try {
            const response = await axios.get(`/api/review/list?page=${page}&size=${size}`);
            console.log(response.data);
            setReviews(response.data.content || []);
            setTotalPages(response.data.totalPages);
        } catch(error) {
            console.error("리뷰를 가져오는 데 실패했습니다.", error);
        }
    };

    const handlePreviousPage = () => {
        if (page > 0) {
            setPage(prevPage => prevPage - 1);
        }
    };

    const handleNextPage = () => {
        if (page < totalPages - 1) {
            setPage(prevPage => prevPage + 1);
        }
    };

    return (
        <div>
            <Typography variant="h4" className="review-title" style={{ textAlign: "center", marginTop: "60px" }}>
                내 리뷰 목록
            </Typography>
            <hr style={{ marginBottom: '20px', border: '1px solid #ddd'}}/>
            <table style={{ margin: '0 20px'}}>
                <thead>
                    <tr>
                        <th>제목</th>
                        <th>내용</th>
                        <th style={{ width: '100px'}}>평점</th>
                    </tr>
                </thead>
                <tbody>
                    {reviews.map((review) => (
                        <tr key={review.id}>
                            <td>{review.title}</td>
                            <td className='description-column'>{review.description}</td>
                            <td>{review.rate}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <div style={{ marginTop: '20px', marginLeft: '20px'}}>
                <button onClick={handlePreviousPage} disabled={page === 0}>
                    이전
                </button>
                <span style={{margin: '0 10px' }}>{page + 1} / {totalPages}</span>
                <button onClick={handleNextPage} disabled={page >= totalPages - 1}>
                    다음
                </button>
            </div>
        </div>
    );
};

export default ReviewUserList;