import * as React from "react";
import { Button, TextField } from "@mui/material";
import Avatar from "@mui/material/Avatar";
import { Link, useNavigate } from "react-router-dom";
import MenuItem from "@mui/material/MenuItem";
import style from "../../styles/header.module.scss";
import { useEffect } from "react";
import axios from "axios";

function Header() {
  const navigate = useNavigate();

  const [isLoggedIn, setIsLoggedIn] = React.useState(false);
  const [isLoading, setIsLoading] = React.useState(true);
  const [username, setUsername] = React.useState("");
  const [userImage, setUserImage] = React.useState(""); // 사용자 이미지 상태 추가
  const [open, setOpen] = React.useState(false);
  const [searchTerm, setSearchTerm] = React.useState("");

  const handleClickAvatar = (event) => {
    event.stopPropagation();
    setOpen((prev) => !prev);
  };

  const handleLogout = async () => {
    try {
      await axios.get("/api/member/logout");
      setIsLoggedIn(false);
      navigate("/");
    } catch (err) {
      console.error("Logout failed", err);
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
        console.log(res);
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

    document.addEventListener("click", function (event) {
      setOpen(false);
    });
  }, []);

  // useEffect(() => {
  //     console.log(userImage);
  // } , [userImage]);

  return (
    <header className={style["header"]}>
      <div className={style["container"]}>
        <Link to="/" className={style["logoSample"]}>
          <img src="/logo.svg" />
        </Link>
        {/* 검색 기능 추가 */}
        <div className={style["search"]}>
          <TextField
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="검색어 입력"
            variant="outlined"
            size="small"
            onKeyDown={handleKeyDown}
          />
          <Button onClick={handleSearch} variant="contained">
            검색
          </Button>
        </div>
        <div className={style["buttons"]}>
          {isLoading ? (
            <></>
          ) : isLoggedIn ? (
            <>
              <Avatar
                alt={username}
                src={userImage} // 사용자 이미지 URL 설정
                onClick={handleClickAvatar}
                className={style["avatar"]}
              />
              <span className={style["username"]}>{username}</span>
              <div className={`${style["menu"]} ${open ? style["open"] : ""}`}>
                <MenuItem onClick={() => navigate("/mypage")}>My Page</MenuItem>
                <MenuItem onClick={handleCompanyClick}>Company</MenuItem>
                <MenuItem onClick={handleLogout}>Logout</MenuItem>
              </div>
            </>
          ) : (
            <>
              <Button onClick={() => navigate("/login")}>Login</Button>
              <Button onClick={() => navigate("/signup")}>Sign Up</Button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}

export default Header;
