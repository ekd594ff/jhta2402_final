import React from 'react';
import {Link} from "react-router-dom";

function Index(props) {
    return (
        <div>
            <Link to={"/example"}>Example</Link>
        </div>
    );
}

export default Index;