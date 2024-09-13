import React, {useEffect, useState} from "react";
import axios from "axios";
import {
    Typography,
    Button,
    Snackbar, Grid2, Card, Box, CardContent,
} from "@mui/material";
import style from "../../styles/quotationRequest-list.module.scss";
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import {useNavigate} from "react-router-dom";
import {CheckCircle, HourglassEmpty} from "@mui/icons-material";

const QuotationRequestList = () => {

    const navigate = useNavigate();

    const [quotationRequests, setQuotationRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [pageInfo, setPageInfo] = useState({
        progress: "PENDING",
        page: 0,
        totalPage: 0,
        size: 6,
    });
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");

    useEffect(() => {
        const fetchQuotationRequests = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await axios.get(
                    `/api/quotationRequest/companyList?progress=${pageInfo.progress}&page=${pageInfo.page}&pageSize=${pageInfo.size}`
                );

                console.log(response.data);
                (pageInfo.page === 0)
                    ? setQuotationRequests(response.data.content)
                    : setQuotationRequests((prevRequests) => [
                        ...prevRequests,
                        ...response.data.content,
                    ]);
                setPageInfo({...pageInfo, totalPage: response.data.page.totalPages});

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
        try {
            await axios.put(`/api/quotationRequest/sellerCancel/${id}`);
            setSnackbarMessage("진행 상태가 업데이트되었습니다.");
            setSnackbarOpen(true);
            // 요청 목록을 다시 불러와서 최신 상태로 업데이트
            setQuotationRequests((prevRequests) =>
                prevRequests.filter((request) => request.id !== id)
            );
        } catch (err) {
            console.error(err);
            setSnackbarMessage("진행 상태 업데이트 실패.");
            setSnackbarOpen(true);
        }
    };

    const setProgress = (progress) => {
        if (pageInfo.progress !== progress) {
            setPageInfo({...pageInfo, progress: progress, page: 0});
        }
    }

    const progressIcon = (progress) => {
        if (progress === "PENDING") {
            return <div className={style['text']}>진행 중<HourglassEmpty/></div>;
        } else if (progress === "APPROVED") {
            return <CheckCircle/>;
        } else if (progress === "USER_CANCELLED") {
            return "유저 취소";
        } else if (progress === "SELLER_CANCELLED") {
            return "판매자 취소";
        } else if (progress === "ADMIN_CANCELLED") {
            return "관리자 취소";
        } else {
            return "진행 상태";
        }
    }

    if (error) {
        alert("오류가 발생했습니다.");
        navigate(-1);
    }

    return (
        <>
            <Header/>
            <main className={style['main']}>
                <div className={style['container']}>
                    <Typography variant="h6" style={{textAlign: "center", margin: "24px 0"}}>
                        회사 견적신청서 목록
                    </Typography>
                    <Grid2 container spacing={2} className={style['qr-grid-container']}>
                        <Grid2 size={2} className={style['qr-grid']}>
                            <Button variant="outlined"
                                    className={style[(pageInfo.progress === "PENDING" ? "focus-button" : "button")]}
                                    onClick={() => setProgress("PENDING")}>
                                진행중인 목록
                            </Button>
                        </Grid2>
                        <Grid2 size={2} className={style['qr-grid']}>
                            <Button variant="outlined"
                                    className={style[(pageInfo.progress === "APPROVED" ? "focus-button" : "button")]}
                                    onClick={() => setProgress("APPROVED")}>
                                완료된 목록
                            </Button>
                        </Grid2>
                        <Grid2 size={2} className={style['qr-grid']}>
                            <Button variant="outlined"
                                    className={style[(pageInfo.progress === "ALL" ? "focus-button" : "button")]}
                                    onClick={() => setProgress("ALL")}>
                                전체 목록
                            </Button>
                        </Grid2>
                    </Grid2>
                    <Grid2 container spacing={2} className={style['qr-card-container']}>
                        {quotationRequests.map(request =>
                            <Grid2 key={request.id} size={6} className={style['qr-card-grid']}>
                                <Card variant="outlined" className={style['qr-card']}>
                                    <CardContent className={style['qr-card-content']}>
                                        <div className={style['image-div']}>
                                            <div className={style['member-div']}>
                                                <img className={style['image']}
                                                     src={request.member.memberUrl || "default"}/>
                                                <div>{request.member.username}</div>
                                            </div>
                                            <Typography variant="subtitle1" className={style[request.progress]}>
                                            {progressIcon(request.progress)}
                                            </Typography>
                                        </div>
                                        <div className={style['progress-div']}>
                                            <Typography variant="body1" className={style['updatedAt']}>
                                                {request.updatedAt}
                                            </Typography>
                                        </div>
                                        <div className={style['info-div']}>
                                            <Typography>{request.title}</Typography>
                                            <Typography>{request.description}</Typography>
                                            <Typography>{request.description}</Typography>
                                        </div>
                                        <div className={style['button-div']}>
                                            <Button className={style['button']}>
                                                거래 취소
                                            </Button>
                                            <Button className={style['button']}>
                                                상세 정보
                                            </Button>
                                        </div>
                                    </CardContent>
                                </Card>
                            </Grid2>
                        )}
                    </Grid2>

                    {/*<TableContainer component={Paper}>*/}
                    {/*    <Table aria-label="collapsible table">*/}
                    {/*        <TableHead>*/}
                    {/*            <TableRow>*/}
                    {/*                <TableCell/>*/}
                    {/*                <TableCell align="center">제목</TableCell>*/}
                    {/*                <TableCell align="center">내용</TableCell>*/}
                    {/*                <TableCell align="center">생성일자</TableCell>*/}
                    {/*                <TableCell align="center">수정일자</TableCell>*/}
                    {/*                <TableCell align="center">수정</TableCell>*/}
                    {/*            </TableRow>*/}
                    {/*        </TableHead>*/}
                    {/*        <TableBody>*/}
                    {/*            */}
                    {/*        </TableBody>*/}
                    {/*    </Table>*/}
                    {/*</TableContainer>*/}
                    {(pageInfo.page + 1 < pageInfo.totalPage) && (
                        <Button onClick={() => setPageInfo({...pageInfo, page: pageInfo.page + 1})}
                                disabled={loading}>
                            {loading ? "Loading..." : "더보기"}
                        </Button>
                    )}
                    <Snackbar
                        open={snackbarOpen}
                        autoHideDuration={3000}
                        onClose={handleSnackbarClose}
                        message={snackbarMessage}
                    />
                </div>
            </main>
            <Footer/>
        </>
    );
};

export default QuotationRequestList;

