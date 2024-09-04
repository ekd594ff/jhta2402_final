import React, { useState } from 'react';
import { Container, Button, List, ListItem, ListItemText, Paper, Snackbar, TextField } from '@mui/material';
import axios from 'axios';

const initialSolutions = [
    { title: 'SEO Optimization', description: 'Optimize the website for search engines.', price: 500 },
    { title: 'E-commerce Integration', description: 'Integrates a shopping cart and payment gateway.', price: 1500 },
    { title: 'Basic Website Design', description: 'Includes home page, about us, and contact us pages.', price: 1000 },
];

const QuotationRequest = () => {
    const [selectedSolutions, setSelectedSolutions] = useState([]);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [Count, setCount] = useState(1);
    const [customDescription, setCustomDescription] = useState('');

    // 솔루션 추가
    const addSolution = (solution) => {
        if (selectedSolutions.find(s => s.title === solution.title)) {
            setSnackbarMessage('이미 신청한 솔루션입니다.');
            setOpenSnackbar(true);
        } else {
            setSelectedSolutions([...selectedSolutions, solution]);
        }
    };

    // 신청 완료 핸들러
    const handleRequestSubmit = async () => {
        try {
            const data = {
                memberId: "6f3f2761-0fa0-43f3-bd22-e658e2a5d7df",
                portfolioId: "a84a9e98-3eb0-4542-9bd0-94b54de5276f",
                title: `title${Count}`,
                description: customDescription,
                solutions: selectedSolutions,
            };
            console.log(data);
            const response = await axios.post('/api/quotationRequest/create', data);
            if (response.status === 200) {
                setSnackbarMessage('신청 완료');
                setOpenSnackbar(true);
                setSelectedSolutions([]);
                setCustomDescription('');
                setCount(Count + 1);
            }

        } catch (error) {
            setSnackbarMessage('신청 실패: ' + error.message);
            setOpenSnackbar(true);
        }
    };

    // Snackbar 닫기 핸들러
    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <Container>
            <h4 style={{ marginBottom: '16px' }}>신청한 솔루션 확인</h4>
            <Paper style={{ padding: '16px', marginBottom: '16px' }}>
                {selectedSolutions.length === 0 ? (
                    <p>신청한 솔루션이 없습니다.</p>
                ) : (
                    <List>
                        {selectedSolutions.map((solution, index) => (
                            <ListItem key={index}>
                                <ListItemText primary={solution.title} secondary={`${solution.description} - ${solution.price} 원`} />
                            </ListItem>
                        ))}
                    </List>
                )}
            </Paper>
            <h5 style={{ marginBottom: '16px' }}>가능한 솔루션</h5>
            <List>
                {initialSolutions.map((solution, index) => (
                    <ListItem key={index}>
                        <ListItemText primary={solution.title} secondary={`${solution.description} - ${solution.price} 원`} />                       
                            <Button variant="contained" color="primary" onClick={() => addSolution(solution)}>
                                신청하기
                            </Button>
                    </ListItem>
                ))}
            </List>

            <TextField
                label="요청사항"
                variant="outlined"
                fullWidth
                multiline
                rows={4}
                value={customDescription}
                onChange={(e) => setCustomDescription(e.target.value)}
                style={{ marginTop: '16px', marginBottom: '16px' }}
            />

            <Button variant="contained" color="success" onClick={handleRequestSubmit} style={{ marginTop: '16px' }}>
                신청 완료
            </Button>

            <Snackbar
                open={openSnackbar}
                autoHideDuration={3000}
                onClose={handleCloseSnackbar}
                message={snackbarMessage || "신청 완료"}
            />
        </Container>
    );
};

export default QuotationRequest;
