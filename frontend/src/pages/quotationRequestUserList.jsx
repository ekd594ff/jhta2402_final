import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Typography,
  List,
  ListItem,
  ListItemText,
  CircularProgress,
  Button,
  Grid,
} from "@mui/material";

const QuotationRequestUserList = ({ memberId }) => {
  const [quotationRequests, setQuotationRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  useEffect(() => {
    const fetchQuotationRequests = async () => {
      if (!memberId) return;

      setLoading(true);
      setError(null);

      try {
        const response = await axios.get(
          `/api/quotationRequest/list/${memberId}?page=${page}&pageSize=10` // 기본 페이지와 페이지 크기 설정
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

  if (loading && page === 0) {
    return <CircularProgress />;
  }
  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  return (
    <div>
      <Typography variant="h6" style={{ margin: "20px 0" }}>
        견적 요청 목록
      </Typography>
      <List>
        {quotationRequests.map((request) => (
          <ListItem key={request.portfolioId} divider>
            <ListItemText
              primary={request.title}
              secondary={
                <>
                  <span>{request.description}</span>
                  <br />
                  <Typography variant="subtitle2">솔루션 목록</Typography>
                  <List>
                    {request.solutions.map((solution) => (
                      <ListItem key={solution.id}>
                        <ListItemText
                          primary={solution.title}
                          secondary={`가격: ${solution.price} 원`}
                        />
                      </ListItem>
                    ))}
                  </List>
                </>
              }
            />
          </ListItem>
        ))}
      </List>
      {hasMore && <Button>{loading ? "Loading..." : "더보기"}</Button>}
    </div>
  );
};

export default QuotationRequestUserList;
