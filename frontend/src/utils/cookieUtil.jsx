export const getCookie = (key) => {
    return document.cookie.replace(`/(?:(?:^|.*;\s*)${key}\s*\=\s*([^;]*).*$)|^.*$/`, "$1");
}

export const isEmpty = (value) => {
    return value == "" || value == null || (typeof value == "object" && !Object.keys(value).length);
}

export const getCookies = (key) => {
    return document.cookie
        .split(";")
        .map(item => item.trim())
        .reduce((acc, cur, index) => {
            const [keyName, value] = cur.split("=");
            acc[keyName] = value;
            return acc;
        }, {})[`${key}`];
}