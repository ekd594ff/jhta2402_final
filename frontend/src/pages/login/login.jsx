import {useState} from "react";
import axios from "axios";
import {useNavigate, Link} from "react-router-dom";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import style from "../../styles/login.module.scss";

function Login() {

    const navigate = useNavigate();

    const [login, setLogin] = useState({email: "", password: ""});

    const submit = (e) => {
        e.preventDefault(); // 기본 폼 제출 방지
        const {email, password} = login;
        axios.post('/api/member/login', {email, password}, {withCredentials: true})
            .then(res => {
                alert("login Success");
                navigate("/");
            })
            .catch(err => alert(err.response ? err.response.data.message : "로그인 실패"));
    }

    const naverLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/naver";
    }

    const googleLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/google"
    }

    const kakaoLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/kakao"
    }

    return <>
        <main className={style['login']}>
            <div className={style['container']}>
                <Link to="/">
                    <img className={style['logo']} alt={"logo"} src="/logo.svg"/>
                </Link>
                <form method="post" onKeyDown={(event) => {
                    const key = event.key;
                    if (key === 'Enter') {
                        submit(event);
                    }
                }}>
                    <TextField
                        required
                        onChange={(e) => setLogin({...login, email: e.target.value})}
                        name="email"
                        label="이메일"
                        variant="standard"
                        value={login.email}
                        slotProps={{
                            inputLabel: {
                                shrink: true,
                            }
                        }}
                    />
                    <TextField
                        required
                        onChange={(e) => setLogin({...login, password: e.target.value})}
                        name="password"
                        label="비밀번호"
                        variant="standard"
                        type="password"
                        value={login.password}
                        slotProps={{
                            inputLabel: {
                                shrink: true,
                            }
                        }}
                    />
                    <div className={style['btn-group']}>
                        <Button variant="contained" className={style['login-btn']} size="large" onClick={submit}>
                            로그인
                        </Button>
                        <div className={style['others']}>
                            <Link to="/signup">회원가입</Link>
                        </div>
                    </div>
                    <div>
                        <Button variant="outlined" onClick={naverLogin}>
                            네이버 로그인
                        </Button>
                        <Button variant="outlined" onClick={googleLogin}>
                            구글 로그인
                        </Button>
                        <Button variant="outlined" onClick={kakaoLogin}>
                            카카오 로그인
                        </Button>
                    </div>
                </form>
            </div>
        </main>
    </>;
}

export default Login;