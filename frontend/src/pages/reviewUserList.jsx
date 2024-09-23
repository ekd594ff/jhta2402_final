import React, { useState, useEffect } from "react";
import axios from "axios";
import style from "../styles/reviewUserList.module.scss";
import { Divider, Rating } from "@mui/material";

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
      const response = await axios.get(
        `/api/review/list?page=${page}&size=${size}`
      );
      setReviews(response.data.content || []);
      setTotalPages(response.data.totalPages);
      console.log(response);
    } catch (error) {
      console.error("리뷰를 가져오는 데 실패했습니다.", error);
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      setPage((prevPage) => prevPage - 1);
    }
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  return (
    <div className={style["reviewUserList"]}>
      <div className={style["title"]}>내 리뷰 목록</div>
      {/* <table>
        <thead>
          <tr>
            <th>제목</th>
            <th>내용</th>
            <th style={{ width: "100px" }}>평점</th>
          </tr>
        </thead>
        <tbody>
          {reviews.map((review) => (
            <tr key={review.id}>
              <td>{review.title}</td>
              <td className="description-column">{review.description}</td>
              <td>{review.rate}</td>
            </tr>
          ))}
        </tbody>
      </table> */}
      <ul className={style["list"]}>
        {reviews.map((review, idx) => {
          const {
            title,
            createdAt,
            rate,
            description,
            portfolioId,
            totalTransactionAmount,
          } = review;
          const render = [
            <li className={style["item"]} key={review.id}>
              <div className={style["top"]}>
                <span className={style["title"]}>{title}</span>
                <span className={style["date"]}>
                  {createdAt.split("T")[0].replace(/-/g, ".")}
                </span>
              </div>
              <div className={style["middle-1"]}>
                <span className={style["price"]}>
                  {totalTransactionAmount.toLocaleString()}
                </span>
              </div>
              <div className={style["middle"]}>
                <Rating
                  readOnly
                  value={rate}
                  precision={0.5}
                  size="small"
                  sx={{
                    color: "#fa4d56", // 원하는 색상
                  }}
                />
                <span className={style["rate"]}>{rate}</span>
              </div>
              <div className={style["bottom"]}>{description}</div>
            </li>,
          ];
          if (idx !== reviews.length - 1) {
            render.push(<Divider size="middle" />);
          }
          return render;
        })}
      </ul>
      <div>
        <button onClick={handlePreviousPage} disabled={page === 0}>
          이전
        </button>
        <span>
          {page + 1} / {totalPages}
        </span>
        <button onClick={handleNextPage} disabled={page >= totalPages - 1}>
          다음
        </button>
      </div>
    </div>
  );
};

export default ReviewUserList;
