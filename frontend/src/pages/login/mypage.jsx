import React, { useEffect, useState } from "react";
import axios from "axios";
import { Container, Typography, TextField, Button, Box, Snackbar, Grid2, Alert, Card, CardContent, Divider } from "@mui/material";
import { useNavigate, Route, Routes } from "react-router-dom";
import Header from "../../components/common/header";
import Footer from "../../components/common/footer";
import Sidebar from "../login/mypage-sidebar";
import style from "../../styles/quotationRequest-list.module.scss";
import {CheckCircle, Image, Pending, Person} from "@mui/icons-material";
import ReportUserList from "../reportUserList";
import ReviewUserList from "../reviewUserList";

const MyPage = () => {
    const [selectedComponent, setSelectedComponent] = useState("profile");
    const [userData, setUserData] = useState(null);
    const navigator = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get("/api/member/email");
                setUserData(response.data);
                //console.info(response.data);
            } catch (error) {
                console.error("사용자 정보를 가져오는 중 오류 발생:", error);
            }
        };

        fetchUserData();
        //console.info(userData);
    }, []);

    useEffect(() => {
        console.log(userData);
    }, [userData]);

    const handleSelectProfile = () => {
        //setSelectedComponent("profile");
        navigator("profile");
    };

    const handleSelectQuotationRequests = () => {
        //setSelectedComponent("quotationRequest/member");
        navigator("quotationRequest/member");
    };

    const handleSelectReportUserList = () => {
        //setSelectedComponent("reportUserList");
        if (userData && userData.id) {
            navigator(`reportUserList/${userData.id}`);
        } else {
            console.error("userData가 로드되지 않았습니다.");
        }
    };

    const handleSelectReviewUserList = () => {
        navigator("reviewList");
    };

    return (
        <>
            <Header />
            <Box display="flex">
                <Box>
                    <Sidebar 
                        onSelectProfile={handleSelectProfile} 
                        onSelectQuotationRequests={handleSelectQuotationRequests} 
                        onSelectReportUserList={handleSelectReportUserList}
                        onSelectReviewUserList={handleSelectReviewUserList}
                    />
                </Box>
                <Box flexGrow={1} paddingTop="20px">
                    <Routes>
                        <Route path="/profile" element={<MyProfile />} />
                        <Route path="/quotationRequest/member" element={<QuotationRequestUserList />} />
                        <Route path="/reportUserList/:memberId" element={userData ? <ReportUserList memberId={userData.id} /> : <div>Loading...</div>} />
                        <Route path="/reviewList" element={<ReviewUserList />} />               
                    </Routes>
                </Box>
            </Box>
            <Footer />
        </>
    );
};

export function MyProfile() {
    const [userData, setUserData] = useState({
        username: "",
        email: "",
        password: "",
        profileImage: "",
    });
    const [selectedFile, setSelectedFile] = useState(null);
    const [message, setMessage] = useState("");
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const navigate = useNavigate();
    const [userImage, setUserImage] = useState("");
    const [username, setUsername] = useState("");

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get("/api/member/email");
                setUserData({
                    email: response.data.email,
                    username: response.data.username,
                    password: "",
                    profileImage: response.data.profileImage || "",
                });
            } catch (error) {
                console.error("사용자 정보를 가져오는 중 오류 발생:", error);
                setMessage("정보를 가져오는 데 실패했습니다.");
                setOpenSnackbar(true);
            }
        };

        fetchUserData();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData({
            ...userData,
            [name]: value,
        });
    };

    const handleFileChange = (e) => {
        setSelectedFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const formData = new FormData();
            formData.append("username", userData.username);
            formData.append("password", userData.password);

            // 이미지 업로드 요청
            if (selectedFile) {
                formData.append("file", selectedFile);
            }

            // 프로필 수정 요청
            await axios.patch("/api/member/profile", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });

            // 사용자 이미지와 사용자 이름 업데이트
            if (selectedFile) {
                const imageUrl = URL.createObjectURL(selectedFile); // 임시 URL 생성
                setUserImage(imageUrl);
            }
            setUsername(userData.username);

            setMessage("정보가 성공적으로 수정되었습니다.");
            setOpenSnackbar(true);
        } catch (error) {
            console.error("수정 중 오류 발생:", error);
            setMessage("수정에 실패했습니다.");
            setOpenSnackbar(true);
        }
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <Container maxWidth="sm" style={{ marginTop: "80px", marginBottom: "80px" }}>
            <Typography variant="h4" gutterBottom>
                내 프로필 수정
            </Typography>
            <Divider style={{marginBottom: "10px" }}/>
            <Typography variant="h6" sx={{ color: 'gray'}}>이름: {userData.username}</Typography>
            <Typography variant="h6" sx={{ color: 'gray' ,marginBottom: "30px" }}>
                이메일: {userData.email}
            </Typography>
            <form onSubmit={handleSubmit}>
                <Box mb={2}>
                    <TextField
                        fullWidth
                        label="새로운 이름은 2 ~ 17자 내로 작성"
                        name="username"
                        value={userData.username}
                        onChange={handleChange}
                        required
                    />
                </Box>
                <Box mb={2}>
                    <TextField
                        fullWidth
                        label="비밀번호는 영문 숫자조합 8 ~ 25자 내로 작성"
                        name="password"
                        type="password"
                        value={userData.password}
                        onChange={handleChange}
                        required
                    />
                </Box>
                <Box mb={2}>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleFileChange}
                    />
                </Box>
                <Box display="flex" justifyContent="space-between">
                    <Button variant="contained" color="primary" type="submit">
                        수정하기
                    </Button>
                </Box>
            </form>
            <Snackbar
                open={openSnackbar}
                onClose={handleCloseSnackbar}
                message={message}
                autoHideDuration={6000}
            />
        </Container>
    );
}

