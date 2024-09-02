import style from "../../styles/portfolio-img-list-item.module.scss"

function PortfolioImgListItem(props) {
    const {value, setter} = props;

    function onClick(event) {
        setter(`https://picsum.photos/seed/${value}/1200/800`);
    }

    return <li className={style['portfolio-img-list-item']} onClick={onClick}>
        <img alt="photos" src={`https://picsum.photos/seed/${value}/1200/800`}/>
    </li>;
}

export default PortfolioImgListItem;