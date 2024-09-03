import style from "../../styles/portfolio-img-list-item.module.scss"

function PortfolioImgListItem(props) {
    const {value, setter} = props;

    // function onClick(event) {
    //     setter(`https://picsum.photos/seed/${value}/1200/800`);
    // }

    return <li onClick={() => {
    }} className={style['portfolio-img-list-item']}>
    </li>;
}

export default PortfolioImgListItem;