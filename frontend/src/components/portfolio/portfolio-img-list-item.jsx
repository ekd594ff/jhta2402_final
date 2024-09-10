import style from "../../styles/portfolio-img-list-item.module.scss"

function PortfolioImgListItem(props) {
    const {value, setter, modalOpener} = props;

    function onClick(event) {
        setter(value);
        modalOpener(true);
    }

    return <div onClick={onClick} className={style['portfolio-img-list-item']}>
        <img src={value} alt="image"/>
    </div>;
}

export default PortfolioImgListItem;