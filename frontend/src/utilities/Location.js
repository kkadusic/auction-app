import countriesJSON from "../assets/json/countries.min.json";
import countryCodesJSON from "../assets/json/country-codes.min.json";
import parsePhoneNumberFromString from "libphonenumber-js";

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

export const validPhoneNumber = (phone, country, isCountryCode) => {
    if (phone === undefined)
        return false;
    const parsedPhoneNumber = parsePhoneNumberFromString(phone, isCountryCode ? country : codeForCountry(country));
    if (parsedPhoneNumber === undefined)
        return false;
    return parsedPhoneNumber.isValid();
}

export const toBase64 = file => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
});
