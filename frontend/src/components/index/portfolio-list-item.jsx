import Rating from '@mui/material/Rating';

import style from "../../styles/portfolio-list-item.module.scss";

function PortfolioListItem(props) {
    const {value} = props;
    return <li className={style['portfolio-list-item']}>
        <div className={style['top']}>
            <img className={style['thumbnail']} src={`https://picsum.photos/seed/${Date.now() + value}/1200/800`}
                 alt='portfolio thumbnail'/>
        </div>
        <div className={style['bottom']}>
            <div className={style['name']}>포트폴리오 이름</div>
            <div className={style['info']}>
                <div className={style['rating']}>
                    <Rating readOnly defaultValue={1} max={1} size="small"/>
                    4.8
                </div>
                포트폴리오 정보
            </div>
            <div className={style['description']}>포트폴리오 설명</div>
        </div>
    </li>;
}

export default PortfolioListItem;