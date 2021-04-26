import './aboutUs.css'
import {useEffect} from "react";
import {useBreadcrumbContext} from "../../AppContext";

const AboutUs = () => {

    const {setBreadcrumb} = useBreadcrumbContext();

    useEffect(() => {
        setBreadcrumb("ABOUT US", [{text: "SHOP", href: "/shop"}, {text: "ABOUT US"}]);
        // eslint-disable-next-line
    }, [])

    return (
        <>
            <h1>About Us</h1>
            <div className="about-container">
                <div className="about-text">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis consequat pretium turpis, in eleifend
                    mi laoreet sed. Donec ipsum mauris, venenatis sit amet porttitor id, laoreet eu magna. In convallis
                    diam volutpat libero tincidunt semper. Ut aliquet erat rutrum, venenatis lacus ut, ornare lectus.
                    Quisque congue ex sit amet diam malesuada, eget laoreet quam molestie. In id elementum turpis.
                    Curabitur quis tincidunt mauris. Duis pharetra a odio vitae consectetur. Nullam vitae lacinia nisi,
                    at porta sapien. Etiam vehicula augue at lacus tempus euismod. Nullam sit amet eros ut metus
                    pulvinar volutpat et elementum lacus. Cras mauris mi, vulputate ac justo vitae, fringilla vestibulum
                    sapien. Sed hendrerit nulla id luctus placerat. Sed venenatis ornare augue, et viverra dolor
                    ullamcorper id. Duis id quam hendrerit, mollis ex ut, varius ipsum.
                    <br/>
                    <br/>
                    Etiam bibendum viverra nulla, at cursus leo fringilla eget. In pellentesque viverra elit id
                    vestibulum. Sed eget leo suscipit, commodo urna vitae, efficitur ligula. Pellentesque non mauris
                    blandit, ultrices nibh consectetur, auctor velit. Nunc ac justo lacus. Vivamus et gravida ante.
                    Quisque cursus augue ligula, aliquam ullamcorper enim ultricies sit amet. In placerat sapien eu
                    ligula commodo pharetra. Nunc et facilisis dolor, ut condimentum metus. Phasellus lacinia efficitur
                    diam sed pharetra. Nullam euismod magna at mauris hendrerit scelerisque vitae vel leo. Cras interdum
                    tellus in sapien fermentum consequat.
                </div>
                <div className="about-images">
                    <img className="about-large-image" alt=""
                         src="https://cdn.pixabay.com/photo/2017/07/10/23/49/club-2492011_960_720.jpg"/>
                    <img className="about-small-image" alt=""
                         src="https://cdn.pixabay.com/photo/2019/04/26/07/14/store-4156934_960_720.png"/>
                    <img className="about-small-image" alt=""
                         src="https://cdn.pixabay.com/photo/2019/09/30/15/22/shopping-cart-4516039_960_720.jpg"/>
                </div>
            </div>
        </>
    );
};

export default AboutUs;
