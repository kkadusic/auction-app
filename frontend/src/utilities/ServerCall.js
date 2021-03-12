import axios from 'axios';
import {getToken} from "./Common";

const hostUrl = 'https://auction-abh-server.herokuapp.com';

const config = () => {
    return {
        headers: {
            'Authorization': 'Bearer ' + getToken()
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

export const getProduct = async (productId, userId) => {
    return (await axios.get(`${hostUrl}/products/?product_id=${productId}&user_id=${userId}`)).data;
};

export const getBidsForProduct = async (id) => {
    return (await axios.get(hostUrl + '/bids/product/?id=' + id)).data;
};

export const bidForProduct = async (price, productId) => {
    return (await axios.post(hostUrl + '/bids/add', {price, productId}, config())).data;
};

export const wishlistProduct = async (personId, productId) => {
    return (await axios.post(hostUrl + '/wishlist/add', {personId, productId},)).data;
};

export const removeWishlistProduct = async (personId, productId) => {
    return (await axios.post(hostUrl + '/wishlist/remove', {personId, productId},)).data;
};
