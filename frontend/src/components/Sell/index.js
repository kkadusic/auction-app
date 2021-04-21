import React, {useEffect, useState} from 'react';
import {useBreadcrumbContext} from "../../AppContext";
import {myAccountSellerUrl, myAccountUrl} from "../../utilities/AppUrl";
import {Step, Stepper} from 'react-form-stepper';
import SellerTab1 from "../SellerTabs/SellerTab1";
import SellerTab2 from "../SellerTabs/SellerTab2";
import SellerTab3 from "../SellerTabs/SellerTab3";

import './sell.css';

const Sell = () => {
    const {setBreadcrumb} = useBreadcrumbContext();

    const [activeTab, setActiveTab] = useState(0);

    useEffect(() => {
        setBreadcrumb("MY ACCOUNT", [{text: "MY ACCOUNT", href: myAccountUrl}, {
            text: "BECOME SELLER",
            href: myAccountSellerUrl
        }]);
        // eslint-disable-next-line
    }, [])

    const renderTab = () => {
        switch (activeTab) {
            case 0:
                return (
                    <SellerTab1/>
                )
            case 1:
                return (
                    <SellerTab2/>
                )
            case 2:
                return (
                    <SellerTab3/>
                )
            default:
                return null;
        }
    }

    const renderStep = (active) => (
        <Step>
            <div className="white-circle">
                <div style={active ? {backgroundColor: '#8367D8'} : {backgroundColor: '#D8D8D8'}}
                     className="purple-circle"/>
            </div>
        </Step>
    )

    return (
        <>
            <Stepper
                activeStep={activeTab}
                styleConfig={{
                    activeBgColor: '#8367D8',
                    circleFontSize: 0,
                    completedBgColor: '#8367D8',
                    inactiveBgColor: '#D8D8D8',
                    size: '28px'
                }}
                connectorStateColors={true}
                connectorStyleConfig={{
                    activeColor: '#8367D8',
                    completedColor: '#8367D8',
                    disabledColor: '#D8D8D8',
                    size: '5px'
                }}
            >
                {renderStep(activeTab >= 0)}
                {renderStep(activeTab >= 1)}
                {renderStep(activeTab >= 2)}
            </Stepper>
            {renderTab()}
        </>
    );
}

export default Sell;