export function QuotationRequestUserList() {
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

    useEffect(() => {
        const fetchQuotationRequests = async () => {
            setLoading(true);
            setError(null);

            let url;
            if (path.endsWith("member")) {
                url = `/api/quotationRequest/list?progress=${pageInfo.progress}&page=${pageInfo.page}&pageSize=${pageInfo.size}`;
            } else if (path.endsWith("company")) {
                url = `/api/quotationRequest/companyList?progress=${pageInfo.progress}&page=${pageInfo.page}&pageSize=${pageInfo.size}`;
            }

            try {
                const response = await axios.get(url);
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
        if (!confirm("해당 거래를 취소하시겠습니까?")) return;

        try {
            await axios.put(`/api/quotationRequest/sellerCancel/${id}`);
            setSnackbarMessage("진행 상태가 업데이트되었습니다.");
            setSnackbarOpen(true);
            setPageInfo({...pageInfo, progress: "ALL", page: 0})
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
            return <Pending/>;
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
                        {quotationRequests.length === 0 &&
                            <div className={style['no-content-div']}>해당 조건의 견적신청서가 없습니다.</div>}
                        {quotationRequests.map(request =>
                            <Grid2 key={request.id} size={6} className={style['qr-card-grid']}>
                                <Card variant="outlined" className={style['qr-card']}>
                                    <CardContent className={style['qr-card-content']}>
                                        <div className={style['image-div']}>
                                            <div className={style['portfolio-div']}>
                                                {request.portfolio.url
                                                    ? <Box component="img" src={request.portfolio.url}
                                                           sx={{
                                                               width: "64px", height: "'64px",
                                                               objectFit: "cover", borderRadius: "50%"
                                                           }}/>
                                                    : <Image/>
                                                }
                                                <Typography>
                                                    {request.portfolio.title}
                                                </Typography>
                                            </div>
                                            <Typography variant="subtitle1" className={style[request.progress]}>
                                                {progressIcon(request.progress)}
                                            </Typography>
                                        </div>
                                        {path.endsWith("company") &&
                                            <div className={style['member-div']}>
                                                <div className={style['member-title-div']}>
                                                    <Typography className={style['title']}
                                                                variant="h6"
                                                                sx={{fontSize: "18px"}}>{request.title}</Typography>
                                                    <div className={style['member-info']}>
                                                        {(request.member.memberUrl)
                                                            ? <Avatar alt="member profile"
                                                                      sx={{height: "32px", width: "32px"}}
                                                                      src={request.member.memberUrl}/>
                                                            : <Avatar alt="member profile"
                                                                      sx={{height: "32px", width: "32px"}}>
                                                                <Person/>
                                                            </Avatar>}
                                                        <div className={style['member-username']}>
                                                            {request.member.username}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div className={style['member-content-div']}>
                                                    <Typography variant="body2">
                                                        {request.description}
                                                    </Typography>
                                                </div>
                                            </div>
                                        }
                                        <div className={style['info-div']}>
                                        </div>
                                        <div className={style['bottom-div']}>
                                            <div className={style['date-div']}>
                                                {request.updatedAt}
                                            </div>
                                            <div className={style['button-div']}>
                                                {request.progress === "PENDING" &&
                                                    <Button className={style['button']}
                                                            onClick={() => updateProgress(request.id)}>
                                                        거래 취소
                                                    </Button>}
                                                <Button className={style['button']}
                                                        onClick={() => navigate(`/quotationRequest/${request.id}`)}>
                                                    상세 정보
                                                </Button>
                                            </div>
                                        </div>
                                    </CardContent>
                                </Card>
                            </Grid2>
                        )}
                    </Grid2>

                    {(pageInfo.page + 1 < pageInfo.totalPage) && (
                        <Button onClick={() => setPageInfo({...pageInfo, page: pageInfo.page + 1})}
                                disabled={loading}>
                            {loading ? "Loading..." : "더보기"}
                        </Button>
                    )}
                    <Snackbar
                        anchorOrigin={{vertical: "top", horizontal: "center"}}
                        open={snackbarOpen}
                        autoHideDuration={3000}
                        onClose={handleSnackbarClose}
                        message={snackbarMessage}
                        sx={{marginTop: "40px"}}>
                        <Alert
                            severity="success"
                            variant="outlined"
                            sx={{
                                width: '100%',
                                bgcolor: 'background.paper',
                            }}>
                            {snackbarMessage}
                        </Alert>
                    </Snackbar>
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default MyPage;
