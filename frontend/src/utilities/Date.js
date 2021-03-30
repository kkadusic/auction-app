import moment, {months} from "moment";

export const longDateTimeFormat = "D MMMM YYYY [at] HH:mm";

export const getMonths = () => {
    return months();
}

export const getCurrentYear = () => {
    return moment().year();
}

export const getCurrentMonth = () => {
    return moment().month();
}

export const getDurationBetweenDates = (date1, date2) => {
    return moment.duration(date2.diff(date1)).format("Y [years], M [months], W [weeks], D [days], h[h] m[m]", {
        trim: "all"
    });
}
