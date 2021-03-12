import {useEffect} from 'react';
import {withRouter} from 'react-router-dom';

import './shop.css';

const Shop = ({match, setBreadcrumb}) => {

    useEffect(() => {
        formBreadcrumb();
        // eslint-disable-next-line
    }, [match.url])

    const formBreadcrumb = () => {
        const urlElements = match.url.split("/").slice(1);
        setBreadcrumb("SHOP", urlElements.map((el, i) => {
            return {
                text: el.toUpperCase().split("_").join(" "),
                href: "/" + urlElements.slice(0, i + 1).join("/")
            }
        }));
    }

    return (
        <div>

        </div>
    );
}

export default withRouter(Shop);
