import { useState } from "react";
import axios from "axios";
import { useNavigate, Link } from "react-router-dom";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";

import style from "../../styles/login.module.scss";

function Login({ setIsLoggedIn, setUsername }) {
  const navigate = useNavigate();

  const [login, setLogin] = useState({ email: "", password: "" });

  const submit = (e) => {
    e.preventDefault(); // 기본 폼 제출 방지
    const { email, password } = login;
    axios
      .post("/api/member/login", { email, password }, { withCredentials: true })
      .then((res) => {
        alert("login Success");
        setIsLoggedIn(true);
        setUsername(email);
        console.log("Logeed in: ", email);
        navigate("/");
      })
      .catch((err) =>
        alert(err.response ? err.response.data.message : "로그인 실패")
      );
  };

  return (
    <>
      <main className={style["login"]}>
        <div className={style["container"]}>
          <form method="post">
            <TextField
              required
              onChange={(e) => setLogin({ ...login, email: e.target.value })}
              name="email"
              label="이메일"
              variant="standard"
              value={login.email}
              InputLabelProps={{
                shrink: true,
              }}
            />
            <TextField
              required
              onChange={(e) => setLogin({ ...login, password: e.target.value })}
              name="password"
              label="비밀번호"
              variant="standard"
              type="password"
              value={login.password}
              InputLabelProps={{
                shrink: true,
              }}
            />
            <div className={style["btn-group"]}>
              <Button
                variant="contained"
                className={style["login-btn"]}
                size="large"
                onClick={submit}
              >
                로그인
              </Button>
              <div className={style["others"]}>
                <Link to="/signup">회원가입</Link>
              </div>
            </div>
          </form>
        </div>
      </main>
    </>
  );
}

export default Login;
