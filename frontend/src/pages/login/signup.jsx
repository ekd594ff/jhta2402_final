import {useState} from "react";
import axios from "axios";
import {useNavigate, Link} from "react-router-dom";
import Button from "@mui/material/Button";

import style from "../../styles/signup.module.scss";
import TextField from "@mui/material/TextField";

function Signup() {

    const navigate = useNavigate()

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [signUp, setSignUp] = useState({email: "", password: "", passwordConfirm: "", username: ""});

    // const signup = () => {
    //     axios.post('/api/member/signup', {
    //             email: email,
    //             password: password
    //         },
    //         {
    //             withCredentials: true
    //         }
    //     ).then(res => {
    //         alert("signup Success")
    //         navigate("/login")
    //     })
    //         .catch(err => alert(err));
    // }

    return <>
        <main className={style['signup']}>
            <div className={style['container']}>
                <form>
                    <TextField
                        required
                        onChange={(e) => setSignUp({...signUp, email: e.target.value})}
                        name="email"
                        label="Email"
                        variant="standard"
                        value={signUp.email}
                        InputLabelProps={{shrink: true}}
                    />
                    <TextField
                        required
                        onChange={(e) => setSignUp({...signUp, password: e.target.value})}
                        name="password"
                        label="Password"
                        variant="standard"
                        value={signUp.email}
                        InputLabelProps={{shrink: true}}
                    />
                    <input value={password} onChange={(e) => setPassword(e.target.value)}
                           type="password" name="password" placeholder="Password"/>
                    <div className={style['btn-group']}>
                        <Button variant="outlined" size="large" className={style['signup-btn']}>
                            회원가입
                        </Button>
                    </div>
                </form>
            </div>
        </main>
    </>
}

export default Signup;