import React, {useState} from 'react';
import {Form, InputGroup, Spinner} from 'react-bootstrap';
import moment from 'moment';
import * as yup from 'yup';
import {BirthDateForm} from './BirthDateForm';
import {getGeoInfo} from "../../utilities/ServerCall";
import {validPhoneNumber} from "../../utilities/Location";

export const requiredFormSchema = {
    firstName: yup.string()
        .min(2, "*First name must have at least 2 characters")
        .max(100, "*First name can't be longer than 100 characters")
        .required("*First name is required"),
    lastName: yup.string()
        .min(2, "*Last name must have at least 2 characters")
        .max(100, "*Last name can't be longer than 100 characters")
        .required("*Last name is required"),
    gender: yup.string()
        .required("*Gender is required")
        .test("gender-test", "*Gender is required", value => value === "MALE" || value === "FEMALE"),
    day: yup.number()
        .typeError("*Day is required")
        .min(0, "*Day is required")
        .required("*Day is required"),
    month: yup.number()
        .typeError("*Month is required")
        .min(0, "*Month is required")
        .required("*Month is required"),
    year: yup.number()
        .typeError("*Year is required")
        .min(0, "*Year is required")
        .required("*Year is required"),
    phoneNumber: yup.string()
        .required("*Phone number is required")
        .test("digits-only", "Phone number only contain digits", value => /^\d*$/.test(value))
        .max(32, "*Phone number can't be longer than 32 characters"),
    email: yup.string()
        .email("*Email must be valid")
        .max(100, "*Email can't be longer than 100 characters")
        .required("*Email is required"),
    verified: yup.boolean(),
};

export const requiredFormInitialValues = (user) => {
    const dob = user.birthDate !== undefined ? moment(user.birthDate) : null;

    return {
        firstName: user.firstName || "",
        lastName: user.lastName || "",
        gender: user.gender || "Gender",
        day: dob !== null ? dob.day() : -1,
        month: dob !== null ? dob.month() : -1,
        year: dob !== null ? dob.year() : -1,
        phoneNumber: user.phoneNumber || "",
        email: user.email || "",
        verified: user.verified || false
    };
};

const RequiredForm = ({initialPhoneNumber, handleChange, touched, errors, values, setFieldValue}) => {
    const [verified, setVerified] = useState(values.verified);
    const [invalid, setInvalid] = useState(false);
    const [loading, setLoading] = useState(false);
    const [countryCode, setCountryCode] = useState(null);

    const verifyPhoneNumber = async (phoneNumber) => {
        if (verified)
            return;
        if (phoneNumber === "" || errors.phoneNumber) {
            setVerified(false);
            setFieldValue("verified", false);
            setInvalid(true);
            return;
        }
        setLoading(true);
        let country = countryCode;
        if (country === null) {
            try {
                country = (await getGeoInfo()).country_code;
                setCountryCode(country);
            } catch (e) {
                setLoading(false);
                return;
            }
        }
        const isValid = validPhoneNumber(phoneNumber, country, true);
        setVerified(isValid);
        setFieldValue("verified", isValid);
        setInvalid(!isValid);
        setLoading(false);
    }

    const getVerifyText = () => {
        if (verified)
            return "Verified";
        if (invalid)
            return "Not valid. Try again?";
        return "Click to verify";
    }

    return (
        <>
            <Form.Group>
                <Form.Label>First name</Form.Label>
                <Form.Control
                    className="form-control-gray-no-shadow"
                    size="lg-18"
                    name="firstName"
                    defaultValue={values.firstName || ""}
                    placeholder="e.g. John"
                    onChange={handleChange}
                    maxLength={100}
                    isInvalid={touched.firstName && errors.firstName}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.firstName}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group>
                <Form.Label>Last name</Form.Label>
                <Form.Control
                    className="form-control-gray-no-shadow"
                    size="lg-18"
                    name="lastName"
                    defaultValue={values.lastName || ""}
                    placeholder="e.g. Doe"
                    onChange={handleChange}
                    maxLength={100}
                    isInvalid={touched.lastName && errors.lastName}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.lastName}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group style={{width: '50%'}}>
                <Form.Label>I am</Form.Label>
                <Form.Control
                    defaultValue={values.gender || "Gender"}
                    name="gender"
                    onChange={handleChange}
                    size="lg-18"
                    as="select"
                    isInvalid={touched.gender && errors.gender}
                >
                    <option value="Gender" disabled hidden>Gender</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                </Form.Control>
                <Form.Control.Feedback type="invalid">
                    {errors.gender}
                </Form.Control.Feedback>
            </Form.Group>

            <BirthDateForm handleChange={handleChange} touched={touched} values={values} errors={errors}
                           setFieldValue={setFieldValue}/>

            <Form.Group>
                <Form.Label>Phone Number</Form.Label>
                <InputGroup>
                    <Form.Control
                        className="form-control-gray-no-shadow"
                        size="lg-18"
                        name="phoneNumber"
                        defaultValue={values.phoneNumber || ""}
                        placeholder="e.g. 61222333"
                        onChange={e => {
                            setVerified(initialPhoneNumber === e.target.value);
                            handleChange(e);
                        }}
                        maxLength={32}
                        isInvalid={touched.phoneNumber && errors.phoneNumber}
                    />
                    <InputGroup.Append onClick={() => verifyPhoneNumber(values.phoneNumber)}>
                        <InputGroup.Text
                            style={verified ? {color: '#417505'} : {
                                color: '#9B9B9B',
                                cursor: 'pointer'
                            }}
                            className="verify-phone"
                        >
                            {getVerifyText()}
                            {loading ? <Spinner className="text-spinner" animation="border"/> : null}
                        </InputGroup.Text>
                    </InputGroup.Append>
                    <Form.Control.Feedback type="invalid">
                        {errors.phoneNumber}
                    </Form.Control.Feedback>
                </InputGroup>
            </Form.Group>

            <Form.Group>
                <Form.Label>Email</Form.Label>
                <Form.Control
                    className="form-control-gray-no-shadow"
                    size="lg-18"
                    name="email"
                    type="email"
                    defaultValue={values.email || ""}
                    placeholder="e.g. john.doe@gmail.com"
                    onChange={handleChange}
                    maxLength={100}
                    isInvalid={touched.email && errors.email}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.email}
                </Form.Control.Feedback>
            </Form.Group>
        </>
    );
}

export default RequiredForm;
