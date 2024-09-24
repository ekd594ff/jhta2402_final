import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Typography, List, ListItem, ListItemText, Button, Snackbar, Box } from "@mui/material";
import { useParams } from "react-router-dom";

const QuotationRequestUserList = () => {
  const [quotationRequests, setQuotationRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const { memberId } = useParams();

  useEffect(() => {
    const fetchQuotationRequests = async () => {
      if (!memberId) return;

      setLoading(true);
      setError(null);

      try {
        const response = await axios.get(
          `/api/quotationRequest/list/${memberId}?page=${page}&pageSize=10`
        );
        setQuotationRequests((prevRequests) => [
          ...prevRequests,
          ...response.data.content,
        ]);
        setHasMore(response.data.content.length > 0);
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchQuotationRequests();
  }, [memberId, page]);

  const loadMoreResults = () => {
    if (hasMore) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  const cancelQuotationRequest = async (id) => {
    try {
      await axios.put(`/api/quotationRequest/cancel/${id}`);
      setSnackbarMessage("요청이 취소되었습니다.");
      setSnackbarOpen(true);
      // 요청 목록을 다시 불러와서 최신 상태로 업데이트
      setQuotationRequests((prevRequests) => 
      prevRequests.filter((request) => request.id !== id)
      );
    } catch (err) {
      console.error(err);
      setSnackbarMessage("요청 취소 실패.");
      setSnackbarOpen(true);
    }
  };

  if (error) {
    return (
      <Typography color="error">
        {error.message || "오류가 발생했습니다."}
      </Typography>
    );
  }

  return (
    <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center">
      <Typography variant="h6" style={{ marginTop: "60px" }}>
        견적 요청 목록
      </Typography>
      <List>
        {quotationRequests.map((request) => (
          <ListItem key={request.portfolioId} divider>
            <ListItemText
              primary={request.title}
              secondary={
                <div>
                  <span>{request.description}</span>
                  <Typography variant="subtitle2" style={{ marginTop: "20px" }}>솔루션 목록</Typography>
                  <List>
                    {request.solutions.map((solution, index) => (
                      <ListItem key={`${solution.id}-${index}`}>
                        <ListItemText
                          primary={solution.title}
                          secondary={`가격: ${solution.price} 원`}
                        />
                      </ListItem>
                    ))}
                  </List>
                </div>
              }
            />
            <div style={{ display: "flex", alignItems: "center" }}>
              <Typography variant="body1" style={{ marginRight: "10px" }}>
                Progress: {request.progress}
              </Typography>
              <Button
                variant="contained"
                color="primary"
                onClick={() => cancelQuotationRequest(request.id)}
              >
                변경
              </Button>
            </div>
          </ListItem>
        ))}
      </List>
      {hasMore && (
        <div style={{ display: "flex", alignItems: "center", marginTop: "20px" }}>
          <Button onClick={loadMoreResults} disabled={loading}>
            {loading ? "Loading..." : "더보기"}
          </Button>
        </div>
      )}
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={handleSnackbarClose}
        message={snackbarMessage}
      />
    </Box>
  );
};

export default QuotationRequestUserList;
