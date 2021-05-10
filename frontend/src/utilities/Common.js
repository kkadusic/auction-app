import {decode} from "jsonwebtoken";

// Set token and user to local storage
export const setSession = (user, token) => {
    localStorage.setItem('auction-token', token);
    localStorage.setItem('auction-user', JSON.stringify(user));
}

// Remember email & password info with local storage
export const setRememberInfo = (email, password) => {
    localStorage.setItem('auction-email', email);
    localStorage.setItem('auction-password', password);
}

// Get email & password info from local storage
export const getRememberInfo = () => {
    let email = localStorage.getItem('auction-email');
    let password = localStorage.getItem('auction-password');
    return {email, password};
}

// Remove email & password info from local storage
export const removeRememberInfo = () => {
    localStorage.removeItem('auction-email');
    localStorage.removeItem('auction-password');
}

// Remove token and user from local storage
export const removeSession = () => {
    localStorage.removeItem('auction-token');
    localStorage.removeItem('auction-user');
};

// Return user from local storage
export const getUser = () => {
    const user = localStorage.getItem('auction-user');
    return user ? JSON.parse(user) : null;
};

export const setUser = (user) => {
    localStorage.setItem('auction-user', JSON.stringify(user));
};

// Return user id from the local storage
export const getUserId = () => {
    const user = localStorage.getItem('auction-user');
    return user ? JSON.parse(user).id : null;
};

// Return token from local storage
export const getToken = () => {
    return localStorage.getItem('auction-token') || null;
}

export const validToken = () => {
    const token = getToken();
    if (token === null) {
        return false;
    }
    const exp = decode(token, {complete: true}).payload.exp;
    return Date.now() < exp * 1000;
}
