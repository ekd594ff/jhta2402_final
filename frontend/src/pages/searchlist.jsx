import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Typography, CircularProgress, Button, Grid} from '@mui/material';
import {useLocation} from 'react-router-dom';

import Header from '../components/common/header';
import Footer from "../components/common/footer.jsx";
import SearchListItem from "../components/searchlist/search-list-item.jsx";

import style from "../styles/serachlist.module.scss";


function SearchList() {
    const location = useLocation();
    const query = new URLSearchParams(location.search).get('query');
    const [prevQuery, setPrevQuery] = useState("");
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
                const response = await axios.get(`/api/portfolio/search/detailed?searchWord=${query}&page=${page}&size=1`);
                setResults(prevResults => {
                    const newList = [...prevResults, ...response.data.content];
                    setHasMore(newList.length < response.data.page.totalElements);
                    return newList;
                });
            } catch (err) {
                setError('검색 결과를 가져오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        if (query) {
            if (query !== prevQuery) {
                console.log("1");
                setLoading(true);
                setError(null);
                axios.get(`/api/portfolio/search/detailed?searchWord=${query}&page=${page}&size=1`)
                    .then(result => {
                        setResults(prevResults => {
                            return [...result.data.content];
                        });
                        setHasMore(result.data.page.totalElements > result.data.content.length);
                    })
                    .catch(err => {
                        setError('검색 결과를 가져오는 데 실패했습니다.');
                    })
                    .finally(() => {
                        setLoading(false);
                        setPrevQuery(query);
                    });
            } else {
                console.log("2");
                fetchSearchResults();
            }
        }
    }, [prevQuery, query, page]);


    const loadMoreResults = () => {
        if (hasMore) {
            setPage(prevPage => prevPage + 1);
        }
    };

    if (loading && page === 0) {
        return <CircularProgress/>;
    }
    if (error) {
        return <Typography color="error">{error}</Typography>;
    }

    return (
        <>
            <Header/>
            <main className={style['searchList']}>
                <div className={style['container']}>
                    <div className={style['search-title']}><span>{`'${query}'`}</span>{`에 대한 검색결과`}</div>
                    <ul className={style['list']}>
                        {
                            results.map((item, index) => <SearchListItem key={index} {...item}/>)
                        }
                    </ul>
                    {hasMore && (
                        <Button variant="contained" color="primary" onClick={loadMoreResults} disabled={loading}>
                            {loading ? 'Loading...' : '더보기'}
                        </Button>
                    )}
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default SearchList;
