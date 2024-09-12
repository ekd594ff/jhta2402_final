import {useNavigate} from "react-router-dom";

import style from "../../styles/recommend-slide-content.module.scss";

function RecommendSlideContent(props) {
    const {
        isactivated,
        description,
        createdat,
        companyid,
        title,
        id,
        isdeleted,
        updatedat,
        companyname,
        thumbnail
    } = props;
    const navigator = useNavigate();

    function onClick(event) {
        event.stopPropagation();
        navigator(`/portfolio/${id}`)
    }


    return <div className={style['recommend-slide-content']}>
        <img src={thumbnail} alt="recommend-interior"/>
        <div className={style['info']}>
            <h1 className={style['portfolio-name']} onClick={onClick}>{title}</h1>
            <h3 className={style['company-name']}>{companyname}</h3>
        </div>
    </div>
}

export default RecommendSlideContent;