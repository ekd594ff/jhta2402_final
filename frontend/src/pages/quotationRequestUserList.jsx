import React, { useEffect, useState } from "react";
import axios from "axios";

const QuotationRequestUserList = ({ memberId }) => {
  const [quotationRequests, setQuotationRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchQuotationRequests = async () => {
      try {
        const response = await axios.get(
          `/api/quotationRequest/list/${memberId}?page=1&pageSize=10` // 기본 페이지와 페이지 크기 설정
        );
        setQuotationRequests(response.data.content); // 페이지의 내용 가져오기
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchQuotationRequests();
  }, [memberId]);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h2>견적 요청 목록</h2>
      <ul>
        {quotationRequests.map((request) => (
          <li key={request.portfolioId}>
            <h3>{request.title}</h3>
            <p>{request.description}</p>
            <h4>솔루션 목록</h4>
            <ul>
              {request.solutions.map((solution) => (
                <li key={solution.id}>
                  <h5>{solution.title}</h5>
                  <p>{solution.description}</p>
                  <p>가격: {solution.price} 원</p>
                </li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default QuotationRequestUserList;
