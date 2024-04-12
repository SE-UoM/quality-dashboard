import React, { useState } from 'react'
import "./FloatingFormInput.css"

function FloatingFormInput({type, id, placeholder, isRequired, onChange, icon, labelText}) {

    return (
        <>
            <div className="form-floating mb-3">
                <input
                    type={type}
                    className="form-control"
                    id={id}
                    placeholder={placeholder}
                    required={isRequired}
                    onChange={onChange}
                />
                <label htmlFor="floatingInput">
                    <i className={icon}> </i>
                    {labelText}
                </label>
            </div>
        </>
    )
}

export default FloatingFormInput