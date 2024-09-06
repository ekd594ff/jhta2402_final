import * as React from 'react';
import {Button, TextField} from "@mui/material";
import Avatar from '@mui/material/Avatar';
import {Link, useNavigate} from "react-router-dom";
import MenuItem from '@mui/material/MenuItem';
import style from "../../styles/header.module.scss";
import {useEffect, useState} from "react";
import axios from "axios";
import SearchIcon from '@mui/icons-material/Search';

function SearchBox(props) {
    const {setter, expand} = props;
    const [value, setValue] = useState("");
    const navigator = useNavigate();
    return (
        <div className={style['search-box']}>
            <div className={style['search-icon-wrapper']} onClick={() => {
                setter(prev => !prev)
            }}>
                <SearchIcon/>
            </div>
            <input
                className={`${style["styled-input-base"]} ${expand ? style['expand'] : ""}`}
                placeholder="포트폴리오 검색"
                value={value}
                onChange={(event) => setValue(event.target.value)}
                onKeyDown={(event) => {
                    if (event.key === 'Enter') {
                        navigator(`/search/detailed?query=${value}`);
                    }
                }}
            />
        </div>
    );
}

function Header() {
    const navigate = useNavigate();

    const [isLoggedIn, setIsLoggedIn] = React.useState(false);
    const [isLoading, setIsLoading] = React.useState(true);
    const [username, setUsername] = React.useState("");
    const [open, setOpen] = React.useState(false);
    const [searchTerm, setSearchTerm] = React.useState("");
    const [searchBoxExpand, setSearchBoxExpand] = useState(false);

    const handleClickAvatar = (event) => {
        event.stopPropagation();
        setOpen(prev => !prev);
    }
    const handleLogout = async () => {
        try {
            await axios.get('/api/member/logout'); // 로그아웃 API 호출
            setIsLoggedIn(false); // 로그인 상태 업데이트
            navigate("/"); // 인덱스 페이지로 리다이렉트
        } catch (err) {
            console.error("Logout failed", err);
        }
    }

    const handleSearch = () => {
        if (searchTerm) {
            navigate(`/search/detailed?query=${searchTerm}`);
        }
    }

    useEffect(() => {
        axios.get(`/api/member/email`)
            .then((res) => {
                setIsLoggedIn(res.data.id !== null);
            }).finally(() => {
            setIsLoading(false);
        });

        document.addEventListener("click", function (event) {
            setOpen(false);
        });
    }, []);

    return (
        <header className={style["header"]}>
            <div className={style['container']}>
                <Link to="/" className={`${style["logoSample"]} ${searchBoxExpand ? style['expand'] : ""}`}>
                    <img src="/logo.svg" alt="home"/>
                </Link>
                <div className={`${style["buttons"]} ${searchBoxExpand ? style['expand'] : ""}`}>
                    <SearchBox setter={setSearchBoxExpand} expand={searchBoxExpand}/>
                    {isLoading ? <></> : isLoggedIn ? (
                        <>
                            <Avatar
                                alt={username}
                                src=""
                                onClick={handleClickAvatar}
                                className={style['avatar']}
                            />
                            <div className={`${style['menu']} ${open ? style['open'] : ""}`}
                            >
                                <MenuItem onClick={() => navigate("/mypage")}>My Page</MenuItem>
                                <MenuItem onClick={handleLogout}>Logout</MenuItem>
                            </div>
                        </>
                    ) : (
                        <>
                            <Button className={style['login-btn']} variant="outlined"
                                    disableRipple
                                    onClick={() => navigate("/login")}>로그인</Button>
                            <Button className={style['signup-btn']}
                                    disableRipple
                                    onClick={() => navigate("/signup")}>회원가입</Button>
                        </>
                    )}
                </div>
            </div>
        </header>
    );
}

export default Header;
