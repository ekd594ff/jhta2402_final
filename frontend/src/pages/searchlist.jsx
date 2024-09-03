import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Typography, List, ListItem, ListItemText, CircularProgress, Button, Grid } from '@mui/material';
import { useLocation } from 'react-router-dom';
import Header from '../components/common/header';

function SearchList() {
    const location = useLocation();
    const query = new URLSearchParams(location.search).get('query');
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    useEffect(() => {
        const fetchSearchResults = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await axios.get(`/api/portfolio/search/detailed?searchWord=${query}&page=${page}&size=10`);
                setResults(prevResults => [...prevResults, ...response.data.content]);
                setHasMore(response.data.content.length > 0); // 다음 페이지가 있는지 확인
            } catch (err) {
                setError('검색 결과를 가져오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        if (query) {
            fetchSearchResults();
        }
    }, [query, page]);

    const loadMoreResults = () => {
        if (hasMore) {
            setPage(prevPage => prevPage + 1);
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
            <Header />
            <Typography variant="h6" style={{ marginTop: '80px' }}>검색 결과</Typography>
            <Grid container spacing={2}>
                {results.length > 0 ? (
                    results.map((item, index) => (
                        <Grid item xs={12} sm={6} md={4} key={index}>
                            <Card>
                                <CardMedia
                                    component="img"
                                    height="140"
                                    image={item.imageUrls[0] || '/placeholder-image-url.jpg'} // 기본 이미지 추가
                                    alt={item.title}
                                />
                                <CardContent>
                                    <Typography variant="h6" component="div">{item.title}</Typography>
                                    <Typography variant="body2" color="text.secondary">{item.description}</Typography>
                                    <Typography variant="body2" color="text.secondary">{item.companyName}</Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                    ))
                ) : (
                    <Typography>No results found</Typography>
                )}
            </Grid>
            {hasMore && (
                <Button variant="contained" color="primary" onClick={loadMoreResults} disabled={loading}>
                    {loading ? 'Loading...' : '더보기'}
                </Button>
            )}
        </div>
    );
}

export default SearchList;
