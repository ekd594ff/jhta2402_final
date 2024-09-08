import style from "../../styles/footer.module.scss";

function Footer() {
    const makers = ["김상빈", "김진현", "김태환", "엄인용", "윤형민"];
    return <footer className={style['footer']}>
        <div className={style['container']}>
            <div className={style['logo']}>
                <img src="/logo_footer.svg" alt="logo"/>
            </div>
            <div className={style['team']}>{makers.map(item => <span key={item}>{item}</span>)}</div>
            <div className={style['github']}>
                <img src="/icon/github.svg" alt="github"/>
                <a href="https://github.com/ekd594ff/jhta2402_final" target="_blank">Github Repository</a>
            </div>
        </div>
    </footer>;
}

export default Footer;