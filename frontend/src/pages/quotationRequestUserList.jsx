import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Typography,
  List,
  ListItem,
  ListItemText,
  Button,
} from "@mui/material";
import { useParams } from "react-router-dom";

const QuotationRequestUserList = () => {
  const [quotationRequests, setQuotationRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  const { memberId } = useParams();

  useEffect(() => {
    const fetchQuotationRequests = async () => {
      console.log(memberId);
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
        console.error(err);
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

  if (error) {
    return (
      <Typography color="error">
        {error.message || "오류가 발생했습니다."}
      </Typography>
    );
  }

  useEffect(() => {
    console.log(quotationRequests);
  }, [quotationRequests]);

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
                <div>
                  <span>{request.description}</span>
                  <Typography variant="subtitle2">솔루션 목록</Typography>
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
          </ListItem>
        ))}
      </List>
      {hasMore && (
        <Button onClick={loadMoreResults} disabled={loading}>
          {loading ? "Loading..." : "더보기"}
        </Button>
      )}
    </div>
  );
};

export default QuotationRequestUserList;
