import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Typography,
  Button,
  Snackbar,
  Grid2,
  Card,
  CardContent,
  Alert,
  Box,
  Backdrop,
} from "@mui/material";
import style from "../../styles/quotationRequest-list.module.scss";
import { useNavigate } from "react-router-dom";
import { CheckCircle, Image, Pending } from "@mui/icons-material";
import { dateFormatter } from "../../utils/dateUtil.jsx";
import TextField from "@mui/material/TextField";
import Rating from "@mui/material/Rating";

import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import ListAltIcon from "@mui/icons-material/ListAlt";

const QuotationRequestListComponent = () => {
  const path = window.location.pathname;
  const navigate = useNavigate();

  const [quotationRequests, setQuotationRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pageInfo, setPageInfo] = useState({
    progress: "PENDING", // PENDING, APPROVED, ALL
    page: 0,
    totalPage: 0,
    size: 6,
  });
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [reviewModal, setReviewModal] = useState({
    open: false,
    isEdit: false,
    readOnly: true,
    id: "",
    title: "",
    description: "",
    rate: 5.0,
    quotationRequestId: "",
  });
  const [reviewHoverRate, setReviewHoverRate] = useState(5.0);

  useEffect(() => {
    const fetchQuotationRequests = async () => {
      setLoading(true);
      setError(null);

      const url = path.endsWith("member")
        ? `/api/quotationRequest/list?progress=${pageInfo.progress}&page=${pageInfo.page}&pageSize=${pageInfo.size}`
        : `/api/quotationRequest/companyList?progress=${pageInfo.progress}&page=${pageInfo.page}&pageSize=${pageInfo.size}`;

      try {
        const response = await axios.get(url);
        pageInfo.page === 0
          ? setQuotationRequests(response.data.content)
          : setQuotationRequests((prevRequests) => [
              ...prevRequests,
              ...response.data.content,
            ]);
        setPageInfo({ ...pageInfo, totalPage: response.data.page.totalPages });
      } catch (err) {
        console.error(err);
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchQuotationRequests();
  }, [pageInfo.page, pageInfo.progress]);

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  const updateProgress = async (id) => {
    if (!confirm("해당 거래를 취소하시겠습니까?")) return;

    const url = path.endsWith("member")
      ? `/api/quotationRequest/cancel/${id}`
      : `/api/quotationRequest/seller/cancel/${id}`;

    try {
      await axios.put(url);
      setSnackbarMessage("진행 상태가 업데이트되었습니다.");
      setSnackbarOpen(true);
      setPageInfo({ ...pageInfo, progress: "ALL", page: 0 });
    } catch (err) {
      console.error(err);
      setSnackbarMessage("진행 상태 업데이트 실패.");
      setSnackbarOpen(true);
    }
  };

  const setProgress = (progress) => {
    if (pageInfo.progress !== progress) {
      setPageInfo({ ...pageInfo, progress: progress, page: 0 });
    }
  };

  const progressIcon = (progress) => {
    if (progress === "PENDING") {
      return <Pending />;
    } else if (progress === "APPROVED") {
      return <CheckCircle />;
    } else if (progress === "USER_CANCELLED") {
      return "유저 취소";
    } else if (progress === "SELLER_CANCELLED") {
      return "판매자 취소";
    } else if (progress === "ADMIN_CANCELLED") {
      return "관리자 취소";
    } else {
      return "진행 상태";
    }
  };

  const openReviewModal = (review, isEdit, readOnly, quotationRequestId) => {
    if (readOnly) {
      setReviewModal({
        open: true,
        readOnly: readOnly,
        title: review.title,
        description: review.description,
        rate: review.rate,
        quotationRequestId: quotationRequestId,
      });
      setReviewHoverRate(review.rate);
    } else if (isEdit) {
      setReviewModal({
        open: true,
        isEdit: isEdit,
        readOnly: readOnly,
        id: review.id,
        title: review.title,
        description: review.description,
        rate: review.rate,
        quotationRequestId: quotationRequestId,
      });
      setReviewHoverRate(review.rate);
    } else {
      setReviewModal({
        open: true,
        isEdit: isEdit,
        readOnly: readOnly,
        id: "",
        title: "",
        description: "",
        rate: 5.0,
        quotationRequestId: quotationRequestId,
      });
    }
  };

  const updateQuotationRequest = () => {
    setQuotationRequests((prevState) =>
      prevState.map((request) =>
        request.review.id === reviewModal.id
          ? {
              ...request,
              review: {
                id: request.review.id,
                title: reviewModal.title,
                description: reviewModal.description,
                rate: reviewModal.rate,
              },
            }
          : request
      )
    );
  };

  const createReview = (review) => {
    setQuotationRequests((prevState) =>
      prevState.map((request) =>
        request.id === reviewModal.quotationRequestId
          ? {
              ...request,
              review: {
                id: review.id,
                title: review.title,
                description: review.description,
                rate: review.rate,
              },
            }
          : request
      )
    );
  };

  const saveEditReview = (quotationRequestId) => {
    if (
      !confirm(
        reviewModal.isEdit
          ? "리뷰를 수정하시겠습니까?"
          : "리뷰를 작성하시겠습니까?"
      )
    )
      return;

    const url = reviewModal.isEdit
      ? "/api/review/update"
      : `/api/review/quotationRequest/${quotationRequestId}`;

    const data = reviewModal.isEdit
      ? reviewModal
      : {
          title: reviewModal.title,
          description: reviewModal.description,
          rate: reviewModal.rate,
        };

    axios
      .post(url, data)
      .then((res) => {
        alert(reviewModal.isEdit ? "수정되었습니다." : "생성되었습니다.");
        reviewModal.isEdit ? updateQuotationRequest() : createReview(res.data);
        setReviewModal({ open: false });
      })
      .catch(() => alert("문제가 발생했습니다."));
  };

  if (error) {
    alert("오류가 발생했습니다.");
    navigate(-1);
  }

  return (
    <main className={style["main"]}>
      <div className={style["container"]}>
        <div className={style["title"]}>
          {path.endsWith("company")
            ? "회사 견적신청서 목록"
            : "견적신청서 목록"}
        </div>
        <div container spacing={10} className={style["qr-grid-container"]}>
          <Button
            variant="outlined"
            className={
              style[pageInfo.progress === "PENDING" ? "focus-button" : "button"]
            }
            onClick={() => setProgress("PENDING")}
            startIcon={<MoreHorizIcon />}
          >
            진행
          </Button>
          <Button
            variant="outlined"
            className={
              style[
                pageInfo.progress === "APPROVED" ? "focus-button" : "button"
              ]
            }
            onClick={() => setProgress("APPROVED")}
            startIcon={<CheckCircleIcon />}
          >
            완료
          </Button>
          <Button
            variant="outlined"
            className={
              style[pageInfo.progress === "ALL" ? "focus-button" : "button"]
            }
            onClick={() => setProgress("ALL")}
            startIcon={<ListAltIcon />}
          >
            전체
          </Button>
        </div>
        <Grid2 container spacing={2} className={style["qr-card-container"]}>
          {quotationRequests.length === 0 && (
            <div className={style["no-content-div"]}>
              해당 조건의 견적신청서가 없습니다.
            </div>
          )}
          {quotationRequests.map((request, index) => (
            <div
              key={`${index}_${request.id}`}
              size={{ sx: 12 }}
              sx={{ width: "100%" }}
              className={style["qr-card-grid"]}
            >
              <Card variant="outlined" className={style["qr-card"]}>
                <CardContent className={style["qr-card-content"]}>
                  <div className={style["image-div"]}>
                    <div className={style["portfolio-div"]}>
                      {request.portfolio.url ? (
                        <Box
                          component="img"
                          src={request.portfolio.url}
                          sx={{
                            width: "64px",
                            height: "'64px",
                            objectFit: "cover",
                            borderRadius: "50%",
                          }}
                        />
                      ) : (
                        <Image />
                      )}
                      <Typography>{request.portfolio.title}</Typography>
                    </div>
                    <Typography
                      variant="subtitle1"
                      className={style[request.progress]}
                    >
                      {progressIcon(request.progress)}
                    </Typography>
                  </div>
                  {path.endsWith("company") && (
                    <div className={style["member-div"]}>
                      <div className={style["member-title-div"]}>
                        <div
                          className={style["title"]}
                          sx={{ fontSize: "18px" }}
                        >
                          {request.title}
                        </div>
                        <div className={style["member-info"]}>
                          <div className={style["member-username"]}>
                            {request.member.username}
                          </div>
                        </div>
                      </div>
                      <div className={style["member-content-div"]}>
                        <div
                          sx={{
                            display: "-webkit-box",
                            overflow: "hidden",
                            WebkitBoxOrient: "vertical",
                            WebkitLineClamp: 2,
                            minHeight: "3em",
                          }}
                        >
                          {request.description}
                        </div>
                      </div>
                    </div>
                  )}
                  <div className={style["info-div"]}></div>
                  <div className={style["bottom-div"]}>
                    <div className={style["date-div"]}>
                      {dateFormatter(request.updatedAt)}
                    </div>
                    <div className={style["button-div"]}>
                      {request.progress === "PENDING" && (
                        <Button
                          className={style["button"]}
                          onClick={() => updateProgress(request.id)}
                        >
                          거래 취소
                        </Button>
                      )}
                      {request.progress === "APPROVED" &&
                        (path.endsWith("member") ? (
                          !!request.review.id ? (
                            <Button
                              className={style["button"]}
                              onClick={() =>
                                openReviewModal(
                                  request.review,
                                  true,
                                  false,
                                  request.id
                                )
                              }
                            >
                              리뷰 수정
                            </Button>
                          ) : (
                            <Button
                              className={style["button"]}
                              onClick={() =>
                                openReviewModal(
                                  request.review,
                                  false,
                                  false,
                                  request.id
                                )
                              }
                            >
                              리뷰 작성
                            </Button>
                          )
                        ) : (
                          !!request.review && (
                            <Button
                              className={style["button"]}
                              onClick={() =>
                                openReviewModal(
                                  request.review,
                                  false,
                                  true,
                                  request.id
                                )
                              }
                            >
                              리뷰 확인
                            </Button>
                          )
                        ))}
                      <Button
                        className={style["button"]}
                        onClick={() =>
                          navigate(`/quotationRequest/${request.id}`)
                        }
                      >
                        상세 정보
                      </Button>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          ))}
        </Grid2>

        {pageInfo.page + 1 < pageInfo.totalPage && (
          <Button
            onClick={() =>
              setPageInfo({ ...pageInfo, page: pageInfo.page + 1 })
            }
            disabled={loading}
            sx={{ margin: "16px 0", color: "#FA4D56" }}
          >
            {loading ? "Loading..." : "더보기"}
          </Button>
        )}
        <Snackbar
          anchorOrigin={{ vertical: "top", horizontal: "center" }}
          open={snackbarOpen}
          autoHideDuration={3000}
          onClose={handleSnackbarClose}
          message={snackbarMessage}
          sx={{ marginTop: "40px" }}
        >
          <Alert
            severity="success"
            variant="outlined"
            sx={{
              width: "100%",
              bgcolor: "background.paper",
            }}
          >
            {snackbarMessage}
          </Alert>
        </Snackbar>
        <Box sx={{ height: "32px" }} />
      </div>

      <Backdrop className={style["back-drop"]} open={reviewModal.open}>
        <div className={style["review-modal"]}>
          <div className={style["review-info"]}>
            {reviewModal.readOnly
              ? "리뷰 확인"
              : reviewModal.isEdit
              ? "리뷰 수정"
              : "리뷰 작성"}
          </div>
          <div className={style["rate-div"]}>
            <Rating
              className={style["rating"]}
              name="review-rate"
              value={reviewModal.rate}
              onChange={(event, newValue) =>
                setReviewModal({
                  ...reviewModal,
                  rate: newValue,
                })
              }
              onChangeActive={(event, newHover) => {
                setReviewHoverRate(newHover);
              }}
              readOnly={reviewModal.readOnly}
              precision={0.5}
            />
            <div className={style["rate-text"]}>
              {reviewHoverRate === -1 ? reviewModal.rate : reviewHoverRate}
            </div>
          </div>
          <TextField
            className={style["text-field-detail"]}
            value={reviewModal.title}
            onChange={(e) => {
              setReviewModal({
                ...reviewModal,
                title: e.target.value,
              });
            }}
            type="text"
            name="title"
            placeholder=""
            variant="outlined"
            label="제목"
            required={true}
            slotProps={{
              inputLabel: {
                shrink: true,
              },
              input: {
                readOnly: reviewModal.readOnly,
              },
            }}
          />
          <TextField
            className={style["text-field-detail"]}
            value={reviewModal.description}
            onChange={(e) => {
              setReviewModal({
                ...reviewModal,
                description: e.target.value,
              });
            }}
            multiline
            minRows={6}
            name="description"
            placeholder=""
            variant="outlined"
            label="리뷰 내용"
            required={true}
            slotProps={{
              inputLabel: {
                shrink: true,
              },
              input: {
                readOnly: reviewModal.readOnly,
              },
            }}
          />
          <div className={style["button-div"]}>
            <Button
              className={style["cancel-button"]}
              onClick={() =>
                setReviewModal({
                  ...reviewModal,
                  open: false,
                })
              }
            >
              취소
            </Button>
            {!reviewModal.readOnly && (
              <Button
                className={style["save-button"]}
                variant="contained"
                onClick={() => saveEditReview(reviewModal.quotationRequestId)}
              >
                저장
              </Button>
            )}
          </div>
        </div>
      </Backdrop>
    </main>
  );
};

export default QuotationRequestListComponent;
