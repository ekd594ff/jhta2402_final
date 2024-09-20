import Rating from "@mui/material/Rating";
import StarIcon from '@mui/icons-material/Star';

import style from "../../styles/portfolio-review-list-item.module.scss";
import {Tooltip} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import NotificationsIcon from "@mui/icons-material/Notifications.js";
import React from "react";

function PortfolioReviewListItem(props) {
    const {createdAt, description, id, rate, title, updatedAt, username, onReport} = props;
    const date = new Date(createdAt);
    const dateString = `${date.getFullYear()}.${date.getMonth() + 1}.${date.getDate()}`;
    return <li className={style['portfolio-review-list-item']}>
        <div className={style['top']}>
            <span className={style['username']}>{username}</span>
            <span className={style['createdAt']}>{dateString}</span>
            <span className={style['report']}>
                <Tooltip title="신고하기">
                    <IconButton className={style['report-btn']} size="small" disableRipple onClick={() => {
                        if (onReport) {
                            onReport();
                        }
                    }}>
                        <NotificationsIcon/>
                    </IconButton>
                </Tooltip>
            </span>
        </div>
        <div className={style['middle']}>
            <div className={style['rate']}>
                <Rating
                    size="small"
                    value={rate}
                    readOnly
                    precision={0.1}
                    sx={{color: '#FA4D56FF'}}
                    emptyIcon={<StarIcon style={{opacity: 0.55}} fontSize="inherit"/>}
                />
                <span className={style['rate-value']}>{rate}</span>
            </div>
        </div>
        <div className={style['bottom']}>
            <div className={style['title']}>{`${title}`}</div>
            <div className={style['description']}>{`${description}`}</div>
        </div>
    </li>
}

export default PortfolioReviewListItem;