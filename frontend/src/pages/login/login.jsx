import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate, Link, useSearchParams} from "react-router-dom";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import style from "../../styles/login.module.scss";
import {Alert, Icon, Snackbar} from "@mui/material";
import IconButton from "@mui/material/IconButton";

function Login() {

    const navigate = useNavigate();

    const [login, setLogin] = useState({email: "", password: ""});

    const submit = (e) => {
        e.preventDefault(); // 기본 폼 제출 방지
        const {email, password} = login;
        axios.post('/api/member/login', {email, password}, {withCredentials: true})
            .then(res => {
                navigate("/");
            })
            .catch(err => alert(err.response ? err.response.data.message : "로그인 실패"));
    }

    const [snackbarState, setSnackbarState] = useState({
        open: false,
        message: "",
    });

    const [searchParams] = useSearchParams();

    useEffect(() => {
        const errorMessage = searchParams.get("error");

        if (errorMessage !== null) {
            setSnackbarState({
                message: `${errorMessage}로 가입한 계정입니다. 로그인을 다시 시도해주세요.`,
                open: true
            });
        }
    }, []);

    const naverLogin = () => {
        window.location.href = "/oauth2/authorization/naver";
    }

    const googleLogin = () => {
        window.location.href = "/oauth2/authorization/google"
    }

    const kakaoLogin = () => {
        window.location.href = "/oauth2/authorization/kakao"
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
                        <Button variant="outlined" className={style['register-btn']} size="large" onClick={() => {
                            navigate("/signup");
                        }}>
                            회원가입
                        </Button>
                    </div>
                    <div className={style['div-social']}>
                        <div className={style["separator"]}>간편 로그인</div>
                        <div className={style["btn-group-social"]}>
                            <IconButton className={style["login-icon-button"]} aria-label="small"
                                        onClick={naverLogin} size="small" children={
                                <Icon className={style["icon-circle"]}>
                                    <img src="/icon/naver-circle.svg" alt="naver-logo"/>
                                </Icon>}
                            />
                            <IconButton className={style["login-icon-button"]} aria-label="small"
                                        onClick={kakaoLogin} size="small" children={
                                <Icon className={style["icon-circle"]}>
                                    <img src="/icon/kakao-circle.svg" alt="kakao-logo"/>
                                </Icon>}
                            />
                            <IconButton className={style["login-icon-button"]} aria-label="small"
                                        onClick={googleLogin} size="small" children={
                                <Icon className={style["icon-circle"]}>
                                    <img src="/icon/google-circle.svg" alt="google-logo"/>
                                </Icon>}
                            />
                        </div>
                    </div>
                </form>
            </div>

            <Snackbar
                anchorOrigin={{vertical: "top", horizontal: "center"}}
                open={snackbarState.open}
                onClose={() => setSnackbarState({...snackbarState, open: false})}
                message={snackbarState.message}>
                <Alert
                    onClose={() => setSnackbarState({...snackbarState, open: false})}
                    severity="error"
                    variant="outlined"
                    sx={{
                        width: '100%',
                        bgcolor: 'background.paper',
                    }}
                >
                    {snackbarState.message}
                </Alert>
            </Snackbar>
        </main>
    </>;
}

export default Login;