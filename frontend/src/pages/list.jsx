import React, {useEffect, useState} from 'react';
import axios from "axios";

function List(props) {

    const [list, setList] = useState([]);
    // 화면에 변경사항 반영하기 위해 useState 사용
    const [portfolio, setPortfolioList] = useState([]);

    useEffect(() => {
        axios.get("/api/portfolio/test2")
            .then(res => {
                console.log(res.data.content);
                setPortfolioList(res.data.content);
            });
    }, []);


    return (
        <div>
            {portfolio.map(p => <div key={p.title}>
                <p>{p.title}</p>
                <p>{p.description}</p>
                <p>{p.companyName}</p>
                <p>{p.createDate}</p>
            </div>)}

        </div>
    );
}

export default List;