import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Chip, Divider} from '@mui/material';
import {useLocation} from 'react-router-dom';
import {FormControl, InputLabel, Select, MenuItem} from '@mui/material';
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
    const [sortField, setSortField] = useState('rate');
    const [sortDirection, setSortDirection] = useState('desc');
    const [reset, setReset] = useState(false);
    const pageSize = 12;

    const fetchResults = (pageToFetch = 0, reset = false, sortField = 'createdAt', sortDirection = 'desc') => {
        setLoading(true);
        setError(null);
        axios.get(`/api/portfolio/search/detailed?searchWord=${query}&page=${pageToFetch}&size=${pageSize}&sortField=${sortField}&sortDirection=${sortDirection}`)
            .then(result => {
                setResults(prevResults => reset ? [...result.data.content] : [...prevResults, ...result.data.content]);
                setHasMore(result.data.content.length === pageSize);
            })
            .catch(() => setError('검색 결과를 가져오는 데 실패했습니다.'))
            .finally(() => {
                setLoading(false);
                setPrevQuery(query);
            });
    };

    useEffect(() => {
        if (prevQuery !== query) {
            fetchResults(0, true, sortField, sortDirection);
            setReset(true);
        }
    }, [query, prevQuery]);

    useEffect(() => {
        if (page > 0) {
            fetchResults(page, false, sortField, sortDirection);
        }
    }, [page, sortField, sortDirection]);

    useEffect(() => {
        fetchResults(0, true, sortField, sortDirection);
        setReset(true);
    }, [sortField, sortDirection, setReset]);

    useEffect(() => {
        if (reset) {
            setPage(0);
            setHasMore(true);
            setReset(false);
        }
    }, [reset, setReset, setPage, setHasMore]);

    const loadMoreResults = () => {
        if (hasMore && !loading) {
            setPage(prevPage => prevPage + 1);
        }
    };

    const handleSortFieldChange = (event) => {
        setSortField(event.target.value);
    };

    const handleSortDirectionChange = (event) => {
        setSortDirection(event.target.value);
    };
    return (
        <>
            <Header/>
            <main className={style['searchList']}>
                <div className={style['container']}>
                    <div className={style['search-title']}>
                        <div><span>{`'${query}'`}</span>{`에 대한 검색결과`}</div>
                        <div className={style['search-menu']}>
                            <FormControl variant="outlined" size="small" className={style['select']}>
                                <InputLabel>정렬</InputLabel>
                                <Select
                                    value={sortField}
                                    onChange={handleSortFieldChange}
                                    label="정렬"
                                >
                                    <MenuItem value="createdAt"><em>날짜순</em></MenuItem>
                                    <MenuItem value="companyName">이름순</MenuItem>
                                    <MenuItem value="rate">인기순</MenuItem>
                                </Select>
                            </FormControl>
                            <FormControl variant="outlined" size="small" className={style['select']}>
                                <InputLabel>순서</InputLabel>
                                <Select
                                    value={sortDirection}
                                    onChange={handleSortDirectionChange}
                                    label="정렬"
                                >
                                    <MenuItem value="asc"><em>오름차순</em></MenuItem>
                                    <MenuItem value="desc">내림차순</MenuItem>
                                </Select>
                            </FormControl>
                        </div>
                    </div>

                    {!loading && !results.length ?
                        <div className={style['empty']}>
                            <span>검색 결과가 존재하지 않습니다</span>
                        </div> :
                        <>
                            <ul className={style['list']}>
                                {results.map((item, index) => {
                                    const {id} = item;
                                    return <SearchListItem {...item} key={`${id}_${index}`}/>;
                                })}
                            </ul>
                            {hasMore && !loading ? (
                                <Divider>
                                    <Chip className={style['more']} label="더보기" size="small" variant="outlined"
                                          onClick={loadMoreResults}/>
                                </Divider>) : <></>}
                        </>}
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default SearchList;