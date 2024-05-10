// This method takes any number and returns a string with the number formatted to a shorter version.
export function formatText(txt, suffix = "") {
    if (txt >= 1000000) {
        return (txt / 1000000).toFixed(1) + "M";
    } else if (txt >= 1000) {
        return (txt / 1000).toFixed(1) + "k";
    } else {
        return txt;
    }
}

export function truncateString(str, maxLength) {
    if (str.length > maxLength) {
        return str.slice(0, maxLength) + '...';
    } else {
        return str;
    }
}
