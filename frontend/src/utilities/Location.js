import moment from "moment";
import countriesJSON from "../assets/json/countries.min.json";
import countryCodesJSON from "../assets/json/country-codes.min.json";

export const countries = Object.keys(countriesJSON);

export const citiesByCountry = (country) => {
    if (country == null)
        return [];
    const result = countriesJSON[country];
    return result !== undefined ? result : [];
}

export const callCodeForCountry = (country) => {
    if (country == null)
        return "";
    const result = countryCodesJSON.countryCodes.filter(el => el.country_name === country);
    return result.length === 1 ? result[0].dialling_code : "";
}

export const codeForCountry = (country) => {
    if (country == null)
        return "";
    const result = countryCodesJSON.countryCodes.filter(el => el.country_name === country);
    return result.length === 1 ? result[0].country_code : "";
}

export const getNextYears = (n) => {
    const year = moment().year();
    return [...Array(n).keys()].map(x => year + x);
}
