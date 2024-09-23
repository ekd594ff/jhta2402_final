import * as React from "react";
import { Button, TextField } from "@mui/material";
import Avatar from "@mui/material/Avatar";
import { Link, useNavigate } from "react-router-dom";
import MenuItem from "@mui/material/MenuItem";
import style from "../../styles/header.module.scss";
import { useEffect, useState } from "react";
import axios from "axios";

import SearchIcon from "@mui/icons-material/Search";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import BusinessIcon from "@mui/icons-material/Business";
import LogoutIcon from "@mui/icons-material/Logout";
import DashboardIcon from "@mui/icons-material/Dashboard";

function SearchBox(props) {
  const { setter, expand } = props;
  const [value, setValue] = useState("");
  const navigator = useNavigate();
  return (
    <div className={style["search-box"]}>
      <div
        className={style["search-icon-wrapper"]}
        onClick={() => {
          setter((prev) => !prev);
        }}
      >
        <SearchIcon />
      </div>
      <input
        className={`${style["styled-input-base"]} ${
          expand ? style["expand"] : ""
        }`}
        placeholder="포트폴리오 검색"
        value={value}
        onChange={(event) => setValue(event.target.value)}
        onKeyDown={(event) => {
          if (event.key === "Enter") {
            if (!value.trim()) {
              window.alert("검색어를 입력해 주세요");
              return;
            }
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
  const [isAdmin, setIsAdmin] = useState(false);
  const [username, setUsername] = React.useState("");
  const [open, setOpen] = React.useState(false);
  const [searchTerm, setSearchTerm] = React.useState("");
  const [searchBoxExpand, setSearchBoxExpand] = useState(false);
  const [userImage, setUserImage] = useState("");
  const [hasCompany, setHasCompany] = useState(false);

  const handleClickAvatar = (event) => {
    event.stopPropagation();
    setOpen((prev) => !prev);
  };

  const handleLogout = async () => {
    try {
      await axios.get("/api/member/logout"); // 로그아웃 API 호출
      setIsLoggedIn(false); // 로그인 상태 업데이트
      navigate("/"); // 인덱스 페이지로 리다이렉트
    } catch (err) {
      console.error("Logout failed", err);
    }
  };

  const checkCompany = async () => {
    try {
      const response = await axios.get("/api/company/info", {
        withCredentials: true,
      });
      setHasCompany(response.data.companyName !== null);
    } catch (error) {
      setHasCompany(false);
    }
  };

  const handleSearch = () => {
    if (searchTerm) {
      navigate(`/search/detailed?query=${searchTerm}`);
    }
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      handleSearch();
    }
  };

  const handleCompanyClick = async () => {
    try {
      const response = await axios.get("/api/member/seller/role");
      const hasSellerRole = response.status;

      if (hasSellerRole === 200) {
        navigate("/company/edit");
      }
    } catch (error) {
      alert("회사가 없습니다. 생성 페이지로 이동합니다.");
      navigate("/company/create");
    }
  };

  useEffect(() => {
    axios
      .get(`/api/member/email`)
      .then((res) => {
        setIsLoggedIn(res.data.id !== null);
        setUsername(res.data.username); // 사용자 이름 설정

        // 이미지 배열이 존재하고 길이가 0이 아닐 때만 URL 설정
        if (res.data.images && res.data.images.length > 0) {
          setUserImage(res.data.images[res.data.images.length - 1].url); // 사용자 이미지 URL 설정
        } else {
          setUserImage(""); // 기본 이미지 또는 빈 문자열 생성
        }
      })
      .finally(() => {
        setIsLoading(false);
      });

    axios
      .get(`/api/member/admin/role`)
      .then((result) => {
        setIsAdmin(true);
      })
      .catch((err) => {
        setIsAdmin(false);
      });

    checkCompany();

    document.addEventListener("click", function (event) {
      setOpen(false);
    });
  }, []);

  return (
    <header className={style["header"]}>
      <div className={style["container"]}>
        <Link
          to="/"
          className={`${style["logoSample"]} ${
            searchBoxExpand ? style["expand"] : ""
          }`}
        >
          <img src="/logo.svg" alt="home" />
        </Link>
        <div
          className={`${style["buttons"]} ${
            searchBoxExpand ? style["expand"] : ""
          }`}
        >
          <SearchBox setter={setSearchBoxExpand} expand={searchBoxExpand} />
          {isLoading ? (
            <></>
          ) : isLoggedIn ? (
            <>
              <Avatar
                alt={username}
                src={userImage}
                onClick={handleClickAvatar}
                className={style["avatar"]}
              />
              <span>{username}</span>
              <div className={`${style["menu"]} ${open ? style["open"] : ""}`}>
                <MenuItem onClick={() => navigate("/mypage")}>
                  <AccountCircleIcon />
                  My Page
                </MenuItem>
                <MenuItem
                  onClick={() =>
                    navigate(hasCompany ? "/company/info" : "/company/create")
                  }
                >
                  <BusinessIcon />
                  Company
                </MenuItem>
                <MenuItem onClick={handleLogout}>
                  <LogoutIcon />
                  Logout
                </MenuItem>
                {isAdmin ? (
                  <MenuItem
                    onClick={() => {
                      navigate("/admin");
                    }}
                  >
                    <DashboardIcon />
                    Admin
                  </MenuItem>
                ) : (
                  <></>
                )}
              </div>
            </>
          ) : (
            <>
              <Button
                className={style["login-btn"]}
                variant="outlined"
                disableRipple
                onClick={() => navigate("/login")}
              >
                로그인
              </Button>
              <Button
                className={style["signup-btn"]}
                disableRipple
                onClick={() => navigate("/signup")}
              >
                회원가입
              </Button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}

export default Header;
