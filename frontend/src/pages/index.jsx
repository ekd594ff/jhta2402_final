import axios from "axios";
import LoginLink from "../components/common/loginLink.jsx";
import "../styles/index.scss";
import BasicLayout from "../layout/BasicLayout.jsx";

function Index() {

    const test = () => {
        axios.post("/api/example/test",
            {},
            {withCredentials: true})
            .then((response) => {
                console.log(response)
            }).catch((error) => {
            console.log(error)
        })

    }

    return (
        <BasicLayout>
            <main>
                <h1>IntArear</h1>
                {/* 로그인 쿠키가 없으면 이동 불가 */}
                <p><LoginLink to={"/auth"} className={"auth-test"}>Auth Test</LoginLink></p>
                <button onClick={test}>Auth Test</button>
            </main>
        </BasicLayout>
    );
}

export default Index;