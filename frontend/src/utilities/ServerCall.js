import axios from 'axios';
import {getToken, getUserId} from "./Common";

const hostUrl = process.env.REACT_APP_API_URL;
const cloudName = process.env.REACT_APP_CLOUD_NAME;
const uploadPreset = process.env.REACT_APP_UPLOAD_PRESET;

export const config = () => {
    const token = getToken();
    if (token === null) {
        return null;
    }
    return {
        headers: {
            'Authorization': 'Bearer ' + token
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

export const getProduct = async (productId, userId) => {
    return (await axios.get(hostUrl + '/products', getParams({productId, userId}))).data;
};

export const getBidsForProduct = async (id) => {
    return (await axios.get(hostUrl + '/bids/product', getParams({id}))).data;
};

export const bidForProduct = async (amount, productId) => {
    return (await axios.post(hostUrl + '/bids/add', {amount, productId}, config())).data;
};

export const searchProducts = async (query, category, subcategory, minPrice, maxPrice, page, sort) => {
    let headers;
    if (getUserId() === null) {
        headers = getParams({query, category, subcategory, minPrice, maxPrice, page, sort});
    } else {
        headers = {...config(), ...getParams({query, category, subcategory, minPrice, maxPrice, page, sort})};
    }
    return (await axios.get(hostUrl + '/products/search', headers)).data;
};

export const searchCountProducts = async (query, minPrice, maxPrice) => {
    return (await axios.get(hostUrl + '/products/search/count', getParams({query, minPrice, maxPrice}))).data;
};

export const filterCountProducts = async (query, category, subcategory, minPrice, maxPrice) => {
    return (await axios.get(hostUrl + '/products/filter/count', getParams({
        query,
        category,
        subcategory,
        minPrice,
        maxPrice
    }))).data;
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

export const validResetToken = async (token) => {
    return (await axios.post(hostUrl + '/auth/valid_token', {token})).data;
};

export const getUserBidProducts = async () => {
    return (await axios.get(hostUrl + '/products/user/bid', config())).data;
};

export const getProductFilters = async () => {
    return (await axios.get(hostUrl + '/products/filters')).data;
};

export const getSubcategoriesForCategory = async (id) => {
    return (await axios.get(hostUrl + '/subcategories/category', getParams({id}))).data;
}

export const addProduct = async (product) => {
    return (await axios.post(hostUrl + '/products/add', {...product}, config())).data;
};

export const uploadImage = async (imageFile) => {
    const formData = new FormData();
    formData.append('file', imageFile);
    formData.append('upload_preset', uploadPreset);
    return (await axios.post('https://api.Cloudinary.com/v1_1/' + cloudName + '/image/upload', formData)).data.secure_url;
}

export const getGeoInfo = async () => {
    return (await axios.get('https://ipapi.co/json',)).data;
};

export const getCard = async () => {
    return (await axios.get(hostUrl + '/cards/person', config())).data;
};

export const updateUser = async (user) => {
    return (await axios.put(hostUrl + '/auth/update', user, config())).data;
};

export const getUserProducts = async () => {
    return (await axios.get(hostUrl + '/products/user', config())).data;
};

export const deactivate = async (password) => {
    return (await axios.post(hostUrl + '/auth/deactivate', {password}, config())).data;
};

export const getReceipt = async (productId) => {
    const headers = {...config(), ...getParams({productId})};
    return (await axios.get(hostUrl + '/payments/receipt', headers)).data;
};

export const pay = async (data) => {
    return (await axios.post(hostUrl + '/products/pay', {...data}, config())).data;
};

export const wishlistProduct = async (productId) => {
    return (await axios.post(hostUrl + '/wishlist/add', {productId}, config())).data;
};

export const removeWishlistProduct = async (productId) => {
    return (await axios.post(hostUrl + '/wishlist/remove', {productId}, config())).data;
};

export const getUserWishlistProducts = async () => {
    return (await axios.get(hostUrl + '/products/user/wishlist', config())).data;
};

export const updateNotifications = async (emailNotify, pushNotify) => {
    return (await axios.post(hostUrl + '/auth/notifications/update', {emailNotify, pushNotify}, config())).data;
};

export const getNotifications = async (page, size) => {
    const headers = {...config(), ...getParams({page, size})};
    return (await axios.get(hostUrl + '/notifications', headers)).data;
};

export const checkNotifications = async (uncheckedIds) => {
    const ids = uncheckedIds.join(",");
    const headers = {...config(), ...getParams({ids})};
    return (await axios.get(hostUrl + '/notifications/check', headers)).data;
};

export const getRelatedProducts = async (id) => {
    return (await axios.get(hostUrl + '/products/related/?id=' + id)).data;
};
