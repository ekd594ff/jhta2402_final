import axios from "axios";
import {Link} from "react-router-dom";
import LoginLink from "../components/common/loginLink.jsx";

function Index() {

    const test = () => {
        axios.post("http://localhost:8080/api/example/test",
            {},
            {withCredentials: true})
            .then((response) => {
                console.log(response)
            }).catch((error) => {
            console.log(error)
        })

    }

    return (
        <>
            <main>
                <h1>IntArear</h1>
                <p><Link to="/login">Login</Link></p>
                <p><Link to="/signup">SignUp</Link></p>
                <p><LoginLink to={"/auth"} className={"auth-test"}>Auth Test</LoginLink></p>
                <button onClick={test}>Auth Test</button>
            </main>
        </>
    );
}

export default Index;