import {Link} from "react-router-dom";
import {getCookie} from "../../utils/cookieUtil.jsx";
import PropTypes from "prop-types";

LoginLink.propTypes = {
    to: PropTypes.string.isRequired,
    className: PropTypes.string,
    children: PropTypes.node.isRequired,
}

function LoginLink({to, className, children}) {

    const handleClick = (e) => {
        if (getCookie('login') === '') {
            alert('로그인이 필요합니다');
            e.preventDefault();
        }
    }

    return (
        <Link to={to} className={className}
              onClick={(e) => handleClick(e)}>
            {children}
        </Link>
    );
}

export default LoginLink;