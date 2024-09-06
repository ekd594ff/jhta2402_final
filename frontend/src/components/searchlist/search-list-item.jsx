import Rating from '@mui/material/Rating';

import {Swiper, SwiperSlide} from 'swiper/react';
import {Pagination, Navigation} from 'swiper/modules';

import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';

import style from "../../styles/search-list-item.module.scss";
import {useNavigate} from "react-router-dom";

function SearchListItem(props) {
    const {title, description, companyName, imageUrls, portfolioId} = props;
    const navigator = useNavigate();
    return <li className={style['search-list-item']}>
        <div className={style['top']}>
            {
                imageUrls.length > 1 ? <Swiper
                    slidesPerView={1}
                    loop={true}
                    pagination={{
                        clickable: true,
                    }}
                    navigation={true}
                    modules={[Pagination, Navigation]}
                    className={style['swiper']}
                >
                    {
                        imageUrls.map((url, index) => <SwiperSlide key={`${companyName}_${url}`}>
                            <img className={style['thumbnail']}
                                 src={`https://picsum.photos/seed/${Date.now() + index}/1200/800`}
                                 alt='portfolio thumbnail'/>
                        </SwiperSlide>)
                    }
                </Swiper> : <img className={style['thumbnail']}
                                 src={`https://picsum.photos/seed/${Date.now() + companyName}/1200/800`}
                                 alt='portfolio thumbnail'/>
            }
        </div>
        <div className={style['bottom']} onClick={() => {
            navigator(`/portfolio/${portfolioId}`);
        }}>
            <div className={style['name']}>{companyName}</div>
            <div className={style['info']}>
                <div className={style['rating']}>
                    <Rating readOnly defaultValue={1} max={1} size="small"/>
                    4.8
                </div>
                {title}
            </div>
            <div className={style['description']}>{description}</div>
        </div>
    </li>;
}

export default SearchListItem;