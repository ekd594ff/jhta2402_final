import React from 'react';
import {Link, useNavigate} from "react-router-dom";
import {Button} from "@mui/material";

import style from "../styles/index.module.scss";

function Index(props) {
    const navigator = useNavigate();
    return (
        <main className={style['index']}>
            <div className={style['container']}>
                <div className={style['box']}>
                    <Button variant="contained" className={style['btn']} color="success" onClick={() => {
                        navigator("/example")
                    }}>
                        📋 Board
                    </Button>
                </div>
            </div>
        </main>
    );
}

export default Index;