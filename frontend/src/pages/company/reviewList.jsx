import React, { useEffect, useState } from "react";
import { Button, Card, CardContent, Typography, Box } from "@mui/material";
import axios from "axios";
import StarRating from "./starRating.jsx";

const ReviewList = ({ companyId }) => {
  const [reviews, setReviews] = useState([]);
  const [reviewPage, setReviewPage] = useState(0);
  const reviewsPerPage = 6; // 6개의 리뷰를 한 번에 불러오기
  const [totalReviews, setTotalReviews] = useState(0); // 전체 리뷰 수를 저장
  const [averageRating, setAverageRating] = useState(0); // 평균 평점 저장

  // 사용자 권한을 확인하여 리뷰 API 호출
  const fetchReviewsBasedOnRole = async (companyId, page, size) => {
    try {
      // 권한을 확인하는 API 호출
      const roleResponse = await axios.get("/api/member/check/role", {
        withCredentials: true,
      });
      const role = roleResponse.data; // 'ROLE_SELLER', 'ROLE_USER', 'ROLE_ADMIN' 등

      let response;
      if (role === "ROLE_SELLER") {
        // 판매자 권한일 경우 회사의 모든 리뷰를 조회
        response = await axios.get("/api/review/company/list", {
          params: { page, size },
          withCredentials: true,
        });
      } else {
        // 일반 사용자나 다른 권한일 경우 특정 회사의 리뷰를 조회
        response = await axios.get(`/api/review/list/company/${companyId}`, {
          params: { page, size },
          withCredentials: true,
        });
      }

      setTotalReviews(response.data.page.totalElements); // 전체 리뷰 수 저장

      // 기존 리뷰 목록에 새로운 리뷰 추가
      const newReviews = response.data.content;
      setReviews((prevReviews) => [...prevReviews, ...newReviews]);

      // 평균 평점 계산
      const totalRating = [...reviews, ...newReviews].reduce(
        (acc, review) => acc + review.rate,
        0
      );

      const avgRating = totalRating / (reviews.length + newReviews.length);
      const result = Math.round(avgRating * 100) / 100;
      setAverageRating(isNaN(result) ? 0 : result); // 소숫점 둘째 자리에서 반올림
    } catch (error) {
      console.error("리뷰를 가져오는 데 실패했습니다.", error);
      alert("리뷰를 불러오는데 문제가 발생했습니다.");
    }
  };

  useEffect(() => {
    if (companyId) {
      fetchReviewsBasedOnRole(companyId, 0, reviewsPerPage);
    }
  }, [companyId]);

  // 리뷰를 추가로 로드
  const loadMoreReviews = () => {
    const nextPage = reviewPage + 1;
    setReviewPage(nextPage);
    fetchReviewsBasedOnRole(companyId, nextPage, reviewsPerPage);
  };

  return (
    <>
      <Typography variant="h6" component="div" sx={{ marginBottom: 4 }}>
        리뷰 평점 평균 : {averageRating} / 5
      </Typography>
      <Box
        sx={{
          display: "grid",
          gap: 2,
          gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
        }}
      >
        {reviews.map((review) => (
          <Card
            key={review.id}
            sx={{ padding: 2, borderRadius: 2, boxShadow: 2 }}
          >
            <CardContent>
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <StarRating rate={review.rate} />
                <Typography variant="body2" color="textSecondary">
                  {new Date(review.createdAt).toLocaleDateString()}
                </Typography>
              </Box>
              <Typography
                variant="h6"
                component="div"
                sx={{ marginTop: 1, fontWeight: "bold" }}
              >
                {review.title}
              </Typography>
              <Typography
                variant="body2"
                color="textSecondary"
                sx={{ marginTop: 1 }}
              >
                작성자: {review.author}
              </Typography>
              <Typography variant="body1" sx={{ marginTop: 1 }}>
                {review.description}
              </Typography>
            </CardContent>
          </Card>
        ))}
      </Box>
      {reviews.length < totalReviews && ( // 모든 리뷰가 로드되면 버튼 숨김
        <Box sx={{ display: "flex", justifyContent: "center", marginTop: 2 }}>
          <Button
            onClick={loadMoreReviews}
            variant="text"
            sx={{ borderColor: "#FA4D56", color: "#FA4D56" }}
          >
            더보기
          </Button>
        </Box>
      )}
    </>
  );
};

export default ReviewList;
