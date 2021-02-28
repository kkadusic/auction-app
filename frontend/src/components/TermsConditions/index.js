import {useEffect} from 'react';

import './terms.css';

const TermsConditions = ({setBreadcrumb}) => {

    useEffect(() => {
        setBreadcrumb("TERMS AND CONDITIONS", [{text: "SHOP", href: "/shop"}, {text: "TERMS AND CONDITIONS"}]);
        // eslint-disable-next-line
    }, [])

    return (
        <div className="terms-container">
            <h1>Introduction</h1>
            <div className="terms-text">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis consequat pretium turpis, in eleifend
                mi laoreet sed. Donec ipsum mauris, venenatis sit amet porttitor id, laoreet eu magna. In convallis
                diam volutpat libero tincidunt semper. Ut aliquet erat rutrum, venenatis lacus ut, ornare lectus.
                Quisque congue ex sit amet diam malesuada, eget laoreet quam molestie. In id elementum turpis.
                Curabitur quis tincidunt mauris.
            </div>
            <h2>Some title here</h2>
            <div className="terms-text">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis consequat pretium turpis, in eleifend
                mi laoreet sed. Donec ipsum mauris, venenatis sit amet porttitor id, laoreet eu magna. In convallis
                diam volutpat libero tincidunt semper. Ut aliquet erat rutrum, venenatis lacus ut, ornare lectus.
                Quisque congue ex sit amet diam malesuada, eget laoreet quam molestie. In id elementum turpis.
                Curabitur quis tincidunt mauris.
            </div>
            <h2>Some title here</h2>
            <div className="terms-text">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis consequat pretium turpis, in eleifend
                mi laoreet sed. Donec ipsum mauris, venenatis sit amet porttitor id, laoreet eu magna. In convallis
                diam volutpat libero tincidunt semper. Ut aliquet erat rutrum, venenatis lacus ut, ornare lectus.
                Quisque congue ex sit amet diam malesuada, eget laoreet quam molestie. In id elementum turpis.
                Curabitur quis tincidunt mauris.
            </div>
        </div>
    );
}

export default TermsConditions;
