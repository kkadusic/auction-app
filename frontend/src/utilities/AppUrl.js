export const homeUrl = "/";
export const allCategoriesUrl = "/all";

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
