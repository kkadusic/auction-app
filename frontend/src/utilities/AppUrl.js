export const allCategoriesUrl = "/all";

export const removeSpaces = (name) => name.split(' ').join('_').toLowerCase();

export const productUrl = (product) => {
    return `/shop/${removeSpaces(product.categoryName)}/${removeSpaces(product.subcategoryName)}/${product.id}`;
}

export const categoryUrl = (category) => {
    return `/shop/${removeSpaces(category.name)}`;
}
