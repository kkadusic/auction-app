import React, {useEffect, useState} from 'react';
import {useHistory} from 'react-router-dom';
import {Formik} from 'formik';
import {Form, InputGroup} from 'react-bootstrap';
import SubmitButtons from './SubmitButtons';
import {countries, citiesByCountry, callCodeForCountry, codeForCountry} from "../../utilities/Location";
import parsePhoneNumberFromString from 'libphonenumber-js';
import {productUrl} from "../../utilities/AppUrl";
import * as yup from 'yup';

import './sellerTabs.css';

const SellTab3 = ({product, setProduct, setActiveTab, onDone}) => {

    const history = useHistory();
    const [country, setCountry] = useState(product.country || null);
    const [callCode, setCallCode] = useState(product.callCode || null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setCallCode(callCodeForCountry(country));
    }, [country]);

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
        phone: yup.string()
            .required("*Phone is required")
            .max(100, "*Phone can't be longer than 100 characters")
            .test("country-selected", "*Select a country", () => country !== null)
            .test("valid-phone", "*Phone must be valid", (value) => value !== undefined && parsePhoneNumberFromString(value, codeForCountry(country)) !== undefined),
        shipping: yup.bool(),
        featured: yup.bool()
    });

    const saveValues = (data) => {
        const newData = {...product, ...data, callCode: callCode};
        setProduct(newData);
        return newData;
    }

    const handleSubmit = async (data) => {
        const newData = saveValues(data);
        setLoading(true);
        const newProduct = await onDone(newData);
        if (newProduct === null)
            setLoading(false);
        else
            history.push(productUrl(newProduct));
    }

    return (
        <div className="tab-container">
            <div className="tab-title">
                LOCATION &#38; SHIPPING
            </div>
            <div className="tab-content">
                <Formik
                    validationSchema={schema}
                    initialValues={{
                        street: product.street || "",
                        country: product.country || "",
                        city: product.city || "",
                        zip: product.zip || "",
                        phone: product.phone || "",
                        shipping: false,
                        featured: false,
                    }}
                    onSubmit={handleSubmit}
                >
                    {({
                          handleSubmit,
                          handleChange,
                          touched,
                          errors,
                          values,
                          setFieldValue
                      }) => (
                        <Form noValidate onSubmit={handleSubmit}>
                            <Form.Group style={{marginBottom: 40}}>
                                <Form.Label>Address</Form.Label>
                                <Form.Control
                                    className="form-control-gray-no-shadow"
                                    size="xl-18"
                                    name="street"
                                    defaultValue={product.street || ""}
                                    placeholder="e.g. 432 Park Avenue"
                                    onChange={handleChange}
                                    maxLength={255}
                                    isInvalid={touched.street && errors.street}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.street}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group style={{display: 'flex', justifyContent: 'space-between'}}>
                                <Form.Group className="form-half-width">
                                    <Form.Control
                                        defaultValue={product.country || "Select Country"}
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
                                        defaultValue={product.city || "Select City"}
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
                                    defaultValue={product.zip || ""}
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
                                        defaultValue={product.phone || ""}
                                        placeholder="e.g. 555-1234"
                                        onChange={handleChange}
                                        maxLength={100}
                                        isInvalid={touched.phone && errors.phone}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        {errors.phone}
                                    </Form.Control.Feedback>
                                </InputGroup>
                            </Form.Group>
                            <SubmitButtons
                                onBack={() => {
                                    saveValues(values);
                                    setActiveTab(1);
                                }}
                                lastTab={true}
                                loading={loading}
                            />
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
}

export default SellTab3;
