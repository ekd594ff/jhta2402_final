import React, { useState } from 'react';
import { Container, Button, List, ListItem, ListItemText, ListItemSecondaryAction, Paper, Snackbar } from '@mui/material';

const initialSolutions = [
    { id: 1, name: '솔루션 A', price: 100 },
    { id: 2, name: '솔루션 B', price: 150 },
    { id: 3, name: '솔루션 C', price: 200 },
    { id: 4, name: '솔루션 D', price: 250 },
    { id: 5, name: '솔루션 E', price: 300 },
    { id: 6, name: '솔루션 F', price: 350 },
    { id: 7, name: '솔루션 G', price: 400 },
    { id: 8, name: '솔루션 H', price: 450 },
    { id: 9, name: '솔루션 I', price: 500 },
    { id: 10, name: '솔루션 J', price: 550 },
];

const initialSelectedSolutions = [
    { id: 1, name: '솔루션 A', price: 100 },
    { id: 3, name: '솔루션 C', price: 200 },
    { id: 5, name: '솔루션 E', price: 300 },
];

const QuotationRequest = () => {
    const [selectedSolutions, setSelectedSolutions] = useState(initialSelectedSolutions);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    // 솔루션 추가
    const addSolution = (solution) => {
        if (selectedSolutions.find(s => s.id === solution.id)) {
            setSnackbarMessage('이미 신청한 솔루션입니다.');
            setOpenSnackbar(true);
        } else {
            setSelectedSolutions([...selectedSolutions, solution]);
        }
    };

    // 솔루션 제거
    const removeSolution = (id) => {
        setSelectedSolutions(selectedSolutions.filter(solution => solution.id !== id));
    };

    // 신청 완료 핸들러
    const handleRequestSubmit = () => {
        setSnackbarMessage('신청 완료');
        setOpenSnackbar(true);
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
                        {selectedSolutions.map(solution => (
                            <ListItem key={solution.id}>
                                <ListItemText primary={solution.name} secondary={`${solution.price} 원`} />
                                <ListItemSecondaryAction>
                                    <Button variant="outlined" color="secondary" onClick={() => removeSolution(solution.id)}>
                                        취소
                                    </Button>
                                </ListItemSecondaryAction>
                            </ListItem>
                        ))}
                    </List>
                )}
            </Paper>
            <h5 style={{ marginBottom: '16px' }}>가능한 솔루션</h5>
            <List>
                {initialSolutions.map(solution => (
                    <ListItem key={solution.id}>
                        <ListItemText primary={solution.name} secondary={`${solution.price} 원`} />
                        <ListItemSecondaryAction>
                            <Button variant="contained" color="primary" onClick={() => addSolution(solution)}>
                                신청하기
                            </Button>
                        </ListItemSecondaryAction>
                    </ListItem>
                ))}
            </List>
            <Button variant="contained" color="success" onClick={handleRequestSubmit} style={{ marginTop: '16px' }}>
                신청 완료
            </Button>

            {/* Snackbar 컴포넌트 */}
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
