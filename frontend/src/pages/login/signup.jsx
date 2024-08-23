import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function Signup() {

    const navigate = useNavigate()

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const signup = () => {
        axios.post('/api/signup', {
                email: email,
                password: password
            },
            {
                withCredentials: true
            }
        ).then(res => {
            alert("signup Success")
            navigate("/login")
        })
            .catch(err => alert(err));
    }

    return <>
        <input value={email} onChange={(e) => setEmail(e.target.value)}
               type="email" name="email" placeholder="Email"/>
        <input value={password} onChange={(e) => setPassword(e.target.value)}
               type="password" name="password" placeholder="Password"/>
        <button onClick={signup}>Signup</button>
    </>
}

export default Signup;