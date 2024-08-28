import React, {useEffect, useState} from 'react';
import axios from "axios";

function Test(props) {

    // 버튼 클릭 시 실행
    // const clickFunction = () => {
    //     axios.get("api/portfolio/search?searchWord=title&page=0&size=10")
    //         .then(res => {
    //             console.log(res.data);
    //         });
    // }

    // 화면에 변경사항 반영하기 위해 useState 사용
    const [portfolio, setPortfolio] = useState([]);

    // 페이지 로드 시 실행
    useEffect(() => {
        axios.get("api/portfolio/search?searchWord=title&page=0&size=10")
            .then(res => {
                console.log(res.data.content);
                setPortfolio(res.data.content);
            });
    }, []);

    return (
        <div>
            {/*<button onClick={clickFunction}>Click</button>*/}

            {portfolio.map(p => <div key={p.title}>
                <p>{p.title}</p>
                <p>{p.description}</p>
                <p>{p.companyName}</p>
                <p>{p.createDate}</p>
            </div>)}

        </div>
    );
}

export default Test;