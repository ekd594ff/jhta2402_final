export const formatPhoneNumber = (value) => {
    value = value.replace(/\D/g, ''); //숫자 입력만 받기

    if (value.length >= 12) {
        value = value.substring(0, 11);
    }

    if (value.length === 8) {
        return value.replace(/-/g, '').replace(/(\d{4})(\d{4})/, '$1-$2');
    } else if (value.length === 9) {
        if (value.substring(0, 2) === "02") {
            return value.replace(/-/g, '').replace(/(\d{2})(\d{4})(\d{3})/, '$1-$2-$3');
        } else {
            return value.replace(/-/g, '').replace(/(\d{3})(\d{3})(\d{3})/, '$1-$2-$3');
        }
    } else if (value.length === 10) {
        if (value.substring(0, 2) === "02") {
            return value.replace(/-/g, '').replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3');
        } else {
            return value.replace(/-/g, '').replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
        }
    } else if (value.length >= 11) {
        return value.replace(/-/g, '').replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
    } else {
        return value;
    }
};