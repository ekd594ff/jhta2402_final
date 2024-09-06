import style from "../../styles/portfolio-img-list-item.module.scss"

function PortfolioImgListItem(props) {
    const {value, setter, modalOpener} = props;

    function onClick(event) {
        setter(`https://picsum.photos/seed/${value}/1200/800`);
        modalOpener(true);
    }

    return <div onClick={onClick} className={style['portfolio-img-list-item']}>
        <img src={`https://picsum.photos/seed/${value}/1200/800`} alt="image"/>
    </div>;
}

export default PortfolioImgListItem;