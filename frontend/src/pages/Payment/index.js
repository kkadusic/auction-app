import React, {useEffect, useState} from 'react';
import {useHistory} from 'react-router-dom';
import {Button, Form, InputGroup} from 'react-bootstrap';
import {useAlertContext, useBreadcrumbContext} from "../../AppContext";
import {myAccountBidsUrl, myAccountUrl} from "../../utilities/AppUrl";
import CardForm, {
    cardFormInitialValues,
    cardFormSchema,
} from "../../components/Forms/CardForm";
import {callCodeForCountry, citiesByCountry, countries, validPhoneNumber} from "../../utilities/Location";
import {getUser} from "../../utilities/Common";
import {Formik, getIn} from 'formik';
import {getCard} from "../../utilities/ServerCall";
import {pay, rate} from "../../utilities/ServerCall";
import RateUser from "../../components/Modals/RateUser";
import MyPrompt from "../../components/MyPrompt";
import * as yup from 'yup';

import './payment.css';

const Payment = () => {

    const history = useHistory();
    const product = history.location.state != null && history.location.state.product ? history.location.state.product : {};
    const user = getUser();

    const [country, setCountry] = useState(user.country || null);
    const [callCode, setCallCode] = useState(null);
    const [card, setCard] = useState({});
    const [payPal, setPayPal] = useState(false);
    const [loading, setLoading] = useState(false);

    const {setBreadcrumb} = useBreadcrumbContext();
    const {showMessage} = useAlertContext();

    const [showModal, setShowModal] = useState(false);
    const [promptVisible, setPromptVisible] = useState(false);
    const [finished, setFinished] = useState(false);

    useEffect(() => {
        setBreadcrumb("MY ACCOUNT", [{text: "MY ACCOUNT", href: myAccountUrl}, {
            text: "BIDS",
            href: myAccountBidsUrl
        }, {text: "PAYMENT"}]);

        const fetchData = async () => {
            try {
                setCard(await getCard());
            } catch (e) {
                showMessage("warning", "Unable to load card data");
            }
        }
        fetchData();
        // eslint-disable-next-line
    }, [])

    useEffect(() => {
        setCallCode(callCodeForCountry(country));
    }, [country])

    const schema = yup.object().shape({
        street: yup.string()
            .required("*Address is required")
            .max(255, "*Address can't be longer than 255 characters"),
        country: yup.string()
            .required("*Country is required")
            .max(255, "*Country can't be longer than 255 characters"),
        city: yup.string()
            .required("*City is required")
            .max(255, "*City can't be longer than 255 characters"),
        zip: yup.string()
            .required("*Zip is required")
            .max(32, "*Zip can't be longer than 32 characters"),
        phoneNumber: yup.string()
            .required("*Phone is required")
            .max(32, "*Phone can't be longer than 32 characters")
            .test("digits-only", "*Phone number can only contain digits", value => /^\d*$/.test(value))
            .test("country-selected", "*Select a country", () => country !== null)
            .test("valid-phone", "*Phone must be valid", value => validPhoneNumber(value, country, false)),
        card: !payPal ? cardFormSchema(false, card.cardNumber) : null,
    });

    const handleSubmit = async (data) => {
        setLoading(true);
        data.productId = product.id;
        try {
            await pay(data);
        } catch (e) {
            setLoading(false);
            return;
        }
        setLoading(false);
        setPromptVisible(true);
        setShowModal(true);
    }

    useEffect(() => {
        console.log(product);
        if (finished)
            history.push({
                pathname: myAccountBidsUrl,
                state: {productName: product.name}
            })
        // eslint-disable-next-line
    }, [finished]);

    const onDone = async (rating) => {
        if (rating >= 1 && rating <= 5) {
            try {
                await rate(product.id, rating);
            } catch (e) {
                return;
            }
        }
        setPromptVisible(false);
        setFinished(true);
    }

    return (
        <div className="tab-container">
            <MyPrompt promptVisible={promptVisible}/>
            <RateUser onDone={onDone} showModal={showModal} personAddedId={product.personId}/>
            <div className="tab-title">
                PAYMENT
            </div>
            <div className="tab-content">
                <Formik
                    validationSchema={schema}
                    initialValues={{
                        street: user.street || "",
                        country: user.country || "",
                        city: user.city || "",
                        zip: user.zip || "",
                        phoneNumber: user.phoneNumber || "",
                        card: cardFormInitialValues(user.card || {}),
                    }}
                    onSubmit={handleSubmit}
                >
                    {({
                          handleSubmit,
                          handleChange,
                          touched,
                          errors,
                          setFieldValue
                      }) => (
                        <Form noValidate onSubmit={handleSubmit}>
                            <Form.Group style={{marginBottom: 40}}>
                                <Form.Label>Address</Form.Label>
                                <Form.Control
                                    className="form-control-gray-no-shadow"
                                    size="xl-18"
                                    name="street"
                                    defaultValue={user.street || ""}
                                    placeholder="e.g. 432 Park Avenue"
                                    onChange={handleChange}
                                    maxLength={255}
                                    isInvalid={touched.street && errors.street}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.street}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group style={{display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap'}}>
                                <Form.Group className="form-half-width">
                                    <Form.Control
                                        defaultValue={user.country || "Select Country"}
                                        name="country"
                                        onChange={(e) => {
                                            setCountry(e.target.value);
                                            handleChange(e);
                                        }}
                                        size="xl-18"
                                        as="select"
                                        isInvalid={touched.country && errors.country}
                                    >
                                        <option value="Select Country" disabled hidden>Select Country</option>
                                        {countries.map(country => (
                                            <option key={country} value={country}>{country}</option>
                                        ))}
                                    </Form.Control>
                                    <Form.Control.Feedback type="invalid">
                                        {errors.country}
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Form.Group className="form-half-width">
                                    <Form.Control
                                        defaultValue={user.city || "Select City"}
                                        name="city"
                                        onChange={handleChange}
                                        size="xl-18"
                                        as="select"
                                        isInvalid={touched.city && errors.city}
                                    >
                                        <option value="Select City" hidden>Select City</option>
                                        {citiesByCountry(country).map(city => (
                                            <option key={city} value={city}>{city}</option>
                                        ))}
                                    </Form.Control>
                                    <Form.Control.Feedback type="invalid">
                                        {errors.city}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </Form.Group>

                            <Form.Group className="sell-form-margin">
                                <Form.Label>Zip Code</Form.Label>
                                <Form.Control
                                    className="form-control-gray-no-shadow"
                                    size="xl-18"
                                    name="zip"
                                    defaultValue={user.zip || ""}
                                    placeholder="e.g. 10022"
                                    onChange={handleChange}
                                    maxLength={32}
                                    isInvalid={touched.zip && errors.zip}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.zip}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="sell-form-margin">
                                <Form.Label>Phone</Form.Label>
                                <InputGroup>
                                    <InputGroup.Prepend>
                                        <InputGroup.Text>{callCode}</InputGroup.Text>
                                    </InputGroup.Prepend>
                                    <Form.Control
                                        className="form-control-gray-no-shadow"
                                        size="xl-18"
                                        name="phone"
                                        defaultValue={user.phoneNumber || ""}
                                        placeholder="e.g. 62123456"
                                        onChange={handleChange}
                                        maxLength={32}
                                        isInvalid={touched.phoneNumber && errors.phoneNumber}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        {errors.phoneNumber}
                                    </Form.Control.Feedback>
                                </InputGroup>
                            </Form.Group>

                            <Form.Group className="sell-form-margin">
                                <Form.Text style={{textAlign: 'left'}} className="form-control-description">
                                    {product.shipping ?
                                        "This product will be shipped to the address you enter above." :
                                        "Please agree with the seller on how this item will be delivered."
                                    }
                                </Form.Text>
                            </Form.Group>

                            <Form.Group className="sell-form-margin">
                                <Form.Label style={{fontSize: 20, letterSpacing: 0.7}}>Payment information</Form.Label>
                                <div className="gray-line"/>
                                <CardForm
                                    card={card || {}}
                                    payPal={{}}
                                    payPalDisabled={false}
                                    handleChange={handleChange}
                                    touched={touched}
                                    errors={errors}
                                    price={product.price}
                                    setPayPal={setPayPal}
                                    setFieldValue={setFieldValue}
                                />
                                <Form.Control.Feedback
                                    className={payPal && getIn(errors, 'payPal.orderId') ? "d-block" : null}
                                    type="invalid">
                                    {getIn(errors, 'payPal.orderId')}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Button
                                className="payment-done-button"
                                type="submit"
                                size="xxl"
                                variant="fill-purple-shadow"
                                disabled={loading}
                            >
                                DONE
                            </Button>
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
}

export default Payment;
