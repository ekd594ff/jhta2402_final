import React, { useEffect, useState } from "react";
import axios from "axios";
import { Container, Typography, List, ListItem, ListItemText, CircularProgress, Snackbar, Divider } from "@mui/material";

const ReportUserList = ({ memberId }) => {
    const [reports, setReports] = useState([]);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");
    const [openSnackbar, setOpenSnackbar] = useState(false);

    useEffect(() => {
        if (!memberId) {
            console.error("memberId가 undefined입니다.");
            setMessage("잘못된 요청입니다.");
            setOpenSnackbar(true);
            setLoading(false);
            return;
        }

        const fetchReports = async () => {
            try {
                const response = await axios.get(`/api/report/memberList/${memberId}?page=0&pageSize=10`);
                console.log("Response data:", response.data); // 응답 데이터 로그
                setReports(response.data.content);
            } catch (error) {
                console.error("신고 리스트를 가져오는 중 오류 발생:", error);
                setMessage("신고 리스트를 가져오는데 실패 했습니다.");
                setOpenSnackbar(true);
            } finally {
                setLoading(false);
            }
        };

        fetchReports();
    }, [memberId]);

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <Container maxWidth="md" style={{ marginTop: "60px" }}>
            <Typography variant="h4" gutterBottom style={{textAlign: 'center'}}>
                내 신고 리스트
            </Typography>
            <Divider style={{ border: '1px solid #ddd'}}/>
            {loading ? (
                <CircularProgress />
            ) : (
                <List>
                    {reports.map((report) => (
                        <ListItem key={report.id}>
                            <ListItemText 
                                primary={report.title}
                                secondary={report.description}
                            />
                        </ListItem>
                    ))}
                </List>
            )}
            <Snackbar 
                open={openSnackbar}
                onClose={handleCloseSnackbar}
                message={message}
                autoHideDuration={6000}
            />
        </Container>
    );
};

export default ReportUserList;