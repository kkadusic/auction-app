import axios from 'axios';
import {getToken} from "./Common";

const hostUrl = process.env.REACT_APP_API_URL;

const config = () => {
    return {
        headers: {
            'Authorization': 'Bearer ' + getToken()
        }
    };
}

export const getParams = (args) => {
    return {
        params: {
            ...args
        }
    };
}

export const registerUser = async (user) => {
    return (await axios.post(hostUrl + '/auth/register', user)).data;
};

export const loginUser = async (user) => {
    return (await axios.post(hostUrl + '/auth/login', user)).data;
};

export const getCategories = async () => {
    return (await axios.get(hostUrl + '/categories')).data;
};

export const getRandomSubcategories = async () => {
    return (await axios.get(hostUrl + '/subcategories/random')).data;
};

export const getFeaturedRandomProducts = async () => {
    return (await axios.get(hostUrl + '/products/featured/random')).data;
};

export const getNewProducts = async () => {
    return (await axios.get(hostUrl + '/products/new')).data;
};

export const getLastProducts = async () => {
    return (await axios.get(hostUrl + '/products/last')).data;
};

export const getProduct = async (product_id, user_id) => {
    return (await axios.get(hostUrl + '/products', getParams({product_id, user_id}))).data;
};

export const getBidsForProduct = async (id) => {
    return (await axios.get(hostUrl + '/bids/product', getParams({id}))).data;
};

export const bidForProduct = async (price, productId) => {
    return (await axios.post(hostUrl + '/bids/add', {price, productId}, config())).data;
};

export const searchProducts = async (query, category, subcategory, page, sort) => {
    return (await axios.get(hostUrl + '/products/search', getParams({query, category, subcategory, page, sort}))).data;
};

export const searchCountProducts = async (query) => {
    return (await axios.get(hostUrl + '/products/search/count', getParams({query}))).data;
};

export const getSubcategories = async () => {
    return (await axios.get(hostUrl + '/subcategories')).data;
};

export const forgotPassword = async (email) => {
    return (await axios.post(hostUrl + '/auth/forgot_password', {email})).data;
};

export const resetPassword = async (token, password) => {
    return (await axios.post(hostUrl + '/auth/reset_password', {token, password})).data;
};
