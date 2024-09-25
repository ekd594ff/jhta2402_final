import React, { useEffect, useState } from "react";
import axios from "axios";
import { Avatar, Button, Snackbar, Alert, Box, Backdrop } from "@mui/material";
import style from "../../styles/quotationRequest-list.module.scss";
import { useNavigate } from "react-router-dom";
import { CheckCircle, Pending } from "@mui/icons-material";
import { dateFormatter } from "../../utils/dateUtil.jsx";
import TextField from "@mui/material/TextField";
import Rating from "@mui/material/Rating";

import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import ListAltIcon from "@mui/icons-material/ListAlt";

const QuotationRequestListComponent = ({ expand }) => {
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

  useEffect(() => {
    if (reviewModal.open) {
      document.body.classList.add("modal");
    } else {
      document.body.classList.remove("modal");
    }
  }, [reviewModal.open]);

  return (
    <div className={`${style["main"]} ${expand ? style["expand"] : ""}`}>
      <div className={style["container"]}>
        <div className={style["title"]}>
          {path.endsWith("company")
            ? "회사 견적신청서 목록"
            : "견적신청서 목록"}
        </div>
        <div className={style["qr-grid-container"]}>
          <Button
            variant="outlined"
            className={`${
              style[pageInfo.progress === "PENDING" ? "focus-button" : "button"]
            }`}
            onClick={() => setProgress("PENDING")}
            startIcon={<Pending />}
          >
            진행
          </Button>
          <Button
            variant="outlined"
            className={`${
              style[
                pageInfo.progress === "APPROVED" ? "focus-button" : "button"
              ]
            } ${
              pageInfo.progress === "APPROVED" ? style["approved-selected"] : ""
            }`}
            onClick={() => setProgress("APPROVED")}
            startIcon={<CheckCircleIcon />}
          >
            완료
          </Button>
          <Button
            variant="outlined"
            className={`${
              style[pageInfo.progress === "ALL" ? "focus-button" : "button"]
            } ${pageInfo.progress === "ALL" ? style["all-selected"] : ""}`}
            onClick={() => setProgress("ALL")}
            startIcon={<ListAltIcon />}
          >
            전체
          </Button>
        </div>
        <div className={style["card-container"]}>
          {quotationRequests.length === 0 && (
            <div className={style["no-content-div"]}>
              해당 조건의 견적신청서가 없습니다.
            </div>
          )}
          {quotationRequests.map((request, index) => (
            <div key={`${index}_${request.id}`} className={style["card"]}>
              <div className={style["top"]}>
                <div className={style["left"]}>
                  <Avatar
                    className={style["avatar"]}
                    src={request.portfolio.url}
                    variant="rounded"
                  />
                </div>
                <div className={style["right"]}>
                  <div className={style["content"]}>
                    <div
                      className={`${style["top"]} ${style[request.progress]}`}
                    >
                      <span>{dateFormatter(request.updatedAt)}</span>
                      {progressIcon(request.progress)}
                    </div>
                    <div className={style["middle"]}>
                      <div className={style["title"]}>{request.title}</div>
                      <div className={`${style["status"]}`}></div>
                    </div>
                    <div className={style["bottom"]}>{request.description}</div>
                  </div>
                </div>
              </div>
              <div className={style["bottom"]}>
                {request.progress === "PENDING" && (
                  <Button
                    className={style["button"]}
                    onClick={() => updateProgress(request.id)}
                    variant="outlined"
                    size="small"
                    color="error"
                  >
                    거래 취소
                  </Button>
                )}
                {request.progress === "APPROVED" &&
                  (path.endsWith("member") ? (
                    !!request.review.id ? (
                      <Button
                        className={style["button"]}
                        variant="outlined"
                        color="secondary"
                        size="small"
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
                        variant="outlined"
                        color="success"
                        size="small"
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
                  onClick={() => navigate(`/quotationRequest/${request.id}`)}
                  variant="outlined"
                  color="info"
                  size="small"
                >
                  상세 정보
                </Button>
              </div>
            </div>
          ))}
        </div>
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
      <Backdrop
        className={style["back-drop"]}
        open={reviewModal.open}
        onClick={(event) => {
          setReviewModal((prev) => ({ ...prev, open: false }));
        }}
      >
        <div
          className={style["review-modal"]}
          onClick={(event) => {
            event.stopPropagation();
          }}
        >
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
              size="small"
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
            size="small"
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
            size="small"
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
              size="small"
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
                size="small"
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
    </div>
  );
};

export default QuotationRequestListComponent;
