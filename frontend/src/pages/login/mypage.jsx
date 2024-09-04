import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Container, Typography, TextField, Button, Grid, Snackbar } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const Mypage = () => {
    const [userData, setUserData] = useState({
        username: '',
        email: '',
        password: '',
    });
    const [selectedFile, setSelectedFile] = useState(null);
    const [message, setMessage] = useState('');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('/api/member/email');
                console.log(response); // 이메일로 사용자 정보 가져오기
                setUserData({
                    email: response.data.email,
                    username: response.data.username,
                    password: '', // 비밀번호는 초기화
                });
            } catch (error) {
                console.error('사용자 정보를 가져오는 중 오류 발생:', error);
                setMessage('정보를 가져오는 데 실패했습니다.');
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
            formData.append('username', userData.username);
            formData.append('password', userData.password);

            // 이미지 업로드 요청
            if (selectedFile) {
                formData.append('file', selectedFile);
            }

            // 프로필 수정 요청
            await axios.patch('/api/member/profile', formData, {
                header: {
                    'Content-Type' : 'multipart/form-data',
                },
            });

            setMessage('정보가 성공적으로 수정되었습니다.');
            setOpenSnackbar(true);
        } catch (error) {
            console.error('수정 중 오류 발생:', error);
            setMessage('수정에 실패했습니다.');
            setOpenSnackbar(true);
        }
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    const handleGoIndex = () => {
        navigate('/');
    };

    return (
        <Container maxWidth="sm" style={{ marginTop: '20px' }}>
            <Typography variant="h4" gutterBottom>
                내 프로필 수정
            </Typography>
            <Typography variant="h6">
                이름: {userData.username}
            </Typography>
            <Typography variant="h6" style={{ marginBottom: '30px' }}>
                이메일: {userData.email}
            </Typography>
            <form onSubmit={handleSubmit}>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <TextField
                            fullWidth
                            label="새로운 이름은 2 ~ 17자 내로 작성"
                            name="username"
                            value={userData.username}
                            onChange={handleChange}
                            required
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            fullWidth
                            label="비밀번호는 영문 숫자조합 8 ~ 25자 내로 작성"
                            name="password"
                            type="password"
                            value={userData.password}
                            onChange={handleChange}
                            required
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handleFileChange}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <Grid container spacing={2} justifyContent="space-between">
                            <Grid item>
                                <Button variant="contained" color="secondary" type="submit">
                                    수정하기
                                </Button>
                            </Grid>
                            <Grid item>
                                <Button variant="contained" color="secondary" onClick={handleGoIndex}>
                                    Home
                                </Button>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </form>
            <Snackbar
                open={openSnackbar}
                onClose={handleCloseSnackbar}
                message={message}
                autoHideDuration={6000}
            />
        </Container>
    );
};

export default Mypage;