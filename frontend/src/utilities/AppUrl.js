export const homeUrl = "/";
export const allCategoriesUrl = "/all";
export const loginUrl = "/login";
export const registerUrl = "/register";
export const forgotPasswordUrl = "/forgot_password";
export const resetPasswordUrl = "/reset_password";
export const allCategoryUrl = "/all";
export const aboutUrl = "/about";
export const termsUrl = "/terms";
export const myAccountUrl = "/my-account";
export const myAccountSellerUrl = "/my-account/seller";
export const myAccountSellerSellUrl = "/my-account/seller/sell";
export const myAccountBidsUrl = "/my-account/bids";
export const myAccountWishlistUrl = "/my-account/wishlist";
export const myAccountSettingsUrl = "/my-account/settings";
export const myAccountBidsPayUrl = "/my-account/bids/payment";

export const productUrl = (product) => {
    return `/shop/${removeSpaces(product.categoryName)}/${removeSpaces(product.subcategoryName)}/${product.id}`;
}

export const categoryUrl = (category) => {
    return `/shop/${removeSpaces(category.name)}`;
}

export const subcategoryUrl = (subcategory, category) => {
    if (category === undefined) {
        return `/shop/${removeSpaces(subcategory.categoryName)}/${removeSpaces(subcategory.name)}`;
    }
    return `/shop/${removeSpaces(category.name)}/${removeSpaces(subcategory.name)}`;
}

export const removeSpaces = (name) => name.split(' ').join('_').toLowerCase();
