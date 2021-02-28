import './shop.css';
import {useEffect} from "react";

const Shop = ({setBreadcrumb}) => {

    useEffect(() => {
        setBreadcrumb(null, []);
        // eslint-disable-next-line
    }, [])

    return (
        <div>
            Shop
        </div>
    );
}

export default Shop;
