import React, { useEffect, useState } from "react";
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import axios from "axios";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";

import style from "../../styles/portfolio-detail.module.scss";
import {
  Alert,
  Backdrop,
  Chip,
  Collapse,
  FormControlLabel,
  Radio,
  RadioGroup,
  Tooltip,
} from "@mui/material";
import List from "@mui/material/List";
import PortfolioSolutionListItem from "./portfolio-solution-list-item.jsx";
import PortfolioReviewListItem from "./portfolio-review-list-item.jsx";

import { Swiper, SwiperSlide } from "swiper/react";
import IconButton from "@mui/material/IconButton";

import Divider from "@mui/material/Divider";
import Button from "@mui/material/Button";
import StarIcon from "@mui/icons-material/Star";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import NotificationsIcon from "@mui/icons-material/Notifications";
import ShareIcon from "@mui/icons-material/Share";

import "swiper/css";
import "swiper/css/pagination";
import PortfolioImgListItem from "../../components/portfolio/portfolio-img-list-item.jsx";
import TextField from "@mui/material/TextField";

const solutionAJAXPromise = (portfolioId) =>
  axios.get(`/api/solution/portfolio/${portfolioId}`);
const reviewListAJAXPromise = (portfolioId, pagination) =>
  axios.get(
    `/api/review/portfolio/${portfolioId}?${Object.entries(pagination)
      .map(
        ([key, value]) =>
          `${encodeURIComponent(key)}=${encodeURIComponent(value)}`
      )
      .join("&")}`
  );
const memberInfoAJAXPromise = axios.get("/api/member/email");

function PortfolioReportRadioList() {
  return (
    <>
      <FormControlLabel
        value="부적절한 내용"
        control={<Radio />}
        label="부적절한 내용"
      />
      <FormControlLabel
        value="사진과 다른 서비스"
        control={<Radio />}
        label="사진과 다른 서비스"
      />
      <FormControlLabel
        value="불친절한 서비스"
        control={<Radio />}
        label="불친절한 서비스"
      />
      <FormControlLabel
        value="저작권 불법 도용"
        control={<Radio />}
        label="저작권 불법 도용"
      />
      <FormControlLabel
        value="기타(직접작성)"
        control={<Radio />}
        label="기타(직접작성)"
      />
    </>
  );
}

function ReviewReportRadioList() {
  return (
    <>
      <FormControlLabel
        value="부적절한 리뷰"
        control={<Radio />}
        label="부적절한 리뷰"
      />
      <FormControlLabel
        value="허위 리뷰"
        control={<Radio />}
        label="허위 리뷰"
      />
      <FormControlLabel
        value="기타(직접작성)"
        control={<Radio />}
        label="기타(직접작성)"
      />
    </>
  );
}

