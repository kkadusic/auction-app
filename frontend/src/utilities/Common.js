// Set token and user to local storage
export const setSession = (user, token) => {
    localStorage.setItem('auction-token', token);
    localStorage.setItem('auction-user', JSON.stringify(user));
}

// Remove token and user from local storage
export const removeSession = () => {
    localStorage.removeItem('auction-token');
    localStorage.removeItem('auction-user');
};

// Return user from local storage
export const getUser = () => {
    const user = localStorage.getItem('auction-user');
    return user ?  JSON.parse(user) : null;
};

// Return token from local storage
export const getToken = () => {
    return localStorage.getItem('auction-token') || null;
}
