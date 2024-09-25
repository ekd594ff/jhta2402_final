import React, { useEffect, useState } from "react";
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import style from "../../styles/company-detail.module.scss";
import axios from "axios";
import { checkSeller } from "../../utils/loginUtil.jsx";
import { useNavigate, useParams } from "react-router-dom";
import Paper from "@mui/material/Paper";
import {
  Alert,
  Box,
  Card,
  CardContent,
  ImageList,
  ImageListItem,
  ImageListItemBar,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography,
} from "@mui/material";
import Button from "@mui/material/Button";
import ReviewList from "./reviewList.jsx";

function CompanyDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [companyInfo, setCompanyInfo] = useState({
    imageUrl: "",
    companyName: "",
    description: "",
    phone: "",
    address: "",
    applied: true,
    requestPendingCount: 0,
    requestApprovedCount: 0,
  });

  const [portfolioList, setPortfolioList] = useState([]);

  const [pageInfo, setPageInfo] = useState({
    page: 0,
    size: 4,
    totalPage: 0,
  });

  const [open, setOpen] = useState(false);

  // member api
  const getCompanyById = async () =>
    await axios.get(`/api/company/info/${id}`, { withCredentials: true });

  // company api
  const getCompanyInfo = async () =>
    await axios.get("/api/company/info", { withCredentials: true });
  const getRequestCount = async () =>
    await axios.get("/api/quotationRequest/company/count", {
      withCredentials: true,
    });

  const apis = !!id
    ? [getCompanyById()]
    : [getCompanyInfo(), getRequestCount(), checkSeller()];

  useEffect(() => {
    Promise.all(apis).then(([res, countRes, _]) => {
      if (res.data.deleted) {
        alert("삭제된 회사입니다.");
        navigate(-1);
      }

      if (!id) {
        setCompanyInfo({
          companyId: res.data.companyId,
          imageUrl: res.data.url,
          companyName: res.data.companyName,
          description: res.data.description,
          phone: res.data.phone,
          address: `${res.data.address} ${res.data.detailAddress}`,
          applied: res.data.applied,
          requestPendingCount: countRes.data.pendingCount,
          requestApprovedCount: countRes.data.approvedCount,
        });
      } else {
        setCompanyInfo({
          companyId: res.data.companyId,
          imageUrl: res.data.url,
          companyName: res.data.companyName,
          description: res.data.description,
          phone: res.data.phone,
          address: `${res.data.address} ${res.data.detailAddress}`,
          applied: res.data.applied,
        });
      }
    });
    window.scrollTo(0, 0);

    setTimeout(() => {
      setOpen(false);
    }, 3500);
  }, []);

  const portfolioUrl = !!id
    ? `/api/portfolio/list/company/${id}?page=${pageInfo.page}&size=${pageInfo.size}`
    : `/api/portfolio/list/company?page=${pageInfo.page}&size=${pageInfo.size}`;

  useEffect(() => {
    axios
      .get(portfolioUrl, { withCredentials: true })
      .then((res) => {
        setPortfolioList([...portfolioList, ...res.data.content]);
        setPageInfo({ ...pageInfo, totalPage: res.data.page.totalPages });
      })
      .catch(() => {
        alert("포트폴리오를 불러오는데 문제가 발생했습니다.");
        navigate(-1);
      });
  }, [pageInfo.page]);

  const createData = (name, value) => {
    return { name, value };
  };

  const rows = [
    createData("회사명", companyInfo.companyName),
    createData("전화번호", companyInfo.phone),
    createData("주소", companyInfo.address),
    createData("회사 설명", companyInfo.description),
  ];

  const visibleAlert = (portfolio) => {
    const confirmMessage = portfolio.activated
      ? "해당 포트폴리오를 숨김처리 하시겠습니까?"
      : "해당 포트폴리오를 공개처리 하시겠습니까?";

    if (!confirm(confirmMessage)) return;

    axios
      .patch(
        "/api/portfolio/seller/activate",
        {
          portfolioId: portfolio.id,
          isActivated: portfolio.activated,
        },
        { withCredentials: true }
      )
      .then(() => {
        setPortfolioList((prevState) =>
          prevState.map((p) =>
            p.id === portfolio.id
              ? { ...p, activated: !portfolio.activated }
              : p
          )
        );
        alert("수정되었습니다.");
      })
      .catch(() => alert("문제가 발생했습니다. 다시 시도해주세요."));
  };

  // todo : 삭제 시 추가 절차
  const deleteAlert = (portfolio) => {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    axios
      .patch(`/api/portfolio/seller/delete/${portfolio.id}`, {
        withCredentials: true,
      })
      .then(() => {
        setPortfolioList((prevState) =>
          prevState.filter((p) => p.id !== portfolio.id)
        );

        alert("삭제되었습니다.");
      })
      .catch(() => alert("문제가 발생했습니다. 다시 시도해주세요."));
  };

  useEffect(() => {}, []);

  return (
    <>
      <Header />
      <main className={style["main"]}>
        <div className={style["container"]}>
          <h2 className={style["sub-title"]}>회사 정보</h2>
          {!companyInfo.applied && open && (
            <Alert className={style["alert"]} severity="info">
              관리자가 회사 승인을 검토하고 있습니다.
            </Alert>
          )}
          <div className={style["company-info-div"]}>
            <img className={style["company-img"]} src={companyInfo.imageUrl} />
            <TableContainer
              component={Paper}
              className={style["company-table"]}
            >
              <Table
                sx={{ minWidth: 240 }}
                aria-label="simple table"
                className={style["table"]}
              >
                <TableBody className={style["table-body"]}>
                  {rows.map((row) => (
                    <TableRow
                      key={row.name}
                      sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                      className={style["table-row"]}
                    >
                      <TableCell align="left" className={style["table-name"]}>
                        {row.name}
                      </TableCell>
                      <TableCell align="left">{row.value}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </div>
          {!id && (
            <div className={style["button-div"]}>
              <Button
                variant="text"
                className={style["button"]}
                onClick={() => navigate("/company/edit")}
              >
                수정하기
              </Button>
            </div>
          )}

          {!id && (
            <>
              <h2 className={style["sub-title"]}>견적신청서</h2>
              <Card className={style["request-card"]}>
                <CardContent className={style["card-content"]}>
                  <div className={style["card-div"]}>
                    <div className={style["title"]}>진행중인 인테리어</div>
                    <div className={style["count"]}>
                      {companyInfo.requestPendingCount}
                    </div>
                  </div>
                  <div className={style["card-div"]}>
                    <div className={style["title"]}>완료된 인테리어</div>
                    <div className={style["count"]}>
                      {companyInfo.requestApprovedCount}
                    </div>
                  </div>
                </CardContent>
              </Card>
              <div className={style["button-div"]}>
                <Button
                  variant="text"
                  className={style["button"]}
                  onClick={() => navigate("/quotationRequest/company")}
                >
                  더보기
                </Button>
              </div>
            </>
          )}

          <h2 className={style["sub-title"]}>
            {!id ? "포트폴리오 관리" : "포트폴리오 목록"}
          </h2>
          {!id && (
            <div className={style["button-top-div"]}>
              <Button
                variant="text"
                className={style["button"]}
                onClick={() => navigate("/portfolio/registration")}
              >
                포트폴리오 생성
              </Button>
            </div>
          )}
          <ImageList>
            {portfolioList.map((portfolio) => (
              <ImageListItem
                sx={{ margin: "0 8px", minWidth: "160px" }}
                key={portfolio.id}
              >
                <img src={portfolio.imageUrls[0]} alt="portfolio thumbnail" />
                <ImageListItemBar
                  title={
                    <Typography
                      sx={{ fontWeight: "500", cursor: "pointer" }}
                      onClick={() => navigate(`/portfolio/${portfolio.id}`)}
                    >
                      {portfolio.title}
                    </Typography>
                  }
                  subtitle={
                    !id && (
                      <Box
                        sx={{
                          display: "flex",
                          justifyContent: "space-between",
                          margin: "4px 0 12px 0",
                        }}
                      >
                        <Box>
                          <Button
                            variant="contained"
                            sx={{ backgroundColor: "#FA4D56" }}
                            onClick={() => deleteAlert(portfolio)}
                          >
                            삭제
                          </Button>
                          <Button
                            variant="outlined"
                            sx={{
                              borderColor: "#FA4D56",
                              color: "#FA4D56",
                              margin: "0 8px",
                            }}
                            onClick={() => visibleAlert(portfolio)}
                          >
                            {portfolio.activated ? "숨김" : "보이기"}
                          </Button>
                        </Box>
                        <Box sc={{ display: "flex", justifyContent: "end" }}>
                          <Button
                            variant="outlined"
                            sx={{ borderColor: "#FA4D56", color: "#FA4D56" }}
                            onClick={() =>
                              navigate(`/portfolio/edit/${portfolio.id}`)
                            }
                          >
                            수정
                          </Button>
                        </Box>
                      </Box>
                    )
                  }
                  position="below"
                />
              </ImageListItem>
            ))}
          </ImageList>
          {pageInfo.page + 1 < pageInfo.totalPage && (
            <Button
              onClick={() =>
                setPageInfo({ ...pageInfo, page: pageInfo.page + 1 })
              }
              variant="text"
              sx={{ borderColor: "#FA4D56", color: "#FA4D56", margin: "0 8px" }}
            >
              더보기
            </Button>
          )}
          {/* 리뷰 컴포넌트 추가 */}
          <h2 className={style["sub-title"]}>구매자 리뷰</h2>
          <ReviewList companyId={companyInfo.companyId} />
        </div>
      </main>
      <Footer />
    </>
  );
}

export default CompanyDetail;
