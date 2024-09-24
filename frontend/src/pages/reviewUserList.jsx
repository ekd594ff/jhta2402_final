import React, { useState, useEffect } from "react";
import axios from "axios";
import style from "../styles/reviewUserList.module.scss";
import { Divider, Chip, Rating } from "@mui/material";
import { useNavigate } from "react-router-dom";

const ReviewUserList = () => {
  const [reviews, setReviews] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);
  const [isEndOfData, setIsEndOfData] = useState(false);

  useEffect(() => {
    fetchReviews();
  }, [page]);

  const fetchReviews = async () => {
    try {
      const response = await axios.get(
        `/api/review/list?page=${page}&size=${size}`
      );
      if (!response.data.content.length) {
        setIsEndOfData(true);
      }
      setReviews((prev) => [...prev, ...response.data.content]);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("리뷰를 가져오는 데 실패했습니다.", error);
    }
  };

  const navigator = useNavigate();

  return (
    <div className={style["reviewUserList"]}>
      <div className={style["title"]}>내 리뷰 목록</div>
      {reviews.length ? (
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
                <div className={style["portfolio-link-wrapper"]}>
                  <Chip
                    className={style["chip"]}
                    label="포트폴리오"
                    color="primary"
                    variant="outlined"
                    size="small"
                    onClick={() => {
                      navigator(`/portfolio/${portfolioId}`);
                    }}
                  />
                </div>
              </li>,
            ];
            return render;
          })}
        </ul>
      ) : (
        <div className={style["empty"]}>리뷰 내역이 존재하지 않습니다</div>
      )}
      {isEndOfData ? (
        <></>
      ) : (
        <Divider variant="middle" className={style["divider"]}>
          <Chip
            label="더보기"
            variant="outlined"
            size="small"
            onClick={() => {
              setPage((prev) => prev + 1);
            }}
          />
        </Divider>
      )}
    </div>
  );
};

export default ReviewUserList;
