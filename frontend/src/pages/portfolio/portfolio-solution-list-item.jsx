import {ListItem} from "@mui/material";
import Checkbox from "@mui/material/Checkbox";
import React from "react";

import style from "../../styles/portfolio-solution-list-item.module.scss"

function PortfolioSolutionListItem(props) {

    const {title, description, price, setter, list, index} = props;

    return <ListItem
        disablePadding
        className={style['portfolio-solution-list-item']}
    >
        <div className={style['left']}>
            <Checkbox size="small" className={style['checkbox']} sx={{
                '& .MuiTouchRipple-root': {
                    display: 'none'
                },
                color: '#FA4D56',
                '&.Mui-checked': {
                    color: '#FA4D56',
                },
            }} onClick={() => {
                const newSelectedList = [...list];
                newSelectedList[index] = !newSelectedList[index];
                setter(newSelectedList);
            }}/>
        </div>
        <div className={style['right']}>
            <div className={style['top']}>
                <span className={style['title']}>{`솔루션 제목_${title}`}</span>
            </div>
            <div className={style['middle']}>
                <span className={style['price']}>{`${price.toLocaleString()}원`}</span>
            </div>
            <div className={style['bottom']}>
                <span
                    className={style['description']}>{`솔루션 내용_${description}_${description}${description}${description}${description}${description}`}</span>
            </div>
        </div>
    </ListItem>;
}

export default PortfolioSolutionListItem;