function PortfolioDetail() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const [portfolioImgList, setPortfolioImgList] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [solutionList, setSolutionList] = useState([]);
  const [selectedSolutionList, setSelectedSolutionList] = useState([]);
  const [modalImg, setModalImg] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [reportModalOpen, setReportModalOpen] = useState(false);
  const [reportData, setReportData] = useState({
    title: "",
    description: "",
    refId: "",
  });
  const [reportModalMode, setReportModalMode] = useState("portfolio");
  const [customReportTitle, setCustomReportTitle] = useState("");
  const [disableCustomReport, setDisableCustomReport] = useState(true);
  const [alert, setAlert] = useState({
    open: false,
    severity: "success",
    msg: "",
  });
  const [memberId, setMemberId] = useState("");

  const [portfolioInfo, setPortfolioInfo] = useState({
    portfolioId: id,
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
    size: 5,
    sort: "rate",
    prevSort: "",
  });

  const [reviewHasMore, setReviewHasMore] = useState(null);

  function onLoadMoreReview() {
    reviewListAJAXPromise(id, {
      page: reviewInfo.page + 1,
      size: reviewInfo.size,
      sort: reviewInfo.sort,
    }).then((result) => {
      const {
        data: {
          content,
          page: { number, totalElements, totalPages },
        },
      } = result;
      setReviewInfo({
        ...reviewInfo,
        page: number,
        totalElements,
        totalPages,
        review: [...reviewInfo.review, ...content],
      });
    });
  }

  useEffect(() => {
    if (reviewInfo.review.length < reviewInfo.totalElements) {
      setReviewHasMore(true);
    } else {
      setReviewHasMore(false);
    }
  }, [reviewInfo]);

  useEffect(() => {
    const portFolioAJAXPromise = axios
      .get(`/api/portfolio/${id}`)
      .then((res) => {
        setPortfolioInfo({
          ...portfolioInfo,
          ...res.data,
        });

        setPortfolioImgList(res.data.imageUrls);
      })
      .catch(() => {
        alert("유효하지 않은 페이지입니다.");
        navigate(-1);
      });

    Promise.all([
      reviewListAJAXPromise(id, {
        page: reviewInfo.page,
        size: reviewInfo.size,
        sort: reviewInfo.sort,
      }),
      portFolioAJAXPromise,
      solutionAJAXPromise(id),
      memberInfoAJAXPromise,
    ])
      .then(
        ([
          reviewListResult,
          portfolioResult,
          solutionResult,
          memberInfoResult,
        ]) => {
          const {
            data: {
              content,
              page: { number, totalElements, totalPages },
            },
          } = reviewListResult;
          setReviewInfo({
            ...reviewInfo,
            page: number,
            totalElements,
            totalPages,
            review: content,
          });
          const { data } = solutionResult;
          setSolutionList(data);
          setSelectedSolutionList(
            Array.from({ length: data.length }, () => false)
          );
          const {
            data: { id: memberId },
          } = memberInfoResult;
          setMemberId(memberId);
        }
      )
      .finally(() => {
        setIsLoading(false);
      });
  }, [id]); // 의존성 배열 수정

  useEffect(() => {
    if (portfolioImgList.length) {
      setModalImg(portfolioImgList[0]);
    }
  }, [portfolioImgList]);

  useEffect(() => {
    if (modalOpen || reportModalOpen) {
      document.body.classList.add("modal");
    } else {
      document.body.classList.remove("modal");
    }
  }, [modalOpen, reportModalOpen]);

  useEffect(() => {
    window.scrollTo(0, 0);

    const alertOpen = searchParams.get("requestSuccess");
    if (alertOpen !== null) {
      if (alertOpen === "true") {
        // 솔루션 신청 성공
        setAlert((prev) => ({
          open: true,
          severity: "success",
          msg: "신청이 완료되었습니다",
        }));
      } else {
        setAlert((prev) => ({
          open: true,
          severity: "error",
          msg: "신청 실패하였습니다",
        }));
      }
      setTimeout(() => {
        setAlert((prev) => ({
          open: false,
          severity: "",
          msg: "",
        }));
      }, 3000);
    }
  }, []);

  useEffect(() => {
    setReportData((prev) => ({ ...prev, title: "", description: "" }));
  }, [reportModalMode, setReportData]);

  useEffect(() => {
    if (reviewInfo.prevSort && reviewInfo.sort !== reviewInfo.prevSort) {
      reviewListAJAXPromise(id, {
        page: 0,
        size: reviewInfo.size,
        sort: reviewInfo.sort,
      }).then((result) => {
        const {
          data: {
            content,
            page: { number, totalElements, totalPages },
          },
        } = result;
        setReviewInfo({
          ...reviewInfo,
          page: number,
          totalElements,
          totalPages,
          review: content,
        });
      });
    }
  }, [reviewInfo.sort, reviewInfo.prevSort]);

  return (
    <>
      <Header />
      <main className={style["portfolio-detail"]}>
        <div className={style["container"]}>
          <Collapse className={style["collapse"]} in={alert.open}>
            <Alert severity={alert.severity}>{alert.msg}</Alert>
          </Collapse>
          <div className={style["top"]}>
            <div className={style["left"]}>
              <div className={style["selected-img"]}>
                <div className={style["selected-img-wrapper"]}>
                  {portfolioImgList[0] ? (
                    <img
                      alt="selected-img"
                      src={portfolioImgList[0]}
                      onClick={() => {
                        setModalOpen((prev) => !prev);
                        setModalImg(portfolioImgList[0]);
                      }}
                    />
                  ) : (
                    <></>
                  )}
                </div>
              </div>
            </div>
            <div className={style["right"]}>
              <div className={style["info"]}>
                <div className={style["top"]}>
                  <span className={style["company-name"]}>
                    {portfolioInfo.companyName}
                  </span>
                  <span className={style["btn-group"]}>
                    <Tooltip title="신고하기">
                      <IconButton
                        size="small"
                        disableRipple
                        onClick={() => {
                          setReportModalMode("portfolio");
                          setReportData({
                            ...reportData,
                            refId: portfolioInfo.portfolioId,
                          });
                          setTimeout(() => {
                            setReportModalOpen(true);
                          }, 1);
                        }}
                      >
                        <NotificationsIcon />
                      </IconButton>
                    </Tooltip>
                    <Tooltip title="공유하기">
                      <IconButton size="small" disableRipple>
                        <ShareIcon />
                      </IconButton>
                    </Tooltip>
                  </span>
                </div>
                <div>
                  <span className={style["title"]}>{portfolioInfo.title}</span>
                </div>
                <div>
                  <pre className={style["description"]}>
                    {portfolioInfo.description}
                  </pre>
                </div>
              </div>
              <div className={style["solution-list-title"]}>
                인테리어 솔루션
              </div>
              {!isLoading ? (
                solutionList.length ? (
                  <>
                    <List className={style["portfolio-solution-list"]}>
                      {solutionList.map((item, index) => {
                        const render = [
                          <PortfolioSolutionListItem
                            key={`${item.id}_${index}`}
                            setter={setSelectedSolutionList}
                            {...item}
                            list={selectedSolutionList}
                            index={index}
                          />,
                        ];
                        if (index !== solutionList.length - 1) {
                          render.push(
                            <Divider
                              variant="middle"
                              key={`${item.id}_${index}_${index}`}
                            />
                          );
                        }
                        return render;
                      })}
                    </List>
                    <Button
                      onClick={() => {
                        navigate("/quotationRequest", {
                          state: {
                            list: solutionList,
                            selectedList: selectedSolutionList,
                            portfolioInfo: portfolioInfo,
                          },
                        });
                      }}
                      className={style["solution-list-submit"]}
                      size="large"
                      disableRipple
                    >
                      신청하기
                    </Button>
                  </>
                ) : (
                  <div className={style["empty"]}>
                    <span>등록된 솔루션이 없습니다</span>
                  </div>
                )
              ) : (
                <></>
              )}
            </div>
          </div>
          <div className={style["middle"]}>
            <div className={style["portfolio-list-title"]}>
              포트폴리오
              <span className={style["portfolio-count"]}>
                {portfolioImgList.length}
              </span>
            </div>
            <Swiper
              slidesPerView={4}
              spaceBetween={32}
              pagination={{
                clickable: true,
              }}
              breakpoints={{
                960: {
                  slidesPerView: 4,
                  spaceBetween: 8,
                },
                720: {
                  slidesPerView: 3,
                  spaceBetween: 6,
                },
                540: {
                  slidesPerView: 2,
                  spaceBetween: 4,
                },
                320: {
                  slidesPerView: 1,
                  spaceBetween: 2,
                },
              }}
              className={style["portfolio-swiper"]}
            >
              {portfolioImgList.map((value) => (
                <SwiperSlide key={value}>
                  <PortfolioImgListItem
                    value={value}
                    setter={setModalImg}
                    modalOpener={setModalOpen}
                  />
                </SwiperSlide>
              ))}
            </Swiper>
          </div>
          <div className={style["bottom"]}>
            <div className={style["review-list-top"]}>
              <div className={style["review-list-title"]}>
                리뷰
                <span>{reviewInfo.totalElements || ""}</span>
              </div>
              <div className={style["order"]}>
                <Button
                  className={reviewInfo.sort === "rate" ? style["active"] : ""}
                  size="small"
                  name="rate"
                  disableRipple
                  startIcon={<StarIcon />}
                  onClick={() => {
                    setReviewInfo((prev) => ({
                      ...prev,
                      sort: "rate",
                      prevSort: prev.sort,
                    }));
                  }}
                >
                  추천순
                </Button>
                <Button
                  className={
                    reviewInfo.sort === "createdAt" ? style["active"] : ""
                  }
                  size="small"
                  name="createdAt"
                  disableRipple
                  startIcon={<AccessTimeIcon />}
                  onClick={() => {
                    setReviewInfo((prev) => ({
                      ...prev,
                      sort: "createdAt",
                      prevSort: prev.sort,
                    }));
                  }}
                >
                  최신순
                </Button>
              </div>
            </div>
            <ul className={style["review-list"]}>
              {!isLoading ? (
                reviewInfo.review.length ? (
                  reviewInfo.review.map((item, index) => {
                    const render = [
                      <PortfolioReviewListItem
                        onReport={() => {
                          setReportModalMode("review");
                          setReportData({ ...reportData, refId: item.id });
                          setTimeout(() => {
                            setReportModalOpen((prev) => !prev);
                          }, 1);
                        }}
                        {...item}
                        key={`${index}_${index}`}
                      />,
                    ];
                    if (index !== reviewInfo.review.length - 1) {
                      render.push(
                        <Divider
                          variant="middle"
                          key={`${item.id}_${index}_${index}_dvd}`}
                        />
                      );
                    }
                    return render;
                  })
                ) : (
                  <div className={style["empty"]}>
                    <span>등록된 리뷰가 없습니다</span>
                  </div>
                )
              ) : (
                <></>
              )}
              {reviewHasMore !== null && reviewHasMore ? (
                <Divider>
                  <Chip
                    className={style["more"]}
                    label="더보기"
                    size="small"
                    variant="outlined"
                    onClick={onLoadMoreReview}
                  />
                </Divider>
              ) : (
                <></>
              )}
            </ul>
          </div>
        </div>
      </main>
      <Footer />
      <Backdrop
        className={style["modal"]}
        open={modalOpen}
        onClick={() => {
          setModalOpen(false);
        }}
      >
        <img alt="modal image" src={modalImg} />
      </Backdrop>
      <Backdrop
        className={style["report-modal"]}
        open={reportModalOpen}
        onClick={(event) => {
          setReportModalOpen(false);
        }}
      >
        <div className={style["content"]}>
          <div className={style["top"]}>신고하기</div>
          <div
            className={style["middle"]}
            onClick={(event) => {
              event.stopPropagation();
            }}
          >
            <RadioGroup
              onChange={(event) => {
                const title = event.target.value;
                if (title === "기타(직접작성)") {
                  setDisableCustomReport(false);
                  setReportData({
                    ...reportData,
                    description: reportData.description,
                    title: customReportTitle,
                  });
                } else {
                  setDisableCustomReport(true);
                  setReportData({
                    ...reportData,
                    description: reportData.description,
                    title,
                  });
                }
              }}
              className={style["report-list"]}
            >
              {reportModalMode === "portfolio" ? (
                <PortfolioReportRadioList />
              ) : (
                <ReviewReportRadioList />
              )}
              <TextField
                variant="outlined"
                disabled={disableCustomReport}
                value={customReportTitle}
                onChange={(event) => {
                  const title = event.target.value;
                  setCustomReportTitle(title);
                  setReportData({ ...reportData, title });
                }}
              />
              <label className={style["report-description"]}>
                상세설명
                <TextField
                  multiline
                  maxRows={4}
                  variant="outlined"
                  value={reportData.description}
                  onChange={(event) => {
                    const value = event.target.value;
                    setReportData({ ...reportData, description: value });
                  }}
                />
              </label>
              <Button
                className={style["report-submit"]}
                size="medium"
                onClick={() => {
                  if (!memberId) {
                    window.alert("로그인이 필요합니다");
                  }
                  const data = {
                    ...reportData,
                    memberId,
                  };
                  axios
                    .post(`/api/report/create/${reportModalMode}`, data)
                    .then((result) => {
                      window.alert("신고가 접수되었습니다");
                      setReportModalOpen(false);
                    })
                    .catch((err) => {
                      window.alert("신고 접수 실패");
                    });
                }}
                disableRipple
              >
                신고 제출
              </Button>
            </RadioGroup>
          </div>
        </div>
      </Backdrop>
    </>
  );
}

export default PortfolioDetail;
