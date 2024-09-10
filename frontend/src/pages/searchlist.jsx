import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Chip, Divider} from '@mui/material';
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

  const fetchResults = (pageToFetch = 0, reset = false) => {
    setLoading(true);
    setError(null);
    axios.get(`/api/portfolio/search/detailed?searchWord=${query}&page=${pageToFetch}&size=4`)
        .then(result => {
          setResults(prevResults => reset ? result.data.content : [...prevResults, ...result.data.content]);
          setHasMore(result.data.page.totalElements > (reset ? result.data.content.length : results.length + result.data.content.length));
        })
        .catch(() => setError('검색 결과를 가져오는 데 실패했습니다.'))
        .finally(() => {
          setLoading(false);
          setPrevQuery(query);
        });
  };

  useEffect(() => {
    if (prevQuery !== query) {
      setPage(0);
      fetchResults(0, true);
    }
  }, [query, prevQuery]);

  useEffect(() => {
    if (page > 0) {
      fetchResults(page);
    }
  }, [page]);

  const loadMoreResults = () => {
    if (hasMore && !loading) {
      setPage(prevPage => prevPage + 1);
    }
  };

  return (
      <>
        <Header/>
        <main className={style['searchList']}>
          <div className={style['container']}>
            <div className={style['search-title']}><span>{`'${query}'`}</span>{`에 대한 검색결과`}</div>
            {!loading && !results.length ?
                <div className={style['empty']}>
                  <span>검색 결과가 존재하지 않습니다</span>
                </div> :
                <>
                  <ul className={style['list']}>
                    {results.map((item, index) => <SearchListItem key={index} {...item}/>)}
                  </ul>
                  {hasMore && !loading && (
                      <Divider>
                        <Chip className={style['more']} label="더보기" size="small" variant="outlined"
                              onClick={loadMoreResults}/>
                      </Divider>
                  )}
                </>}
          </div>
        </main>
        <Footer/>
      </>
  );
}

export default SearchList;