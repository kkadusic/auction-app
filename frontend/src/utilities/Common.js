// Set token and user to local storage
export const setUser = (token, user) => {
    localStorage.setItem('auction-token', token);
    localStorage.setItem('auction-user', JSON.stringify(user));
}

// Return user from the local storage
export const getUser = () => {
    return localStorage.getItem('auction-user') || null;
}

// Return token from local storage
export const getToken = () => {
    return localStorage.getItem('auction-token') || null;
}
