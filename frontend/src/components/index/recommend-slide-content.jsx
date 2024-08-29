import {SwiperSlide} from 'swiper/react';
import 'swiper/css';

import style from "../../styles/recommend-slide-content.module.scss";

function RecommendSlideContent(props) {
    const {} = props;
    return <div className={style['container']}>
        <img src={`https://picsum.photos/seed/picsum/1200/800`} alt={"recommend-interior"}/>
        <div className={style['info']}></div>
    </div>
}

export default RecommendSlideContent;