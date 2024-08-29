import style from "../../styles/recommend-slide-content.module.scss";

function RecommendSlideContent(props) {
    const {value} = props;
    return <div className={style['recommend-slide-content']}>
        <img src={`https://picsum.photos/seed/${Date.now() + value}/1200/800`} alt={"recommend-interior"}/>
        <div className={style['info']}>
            <h1 className={style['portfolio-name']}>시원한 마루</h1>
            <h3 className={style['company-name']}>한옥명가</h3>
        </div>
    </div>
}

export default RecommendSlideContent;