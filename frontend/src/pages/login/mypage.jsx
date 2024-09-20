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
import QuotationRequestListComponent from "../quotationRequest/quotationRequestListComponent.jsx";
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
                        <Route path="/quotationRequest/member" element={<QuotationRequestListComponent />} />
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


export default MyPage;
