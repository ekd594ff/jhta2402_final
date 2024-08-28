import React, {useEffect} from 'react';
import axios from "axios";

function List(props) {

    useEffect(() => {
        axios.get("api/portfolio/test2")
            .then(res => console.log(res));
    }, []);

    return (
        <div></div>
    );
}

export default List;