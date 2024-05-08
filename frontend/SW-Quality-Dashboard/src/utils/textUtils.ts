export function formatText(txt, suffix = "") {
    if (isNaN(txt) || txt === null || txt === undefined || txt === "") {
        return "N/A";
    }

    const parsedInt = parseInt(txt);

    if (parsedInt < 1000) return txt;

    if (parsedInt > 1000) {
        const num = parseInt(txt);
        const roundedNum = Math.round(num / 100) / 10;
        return parseInt(roundedNum) + suffix;
    }
    return txt;
}

export function truncateString(str, maxLength) {
    if (str.length > maxLength) {
        return str.slice(0, maxLength) + '...';
    } else {
        return str;
    }
}
