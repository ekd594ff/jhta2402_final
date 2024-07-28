import axios from "axios";

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
                <p><a href="/login">Login</a></p>
                <p><a href="/signup">SignUp</a></p>
                <button onClick={test}>Auth Test</button>
            </main>
        </>
    );
}

export default Index;