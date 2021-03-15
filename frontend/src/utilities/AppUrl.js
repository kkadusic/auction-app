export const homeUrl = "/";
export const allCategoriesUrl = "/all";
export const loginUrl = "/login";
export const registerUrl = "/register";
export const myAccountUrl = "/my_account";
export const forgotPasswordUrl = "/forgot_password";
export const resetPasswordUrl = "/reset_password";
export const allCategoryUrl = "/all";
export const aboutUrl = "/about";
export const termsUrl = "/terms";

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
