import style from "../../styles/portfolio-img-list-item.module.scss"

function PortfolioImgListItem(props) {
    const {value} = props;
    return <li className={style['portfolio-img-list-item']}>
        <img alt="photos" src={`https://picsum.photos/seed/${Date.now() + value}/1200/800`}/>
    </li>;
}

export default PortfolioImgListItem;