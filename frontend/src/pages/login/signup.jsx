import {useState, useEffect} from "react";
import axios from "axios";
import {useNavigate, Link} from "react-router-dom";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import {debounce} from "lodash";

import style from "../../styles/signup.module.scss";

function Signup() {

    const navigate = useNavigate()

    const [passwordConfirm, setPasswordConfirm] = useState('');
    const [signUp, setSignUp] = useState({email: "", password: "", username: ""});
    const [signUpMsg, setSignUpMsg] = useState({email: "　", password: "　", passwordConfirm: "　", username: "　"});
    const [regex] = useState({
        username: /^[가-힣A-Za-z0-9]{2,16}$/,
        password: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/,
        email: /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/
    });
    const [validation, setValidation] = useState({
        email: false,
        password: false,
        passwordConfirm: false,
        username: false
    });

    const checkUsernameDebounce = debounce((username) => {
        axios.post('/api/duplication/username', {
            username
        }, {withCredentials: true})
            .then(result => {
            })
            .catch(err => {
                setSignUpMsg({...signUpMsg, username: "중복된 닉네임 입니다"});
                setValidation({...validation, username: false});
            })
    }, 150);

    const checkEmailDebounce = debounce((email) => {
        axios.post('/api/duplication/email', {
            email
        }, {withCredentials: true})
            .then(result => {
            })
            .catch(err => {
                setSignUpMsg({...signUpMsg, email: "중복된 이메일 입니다"});
                setValidation({...validation, email: false});
            })
    }, 150);

    const submit = (event) => {
        event.preventDefault();
        for (const key in validation) {
            const item = validation[key];
            if (!item) {
                window.alert("입력한 정보가 올바르지 않습니다. 다시 확인해주세요");
                return;
            }
        }
        axios.post('/api/member/signup', signUp,
            {
                withCredentials: true
            }
        ).then(res => {
            window.alert("회원가입 완료");
            navigate("/login")
        })
            .catch(err => alert(err));
    }

    useEffect(() => {
        if (!passwordConfirm) {
            setSignUpMsg({...signUpMsg, passwordConfirm: "　"});
            setValidation({...validation, passwordConfirm: false});
            return;
        }
        if (signUp.password === passwordConfirm) {
            setSignUpMsg({...signUpMsg, passwordConfirm: "비밀번호가 일치합니다"});
            setValidation({...validation, passwordConfirm: true});
        } else {
            setSignUpMsg({...signUpMsg, passwordConfirm: "비밀번호가 일치하지 않습니다"});
            setValidation({...validation, passwordConfirm: false});
        }
    }, [signUp.password, passwordConfirm]);

    return <>
        <main className={style['signup']}>
            <div className={style['container']}>
                <Link to="/">
                    <img className={style['logo']} alt="logo" src="/logo.svg"/>
                </Link>
                <form onKeyDown={(event) => {
                    const key = event.key;
                    if (key === 'Enter') {
                        submit(event);
                    }
                }}>
                    <TextField value={signUp.email} onChange={(e) => {
                        const value = e.target.value;
                        setSignUp({...signUp, email: value});
                        if (!value) {
                            setSignUpMsg({...signUpMsg, email: "　"});
                            setValidation({...validation, email: false});
                        } else {
                            if (regex.email.test(value)) {
                                setSignUpMsg({...signUpMsg, email: "사용 가능한 이메일 입니다"});
                                checkEmailDebounce(value);
                                setValidation({...validation, email: true});
                            } else {
                                setSignUpMsg({...signUpMsg, email: "사용할 수 없는 이메일 입니다"});
                                setValidation({...validation, email: false});
                            }
                        }
                    }}
                               type="email"
                               name="email"
                               placeholder=""
                               variant="standard"
                               label="이메일"
                               helperText={signUpMsg.email}
                               slotProps={{
                                   inputLabel: {
                                       shrink: true,
                                   }
                               }}/>
                    <TextField value={signUp.username}
                               onChange={(e) => {
                                   const value = e.target.value;
                                   setSignUp({...signUp, username: value});
                                   if (!value) {
                                       setSignUpMsg({...signUpMsg, username: "　"});
                                       setValidation({...validation, username: false});
                                   } else {
                                       if (regex.username.test(value)) {
                                           setSignUpMsg({...signUpMsg, username: "사용 가능한 닉네임 입니다"});
                                           checkUsernameDebounce();
                                           setValidation({...validation, username: true});
                                       } else {
                                           setSignUpMsg({...signUpMsg, username: "사용할 수 없는 닉네임 입니다"});
                                           setValidation({...validation, username: false});
                                       }
                                   }
                               }}
                               type="text"
                               name="username"
                               placeholder="2글자 이상 4글자 이하 한글"
                               variant="standard"
                               label="닉네임"
                               helperText={signUpMsg.username}
                               slotProps={{
                                   inputLabel: {
                                       shrink: true,
                                   }
                               }}/>
                    <TextField value={signUp.password}
                               onChange={(e) => {
                                   const value = e.target.value;
                                   setSignUp({...signUp, password: value});
                                   if (!value) {
                                       setSignUpMsg({...signUpMsg, password: "　"});
                                       setValidation({...validation, password: false});
                                   } else {
                                       if (regex.password.test(value)) {
                                           setSignUpMsg({...signUpMsg, password: "사용 가능한 비밀번호 입니다"});
                                           setValidation({...validation, password: true});
                                       } else {
                                           setSignUpMsg({...signUpMsg, password: "사용할 수 없는 비밀번호 입니다"});
                                           setValidation({...validation, password: false});
                                       }
                                   }
                               }}
                               type="password"
                               name="password"
                               label="비밀번호"
                               placeholder="영문 숫자조합 8자리 이상"
                               variant="standard"
                               helperText={signUpMsg.password}
                               slotProps={{
                                   inputLabel: {
                                       shrink: true,
                                   }
                               }}/>
                    <TextField value={passwordConfirm} onChange={(e) => setPasswordConfirm(e.target.value)}
                               type="password"
                               name="passwordConfirm"
                               label="비밀번호 확인"
                               placeholder="비밀번호 확인"
                               variant="standard"
                               helperText={signUpMsg.passwordConfirm}
                               slotProps={{
                                   inputLabel: {
                                       shrink: true,
                                   }
                               }}/>
                    <Button variant="outlined" className={style['register-btn']} size="large" onClick={submit}>
                        회원가입
                    </Button>
                </form>
            </div>
        </main>
    </>;
}

export default Signup;