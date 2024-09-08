
export const getCookie = (key) => {
    return document.cookie.replace(`/(?:(?:^|.*;\s*)${key}\s*\=\s*([^;]*).*$)|^.*$/`, "$1");
}

export const isEmpty = (value) => {
    return value == "" || value == null || (typeof value == "object" && !Object.keys(value).length);
}
